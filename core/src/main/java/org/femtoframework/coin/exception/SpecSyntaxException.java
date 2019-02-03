package org.femtoframework.coin.exception;

import org.femtoframework.coin.CoinException;

/**
 * Spec Syntax Exception
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public class SpecSyntaxException extends CoinException {

    public SpecSyntaxException(String message)
    {
        super(message);
    }

    public SpecSyntaxException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
