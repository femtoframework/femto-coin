package org.femtoframework.naming;

import org.femtoframework.implement.ImplementUtil;

import javax.naming.Context;
import javax.naming.NamingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Hashtable;

/**
 * 命名服务工具类
 *
 * @author fengyun
 * @version 1.00 2005-5-21 1:43:00
 */
public class NamingUtil
{
    private NamingUtil()
    {
    }

    private static NamingModule module;

    /**
     * 返回模块
     *
     * @return 模块
     */
    public static NamingModule getModule()
    {
        if (module == null) {
            module = ImplementUtil.getInstance(NamingModule.class);
        }
        return module;
    }

    /**
     * 返回本地的命名服务
     *
     * @return 本地命名服务
     */
    public static NamingService getLocalService()
    {
        return getModule().getLocalService();
    }

    /**
     * 返回本地的该Scheme的命名服务
     *
     * @param scheme Scheme
     * @return 本地该Scheme的命名服务
     */
    public static NamingService getLocalService(String scheme)
    {
        return getModule().getServiceFactory(scheme).getLocalService();
    }

    /**
     * 根据scheme返回命名服务工厂
     *
     * @return Scheme "coin" "aps"等
     */
    public static NamingServiceFactory getServiceFactory(String scheme)
    {
        return getModule().getServiceFactory(scheme);
    }

    /**
     * 根据主机名和端口返回名字服务
     *
     * @param host 主机
     * @param port 端口
     * @return 名字服务
     * @throws NamingException 如果名字服务不存在或者连接异常等
     */
    public static NamingService createService(String scheme, String host, int port)
        throws NamingException
    {
        return getModule().createService(scheme, host, port);
    }

    /**
     * 根据主机名和端口返回名字服务
     *
     * @param uri 名字服务统一资源定位
     * @return 名字服务
     * @throws NamingException 如果名字服务不存在或者连接异常等
     */
    public static NamingService createService(String uri)
        throws NamingException
    {
        return getModule().createService(uri);
    }

    /**
     * 根据主机名和端口返回名字服务
     *
     * @param uri 名字服务统一资源定位
     * @return 名字服务
     * @throws NamingException 如果名字服务不存在或者连接异常等
     */
    public static NamingService createService(URI uri)
        throws NamingException
    {
        return getModule().createService(uri);
    }

    /**
     * 从env中获取ProviderURL
     *
     * @param env 环境变量
     * @return URI
     */
    public static URI getProviderURI(Hashtable env)
        throws NamingException
    {
        URI uri = (URI) env.get(NamingConstants.PROVIDER_URI);
        if (uri == null) {
            String providerURL = (String) env.get(Context.PROVIDER_URL);
            if (providerURL != null) {
                try {
                    uri = new URI(providerURL);
                    env.put(NamingConstants.PROVIDER_URI, uri);
                }
                catch (URISyntaxException e) {
                    throw new NamingException("Invalid provider url:" + providerURL);
                }
            }
        }
        return uri;
    }
}
