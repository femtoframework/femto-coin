package org.femtoframework.coin.exception;

/**
 * The object is in creating
 *
 * @author fengyun
 * @version 1.00 2009-2-3 13:59:58
 */
public class BeanInCreatingException extends BeanCreationException
{
    public BeanInCreatingException(String objectName, String message)
    {
        super(objectName, message);
    }

    public BeanInCreatingException(String objectName)
    {
        super(objectName, "Requested bean is currently in creating (Maybe some looping reference in configuration.)");
    }
}