package org.femtoframework.naming;

import javax.naming.*;
import java.util.Collection;

/**
 * 命名服务
 *
 * @author fengyun
 * @version Feb 13, 2003 10:56:01 AM
 */
public interface NamingService
{
    /**
     * 用给定的名字邦定对应的对象
     *
     * @param name      名字
     * @param obj       对象（对象和类名任选其一）
     * @param className 类名
     * @throws NamingException 命名异常
     */
    void bind(Name name, Object obj, String className) throws NamingException;

    /**
     * 用给定的名字重新邦定对应的对象
     *
     * @param name      名字
     * @param obj       对象（对象和类名任选其一）
     * @param className 类名
     * @throws NamingException 命名异常
     */
    void rebind(Name name, Object obj, String className) throws NamingException;

    /**
     * 取消邦定给定的名字
     *
     * @param name 名字
     * @throws NamingException 命名异常
     */
    void unbind(Name name) throws NamingException;

    /**
     * 查找给定名字的对象
     *
     * @param name 名字
     * @return
     * @throws NamingException 命名异常
     */
    Object lookup(Name name) throws NamingException;

    /**
     * 列出符合给定名字的所有对象
     *
     * @param name 名字
     * @return
     * @throws NamingException 命名异常
     */
    Collection<NameClassPair> list(Name name) throws NamingException;

    /**
     * 列出符合给定名字的所有邦定对象
     *
     * @param name 名字
     * @return
     * @throws NamingException 命名异常
     */
    Collection<Binding> listBindings(Name name) throws NamingException;

    /**
     * 创建子上下文
     *
     * @param name 名字
     * @return
     * @throws NamingException 命名异常
     */
    Context createSubcontext(Name name) throws NamingException;
}
