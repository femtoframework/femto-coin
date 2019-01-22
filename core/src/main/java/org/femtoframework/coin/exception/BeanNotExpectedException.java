package org.femtoframework.coin.exception;


/**
 * Bean doesn't have expected kind
 *
 * @author fengyun
 * @version 1.00 2009-2-3 14:28:22
 */
public class BeanNotExpectedException extends BeanCreationException
{
    public BeanNotExpectedException(String objectName, Class objectType, Class expectedType)
    {
        super(objectName, "The bean kind is " + objectType.getName()
                          + " but expected kind is " + expectedType.getName());
    }

    public BeanNotExpectedException(String expectedName, String declaredName)
    {
        super("The expected name is " + expectedName + ", but declared name is " + declaredName);
    }

    public BeanNotExpectedException(String msg)
    {
        super(msg);
    }
}
