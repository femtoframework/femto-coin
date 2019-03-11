package org.femtoframework.naming;

import javax.naming.CompositeName;
import javax.naming.Name;
import javax.naming.NameParser;
import javax.naming.NamingException;
import java.io.Serializable;

/**
 * 名字解析器
 *
 * @author fengyun
 * @version Feb 13, 2003 11:02:11 AM
 */
public class NamingParser
    implements NameParser, Serializable
{
    public Name parse(String name)
        throws NamingException
    {
        return new CompositeName(name);
    }
}
