package org.femtoframework.naming;


import org.femtoframework.implement.ImplementUtil;

import javax.naming.Context;
import javax.naming.NamingException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * 简单命名模块实现
 *
 * @author fengyun
 * @version 1.00 2005-6-3 15:15:25
 */
public class SimpleNamingModule implements NamingModule
{
    private NamingService localService;

    private static final String DEFAULT_SCHEME = "coin";

    /**
     * 返回本地的命名服务
     *
     * @return 本地命名服务
     */
    public NamingService getLocalService()
    {
        if (localService == null) {
            String uri = System.getProperty(Context.PROVIDER_URL);
            if (uri != null) {
                try {
                    localService = createService(uri);
                }
                catch (NamingException e) {
                    throw new IllegalStateException("Naming exception:" + e.getMessage());
                }
            }
            else {
                localService = getServiceFactory(DEFAULT_SCHEME).getLocalService();
            }
        }
        return localService;
    }

    /**
     * 根据scheme返回命名服务工厂
     *
     * @return Scheme "coin" "apsis"等
     */
    public NamingServiceFactory getServiceFactory(String scheme)
    {
        return ImplementUtil.getInstance(scheme, NamingServiceFactory.class);
    }

    /**
     * 根据主机名和端口返回名字服务
     *
     * @param host 主机
     * @param port 端口
     * @return 名字服务
     * @throws NamingException 如果名字服务不存在或者连接异常等
     */
    public NamingService createService(String scheme, String host, int port) throws NamingException
    {
        NamingServiceFactory factory = getServiceFactory(scheme);
        return factory != null ? factory.createService(host, port) : null;
    }

    /**
     * 根据主机名和端口返回名字服务
     *
     * @param uri 名字服务统一资源定位
     * @return 名字服务
     * @throws NamingException 如果名字服务不存在或者连接异常等
     */
    public NamingService createService(String uri) throws NamingException
    {
        try {
            return createService(new URI(uri));
        }
        catch (URISyntaxException e) {
            throw new NamingException("Invalid uri:" + uri);
        }
    }

    /**
     * 根据主机名和端口返回名字服务
     *
     * @param uri 名字服务统一资源定位
     * @return 名字服务
     * @throws NamingException 如果名字服务不存在或者连接异常等
     */
    public NamingService createService(URI uri) throws NamingException
    {
        NamingServiceFactory factory = getServiceFactory(uri.getScheme());
        return factory != null ? factory.createService(uri) : null;
    }
}
