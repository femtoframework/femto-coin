package org.femtoframework.coin.exception;


import org.femtoframework.coin.CoinException;

/**
 * RuntimeException wrapper for java.lang.IllegalAccessException, ClassNotFoundException,
 * NoSuchMethodException, InstantiationException etc.
 *
 * @author fengyun
 * @version 1.00 2009-1-27 14:16:32
 */
public class BeanCreationException extends CoinException
{
    public BeanCreationException(String message)
    {
        super(message);
    }

    public BeanCreationException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public BeanCreationException(String objectName, String message)
    {
        this(objectName, message, (Throwable)null);
    }

    public BeanCreationException(String objectName, String message, Throwable cause)
    {
        super("Exception when creating bean with name '" + objectName + "': " + message, cause);
    }
}
