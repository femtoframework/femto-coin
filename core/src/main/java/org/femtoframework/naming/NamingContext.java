package org.femtoframework.naming;

import javax.naming.*;
import javax.naming.spi.NamingManager;
import javax.naming.spi.ResolveResult;
import java.lang.ref.WeakReference;
import java.net.URI;
import java.util.*;

/**
 * JNDI Context Implements
 *
 * @author fengyun
 * @version Feb 13, 2003 11:28:20 AM
 */

public class NamingContext extends AbstractContext
{
    private NamingService naming;

    // Cache of naming service stubs
    // This is a critical optimization in the case where new InitialContext
    // is performed often. The service stub will be shared between all those
    // calls, which will improve performance.
    // Weak references are used so if no contexts use a particular service
    // it will be removed from the cache.
    private static HashMap<String, WeakReference<NamingService>> cachedServices
        = new HashMap<String, WeakReference<NamingService>>();

    static void addService(String name, NamingService service)
    {
        // Add service to map
        // Clone and synchronize to minimize delay for readers of the map
        synchronized (NamingContext.class) {
            HashMap<String, WeakReference<NamingService>> newServices
                = (HashMap<String, WeakReference<NamingService>>)cachedServices.clone();
            newServices.put(name, new WeakReference<NamingService>(service));
            cachedServices = newServices;
        }
    }

    public static NamingService getService(URI uri)
        throws NamingException
    {
        String u = uri.toString();
        WeakReference<NamingService> ref = cachedServices.get(u);
        NamingService service;
        if (ref == null || (service = ref.get()) == null) {
            synchronized (cachedServices) {
                ref = cachedServices.get(u);
                if (ref == null || (service = (NamingService)ref.get()) == null) {
                    service = NamingUtil.createService(uri);
                    cachedServices.put(uri.toString(),
                        new WeakReference<NamingService>(service));
                }
            }
        }
        return service;
    }

    static void removeService(Hashtable env)
    {
        // Locate naming service
        try {
            URI uri = NamingUtil.getProviderURI(env);
            synchronized (cachedServices) {
                cachedServices.remove(uri.toString());
            }
        }
        catch (NamingException e) {
        }
    }

    /**
     * 返回主机信息
     *
     * @param providerUrl
     * @return
     * @throws InvalidNameException
     */
    static String getHostURI(String providerUrl) throws InvalidNameException
    {
        try {
            URI uri = new URI(providerUrl);
            return uri.getScheme() + "://" + uri.getHost() + ':' + uri.getPort();
        }
        catch (Exception e) {
            throw new InvalidNameException("Invalid provider url:" + providerUrl);
        }
    }

    /**
     * Called to remove any url scheme atoms and extract the naming
     * service hostname:port information.
     *
     * @param uri the name component to the parsed. After returning n will
     *            have all scheme related atoms removed.
     * @return the naming service hostname:port information string if name
     *         contained the host information.
     */
    static String parseNameForScheme(URI uri) throws InvalidNameException
    {
        try {
            return uri.getHost() + ':' + uri.getPort();
        }
        catch (Exception e) {
            throw new InvalidNameException("Invalid provider uri:" + uri);
        }
    }

    public NamingContext(Hashtable e, Name baseName, NamingService service)
        throws NamingException
    {
        super(e, baseName);
        this.naming = service;
    }

    public NamingContext(Hashtable e, String baseName, NamingService service)
        throws NamingException
    {
        super(e, baseName);

        this.naming = service;
    }

    public NamingContext(Hashtable e, Name baseName)
        throws NamingException
    {
        super(e, baseName);
    }

    public void rebind(Name name, Object obj)
        throws NamingException
    {
        Hashtable env = getEnv(name);
        checkRef(env);

        try {
            String className;

            // Referenceable
            if (obj instanceof Referenceable) {
                obj = ((Referenceable)obj).getReference();
            }

            if (!(obj instanceof Reference)) {
                className = obj.getClass().getName();
            }
            else {
                className = ((Reference)obj).getClassName();
            }

            naming.rebind(getAbsoluteName(name), obj, className);
        }
        catch (CannotProceedException cpe) {
            cpe.setEnvironment(env);
            Context cctx = NamingManager.getContinuationContext(cpe);
            cctx.rebind(cpe.getRemainingName(), obj);
        }
        catch (RuntimeException e) {
            naming = null;
            removeService(env);
            throw new CommException(e);
        }
    }

