package org.femtoframework.naming;

import javax.naming.NamingException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * 名称服务工厂（对应一类scheme的服务）
 *
 * @author fengyun
 * @version 1.00 2005-6-3 14:51:34
 */
public interface NamingServiceFactory
{
    /**
     * 返回本地的该Scheme的命名服务
     *
     * @return 本地该Scheme的命名服务
     */
    NamingService getLocalService();

    /**
     * 根据主机名和端口创建名字服务
     *
     * @param host 主机
     * @param port 端口
     * @return 名字服务
     * @throws NamingException 如果名字服务不存在或者连接异常等
     */
    NamingService createService(String host, int port) throws NamingException;

    /**
     * 根据主机名和端口创建名字服务
     *
     * @param uri 名字服务统一资源定位
     * @return 名字服务
     * @throws NamingException 如果名字服务不存在或者连接异常等
     */
    NamingService createService(URI uri) throws NamingException;

    /**
     * 根据主机名和端口创建名字服务
     *
     * @param uri 名字服务统一资源定位
     * @return 名字服务
     * @throws NamingException 如果名字服务不存在或者连接异常等
     */
    default NamingService createService(String uri) throws NamingException {
        try {
            return createService(new URI(uri));
        }
        catch (URISyntaxException e) {
            throw new NamingException("Invalid uri:" + uri);
        }
    }
}
