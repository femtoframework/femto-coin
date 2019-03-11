package org.femtoframework.naming;

import javax.naming.NamingException;
import java.net.URI;

/**
 * 命名服务模块
 *
 * @author fengyun
 * @version Feb 25, 2003 3:30:48 PM
 */
public interface NamingModule
{
    /**
     * 返回本地的命名服务
     *
     * @return 本地命名服务
     */
    NamingService getLocalService();

    /**
     * 根据scheme返回命名服务工厂
     *
     * @return Scheme "coin" "aps"等
     */
    NamingServiceFactory getServiceFactory(String scheme);

    /**
     * 根据主机名和端口返回名字服务
     *
     * @param host 主机
     * @param port 端口
     * @return 名字服务
     * @throws NamingException 如果名字服务不存在或者连接异常等
     */
    NamingService createService(String scheme, String host, int port) throws NamingException;

    /**
     * 根据主机名和端口返回名字服务
     *
     * @param uri 名字服务统一资源定位
     * @return 名字服务
     * @throws NamingException 如果名字服务不存在或者连接异常等
     */
    NamingService createService(String uri) throws NamingException;

    /**
     * 根据主机名和端口返回名字服务
     *
     * @param uri 名字服务统一资源定位
     * @return 名字服务
     * @throws NamingException 如果名字服务不存在或者连接异常等
     */
    NamingService createService(URI uri) throws NamingException;
}
