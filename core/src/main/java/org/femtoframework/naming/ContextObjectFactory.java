package org.femtoframework.naming;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;
import javax.naming.spi.ObjectFactory;
import java.util.Hashtable;

/**
 * 采用URL和NamingContext框架的名字服务对象工厂的基础实现
 *
 * @author fengyun
 * @version 1.00 2005-5-21 1:46:53
 */
public class ContextObjectFactory
    implements ObjectFactory, InitialContextFactory
{
    public Object getObjectInstance(Object obj,
                                    Name name,
                                    Context nameCtx,
                                    Hashtable env)
        throws Exception
    {
        if (obj == null) {
            return createNamingContext(env, name, null);
        }
        else if (obj instanceof String) {
            String url = (String)obj;
            Context ctx = createNamingContext(env, name, null);

            Name n = ctx.getNameParser(name).parse(url.substring(url.indexOf(":") + 1));
            if (n.size() >= 3) {
                // Provider URL?
                if (n.get(0).toString().equals("") &&
                    n.get(1).toString().equals("")) {
                    ctx.addToEnvironment(Context.PROVIDER_URL, n.get(2));
                }
            }
            return ctx;
        }
        else {
            return null;
        }
    }

    protected Context createNamingContext(Hashtable env, Name name, NamingService service)
        throws NamingException
    {
        return new NamingContext(env, name, null);
    }

    /**
     * Creates an Initial Context for beginning name resolution.
     * Special requirements of this context are supplied
     * using <code>environment</code>.
     * <p/>
     * The environment parameter is owned by the caller.
     * The implementation will not modify the object or keep a reference
     * to it, although it may keep a reference to a clone or copy.
     *
     * @param env The possibly null environment
     *            specifying information to be used in the creation
     *            of the initial context.
     * @return A non-null initial context object that implements the Context
     *         interface.
     * @throws NamingException If cannot create an initial context.
     */
    public Context getInitialContext(Hashtable env) throws NamingException
    {
        return createNamingContext(env, null, null);
    }
}
