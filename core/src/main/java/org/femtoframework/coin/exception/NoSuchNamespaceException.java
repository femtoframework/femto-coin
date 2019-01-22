package org.femtoframework.coin.exception;


import org.femtoframework.coin.CoinException;

/**
 * No such namespace
 *
 * @author fengyun
 * @version 1.00 2009-2-3 14:26:31
 */
public class NoSuchNamespaceException extends CoinException
{
    public NoSuchNamespaceException(String namespace)
    {
        super("No such namespace:" + namespace);
    }
}
