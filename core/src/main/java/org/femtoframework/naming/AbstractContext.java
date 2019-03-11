package org.femtoframework.naming;

import javax.naming.*;
import java.io.Serializable;
import java.util.Hashtable;

/**
 * 抽象上下文
 *
 * @author fengyun
 * @version 1.00 2004-11-22 20:40:45
 */
public abstract class AbstractContext
    implements Context, Serializable
{
    protected Hashtable env;
    protected Name prefix;

    protected NameParser parser;

    /**
     * 重载命名解析器
     *
     * @return 重载命名解析器
     */
    protected NameParser createParser()
    {
        return new NamingParser();
    }

    public AbstractContext(Hashtable e, Name baseName)
        throws NamingException
    {
        parser = createParser();
        if (baseName == null) {
            this.prefix = parser.parse("");
        }
        else {
            this.prefix = baseName;
        }

        if (e != null) {
            this.env = (Hashtable)e.clone();
        }
        else {
            this.env = new Hashtable();
        }
    }

    public AbstractContext(Hashtable e, String baseName)
        throws NamingException
    {
        parser = createParser();
        if (baseName == null) {
            baseName = "";
        }
        this.prefix = parser.parse(baseName);

        if (e != null) {
            this.env = (Hashtable)e.clone();
        }
        else {
            this.env = new Hashtable();
        }
    }

    public void rebind(String name, Object obj)
        throws NamingException
    {
        rebind(getNameParser(name).parse(name), obj);
    }

    public void bind(String name, Object obj)
        throws NamingException
    {
        bind(getNameParser(name).parse(name), obj);
    }

    public Object lookup(String name)
        throws NamingException
    {
        return lookup(getNameParser(name).parse(name));
    }

    public void unbind(String name)
        throws NamingException
    {
        unbind(getNameParser(name).parse(name));
    }

    public void rename(String oldname, String newname)
        throws NamingException
    {
        rename(getNameParser(oldname).parse(oldname),
            getNameParser(newname).parse(newname));
    }

    public void rename(Name oldName, Name newName)
        throws NamingException
    {
        bind(newName, lookup(oldName));
        unbind(oldName);
    }

    public NamingEnumeration list(String name)
        throws NamingException
    {
        return list(getNameParser(name).parse(name));
    }

    public NamingEnumeration listBindings(String name)
        throws NamingException
    {
        return listBindings(getNameParser(name).parse(name));
    }

    public String composeName(String name, String prefix)
        throws NamingException
    {
        Name result = composeName(parser.parse(name),
            parser.parse(prefix));
        return result.toString();
    }

    public Name composeName(Name name, Name prefix)
        throws NamingException
    {
        if (prefix == null || prefix.size() == 0) {
            return name;
        }

        Name result = (Name)(prefix.clone());
        if (result.getClass().isAssignableFrom(name.getClass())) {
            result.addAll(name);
        }
        else {
            for(int i = 0, s = name.size(); i < s; i ++) {
                result.add(name.get(i));
            }
        }
        return result;
    }

    public NameParser getNameParser(String name)
        throws NamingException
    {
        return parser;
    }

    public NameParser getNameParser(Name name)
        throws NamingException
    {
        return getNameParser(name.toString());
    }

    public Context createSubcontext(String name)
        throws NamingException
    {
        return createSubcontext(getNameParser(name).parse(name));
    }

    public Object addToEnvironment(String propName, Object propVal)
        throws NamingException
    {
        Object old = env.get(propName);
        env.put(propName, propVal);
        return old;
    }

    public Object removeFromEnvironment(String propName)
        throws NamingException
    {
        return env.remove(propName);
    }

    public Hashtable getEnvironment()
        throws NamingException
    {
        return env;
    }

    public void close() throws NamingException
    {
        env = null;
    }

    public String getNameInNamespace()
        throws NamingException
    {
        return prefix.toString();
    }

    public void destroySubcontext(String name)
        throws NamingException
    {
        throw new OperationNotSupportedException();
    }

    public void destroySubcontext(Name name)
        throws NamingException
    {
        throw new OperationNotSupportedException();
    }

    public Object lookupLink(String name)
        throws NamingException
    {
        return lookupLink(getNameParser(name).parse(name));
    }
}
