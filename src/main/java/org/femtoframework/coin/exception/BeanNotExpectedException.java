package org.femtoframework.coin.exception;


/**
 * Bean doesn't have expected type
 *
 * @author fengyun
 * @version 1.00 2009-2-3 14:28:22
 */
public class BeanNotExpectedException extends BeanCreationException
{
    public BeanNotExpectedException(String objectName, Class objectType, Class expectedType)
    {
        super(objectName, "The bean type is " + objectType.getName()
                          + " but expected type is " + expectedType.getName());
    }
}
