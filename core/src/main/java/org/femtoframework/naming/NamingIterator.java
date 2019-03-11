package org.femtoframework.naming;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import java.util.*;

/**
 * 名字枚举
 *
 * @author fengyun
 * @version Feb 13, 2003 11:20:53 AM
 */

public class NamingIterator<T>
    implements NamingEnumeration<T>
{

    private Enumeration<T> en;

    /**
     * 构造
     *
     * @param en 枚举
     */
    public NamingIterator(Enumeration<T> en)
    {
        this.en = en;
    }

    /**
     * 构造
     *
     * @param list 列表
     */
    public NamingIterator(Vector<T> list)
    {
        this(list.elements());
    }

    /**
     * 构造
     *
     * @param list 列表
     */
    public NamingIterator(Collection<T> list)
    {
        this(Collections.enumeration(list));
    }

    /**
     * Tests if this enumeration contains more elements.
     *
     * @return <code>true</code> if and only if this enumeration object
     *         contains at least one more element to provide;
     *         <code>false</code> otherwise.
     */
    public boolean hasMoreElements()
    {
        return en.hasMoreElements();
    }

    /**
     * Returns the next element of this enumeration if this enumeration
     * object has at least one more element to provide.
     *
     * @return the next element of this enumeration.
     * @throws java.util.NoSuchElementException
     *          if no more elements exist.
     */
    public T nextElement()
    {
        return en.nextElement();
    }

    /**
     * Retrieves the next element in the enumeration.
     * This method allows naming exceptions encountered while
     * retrieving the next element to be caught and handled
     * by the application.
     * <p/>
     * Note that <tt>next()</tt> can also throw the runtime exception
     * NoSuchElementException to indicate that the caller is
     * attempting to enumerate beyond the end of the enumeration.
     * This is different from a NamingException, which indicates
     * that there was a problem in obtaining the next element,
     * for example, due to a referral or server unavailability, etc.
     *
     * @return The possibly null element in the enumeration.
     *         null is only valid for enumerations that can return
     *         null (e.g. Attribute.getAll() returns an enumeration of
     *         attribute values, and an attribute value can be null).
     * @throws NamingException If a naming exception is encountered while attempting
     *                         to retrieve the next element. See NamingException
     *                         and its subclasses for the possible naming exceptions.
     * @throws java.util.NoSuchElementException
     *                         If attempting to get the next element when none is available.
     * @see Enumeration#nextElement
     */
    public T next() throws NamingException
    {
        return nextElement();
    }

    /**
     * Determines whether there are any more elements in the enumeration.
     * This method allows naming exceptions encountered while
     * determining whether there are more elements to be caught and handled
     * by the application.
     *
     * @return true if there is more in the enumeration ; false otherwise.
     * @throws NamingException If a naming exception is encountered while attempting
     *                         to determine whether there is another element
     *                         in the enumeration. See NamingException
     *                         and its subclasses for the possible naming exceptions.
     * @see Enumeration#hasMoreElements
     */
    public boolean hasMore() throws NamingException
    {
        return hasMoreElements();
    }

    /**
     * Closes this enumeration.
     * <p/>
     * After this method has been invoked on this enumeration, the
     * enumeration becomes invalid and subsequent invocation of any of
     * its methods will yield undefined results.
     * This method is intended for aborting an enumeration to free up resources.
     * If an enumeration proceeds to the end--that is, until
     * <tt>hasMoreElements()</tt> or <tt>hasMore()</tt> returns <tt>false</tt>--
     * resources will be freed up automatically and there is no need to
     * explicitly call <tt>close()</tt>.
     * <p/>
     * This method indicates to the service provider that it is free
     * to release resources associated with the enumeration, and can
     * notify servers to cancel any outstanding requests. The <tt>close()</tt>
     * method is a hint to implementations for managing their resources.
     * Implementations are encouraged to use appropriate algorithms to
     * manage their resources when client omits the <tt>close()</tt> calls.
     *
     * @throws NamingException If a naming exception is encountered
     *                         while closing the enumeration.
     * @since 1.3
     */
    public void close() throws NamingException
    {
    }
}