    public void bind(Name name, Object obj)
        throws NamingException
    {
        Hashtable env = getEnv(name);
        checkRef(env);

        try {
            String className;

            // Referenceable
            if (obj instanceof Referenceable) {
                obj = ((Referenceable)obj).getReference();
            }

            if (!(obj instanceof Reference)) {
                className = obj.getClass().getName();
            }
            else {
                className = ((Reference)obj).getClassName();
            }
            name = getAbsoluteName(name);
            naming.bind(name, obj, className);
        }
        catch (CannotProceedException cpe) {
            cpe.setEnvironment(env);
            Context cctx = NamingManager.getContinuationContext(cpe);
            cctx.bind(cpe.getRemainingName(), obj);
        }
        catch (RuntimeException e) {
            naming = null;
            removeService(env);
            throw new CommException(e);
        }
    }

    public Object lookup(Name name)
        throws NamingException
    {
        Hashtable env = getEnv(name);
        checkRef(env);

        if (name.isEmpty()) {
            return new NamingContext(env, prefix, naming);
        }

        try {
            Name n = getAbsoluteName(name);
            Object res = naming.lookup(n);
            if (res instanceof Context) {
                // Add env
                Enumeration keys = env.keys();
                while (keys.hasMoreElements()) {
                    String key = (String)keys.nextElement();
                    ((Context)res).addToEnvironment(key, env.get(key));
                }
                return res;
            }
            else if (res instanceof ResolveResult) {
                // Dereference partial result
                try {
                    Object resolveRes = ((ResolveResult)res).getResolvedObj();
                    if (resolveRes instanceof LinkRef) {
                        String ref = ((LinkRef)resolveRes).getLinkName();
                        Context ctx;
                        try {
                            if (ref.startsWith("./")) {
                                ctx = (Context)lookup(ref.substring(2));
                            }
                            else {
                                ctx = (Context)new InitialContext(env).lookup(ref);
                            }

                            return ctx.lookup(((ResolveResult)res).getRemainingName());
                        }
                        catch (ClassCastException e) {
                            throw new NotContextException(ref + " is not a context");
                        }
                    }
                    else {
                        try {
                            Context ctx = (Context)NamingManager.getObjectInstance(resolveRes,
                                getAbsoluteName(name),
                                this,
                                env);
                            return ctx.lookup(((ResolveResult)res).getRemainingName());
                        }
                        catch (ClassCastException e) {
                            throw new NotContextException();
                        }
                    }
                }
                catch (NamingException e) {
                    throw e;
                }
                catch (Exception e) {
                    NamingException ex = new NamingException("Could not dereference object");
                    ex.setRootCause(e);
                    throw ex;
                }
            }
            else if (res instanceof LinkRef) {
                // Dereference link
                try {
                    String ref = ((LinkRef)res).getLinkName();
                    if (ref.startsWith("./")) {
                        return lookup(ref.substring(2));
                    }
                    else {
                        return new InitialContext(env).lookup(ref);
                    }
                }
                catch (NamingException e) {
                    throw e;
                }
                catch (Exception e) {
                    NamingException ex = new NamingException("Could not dereference object");
                    ex.setRootCause(e);
                    throw ex;
                }
            }
            else if (res instanceof Reference) {
                // Dereference object
                try {
                    return NamingManager.getObjectInstance(res,
                        getAbsoluteName(name),
                        this,
                        env);
                }
                catch (NamingException e) {
                    throw e;
                }
                catch (Exception e) {
                    NamingException ex = new NamingException("Could not dereference object");
                    ex.setRootCause(e);
                    throw ex;
                }
            }

            return res;
        }
        catch (CannotProceedException cpe) {
            cpe.setEnvironment(env);
            Context cctx = NamingManager.getContinuationContext(cpe);
            return cctx.lookup(cpe.getRemainingName());
        }
        catch (RuntimeException e) {
            naming = null;
            removeService(env);
            throw new CommException(e);
        }
    }

    public void unbind(Name name)
        throws NamingException
    {
        Hashtable env = getEnv(name);
        checkRef(env);

        try {
            naming.unbind(getAbsoluteName(name));
        }
        catch (CannotProceedException cpe) {
            cpe.setEnvironment(env);
            Context cctx = NamingManager.getContinuationContext(cpe);
            cctx.unbind(cpe.getRemainingName());
        }
        catch (RuntimeException e) {
            naming = null;
            removeService(env);
            throw new CommException(e);
        }
    }

