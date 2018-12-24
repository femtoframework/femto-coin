package org.femtoframework.coin.exception;


import org.femtoframework.coin.CoinException;

/**
 * No such class exception
 *
 * @author fengyun
 * @version 1.00 2009-2-3 22:20:08
 */
public class NoSuchClassException extends CoinException
{

    public NoSuchClassException(String message)
    {
        super(message);
    }

    public NoSuchClassException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
