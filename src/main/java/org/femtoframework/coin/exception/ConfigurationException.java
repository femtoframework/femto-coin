package org.femtoframework.coin.exception;


/**
 * Configuration Exception
 *
 * @author fengyun
 * @version 1.00 2009-2-4 12:21:08
 */
public class ConfigurationException
    extends BeanLifecycleException
{
    public ConfigurationException(String message)
    {
        super(message);
    }

    public ConfigurationException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
