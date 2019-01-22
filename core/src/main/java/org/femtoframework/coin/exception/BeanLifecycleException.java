package org.femtoframework.coin.exception;


import org.femtoframework.coin.CoinException;

/**
 * Bean Lifecycle Exception
 *
 * @author fengyun
 * @version 1.00 2009-2-4 12:56:36
 */
public class BeanLifecycleException extends CoinException
{
    public BeanLifecycleException(String message)
    {
        super(message);
    }

    public BeanLifecycleException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
