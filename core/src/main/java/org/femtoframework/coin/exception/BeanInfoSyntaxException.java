package org.femtoframework.coin.exception;

import org.femtoframework.coin.CoinException;

/**
 * Bean Info Syntax Exception
 *
 * @author Sheldon Shao
 * @version 1.0.0
 */
public class BeanInfoSyntaxException extends CoinException {

    public BeanInfoSyntaxException(String message)
    {
        super(message);
    }

    public BeanInfoSyntaxException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
