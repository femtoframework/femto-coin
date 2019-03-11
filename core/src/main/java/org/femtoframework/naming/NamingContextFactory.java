package org.femtoframework.naming;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.spi.InitialContextFactory;
import javax.naming.spi.ObjectFactory;
import java.net.URI;
import java.util.Hashtable;

/**
 * InitialContextFactory的实现
 *
 * @see InitialContextFactory
 */
public class NamingContextFactory
    implements InitialContextFactory, ObjectFactory
{
    public Context getInitialContext(Hashtable env)
        throws NamingException
    {
        URI uri = NamingUtil.getProviderURI(env);
        String baseName = null;
        if (uri != null) {
            baseName = NamingContext.parseNameForScheme(uri);
        }
        return new NamingContext(env, baseName, null);
    }

    public Object getObjectInstance(Object obj,
                                    Name name,
                                    Context nameCtx,
                                    Hashtable environment)
        throws Exception
    {
        Context ctx = getInitialContext(environment);
        Reference ref = (Reference)obj;
        return ctx.lookup((String)ref.get("URL").getContent());
    }

}