    public NamingEnumeration<NameClassPair> list(Name name)
        throws NamingException
    {
        Hashtable env = getEnv(name);
        checkRef(env);

        try {
            return new NamingIterator<>(naming.list(getAbsoluteName(name)));
        }
        catch (CannotProceedException cpe) {
            cpe.setEnvironment(env);
            Context cctx = NamingManager.getContinuationContext(cpe);
            return cctx.list(cpe.getRemainingName());
        }
        catch (RuntimeException e) {
            naming = null;
            removeService(env);
            throw new CommException(e);
        }
    }

    public NamingEnumeration<Binding> listBindings(Name name)
        throws NamingException
    {
        Hashtable env = getEnv(name);
        checkRef(env);

        try {
            // Get list
            Collection<Binding> bindings = naming.listBindings(getAbsoluteName(name));
            Collection<Binding> realBindings = new ArrayList<Binding>(bindings.size());

            // Convert marshalled objects
            for (Binding binding : bindings) {
                Object obj = binding.getObject();
                realBindings.add(new Binding(binding.getName(),
                    binding.getClassName(), obj));
            }

            // Return transformed list of bindings
            return new NamingIterator<Binding>(realBindings);
        }
        catch (CannotProceedException cpe) {
            cpe.setEnvironment(env);
            Context cctx = NamingManager.getContinuationContext(cpe);
            return cctx.listBindings(cpe.getRemainingName());
        }
        catch (RuntimeException e) {
            naming = null;
            removeService(env);
            throw new CommException(e);
        }
    }


    public Context createSubcontext(Name name)
        throws NamingException
    {
        if (name.size() == 0) {
            throw new InvalidNameException("Cannot pass an empty name to createSubcontext");
        }

        Hashtable env = getEnv(name);
        checkRef(env);
        try {
            //不用绝对名称
//            name = getGenerateName(name);
            return naming.createSubcontext(name);
        }
        catch (CannotProceedException cpe) {
            cpe.setEnvironment(env);
            Context cctx = NamingManager.getContinuationContext(cpe);
            return cctx.createSubcontext(cpe.getRemainingName());
        }
        catch (RuntimeException e) {
            naming = null;
            removeService(env);
            throw new CommException(e);
        }
    }

    public void close() throws NamingException
    {
        super.close();
        naming = null;
    }

    /**
     * Lookup the object referred to by name but don't dereferrence the final
     * component. This really just involves returning the raw value returned by
     * the NamingService.lookup() method.
     *
     * @return the raw object bound under name.
     */
    public Object lookupLink(Name name)
        throws NamingException
    {
        if (name.isEmpty()) {
            return lookup(name);
        }

        Object link;
        try {
            Name n = getAbsoluteName(name);
            link = naming.lookup(n);
        }
        catch (RuntimeException e) {
            naming = null;
            removeService(env);
            throw new CommException(e);
        }
        return link;
    }

    private void checkRef(Hashtable env)
        throws NamingException
    {
        if (naming == null) {
            // Locate naming service
            URI uri = NamingUtil.getProviderURI(env);
            if (uri != null) {
                // Get service from cache
                naming = getService(uri);
            }
            else {
                // Use service in same JVM
                naming = NamingUtil.getLocalService();
            }

            if (naming == null) {
                // Local, but no local JNDI provider found!
                throw new ConfigurationException("No valid Context.PROVIDER_URL was found:" + uri);
            }
        }
    }

    protected Name getAbsoluteName(Name n)
        throws NamingException
    {
        if (n.isEmpty()) {
            return composeName(n, prefix);
        }
        else if (n.get(0).length() == 0) { // Absolute name
            return n.getSuffix(1);
        }
        else { // Add prefix
            return composeName(n, prefix);
        }
    }

    private Hashtable getEnv(Name n) throws InvalidNameException
    {
        Hashtable nameEnv = env;
        if (n.size() >= 4) {
            String url = n.toString();
            if (url.indexOf("://") > 0) {
                String hostUri = getHostURI(url);
                if (hostUri != null) {
                    // Set hostname:port value for the naming service
                    nameEnv = (Hashtable)env.clone();
                    nameEnv.put(Context.PROVIDER_URL, hostUri);

                    // Scheme was "url:/..."
                    n.remove(0);
                    if (n.size() > 1 && n.get(0).length() == 0) {
                        // Scheme was "url://hostname:port/..."
                        // Get hostname:port value for the naming service
                        n.remove(0);
                        if (n.size() > 1 && n.get(0).length() == 0) {
                            n.remove(0);
                        }
                        //host:port
                        n.remove(0);
                        n.remove(0);
                        // If n is a empty atom remove it or else a '/' will result
                        if (n.size() == 1 && n.get(0).length() == 0) {
                            n.remove(0);
                        }
                    }
                }
            }
        }
        return nameEnv;
    }
}
