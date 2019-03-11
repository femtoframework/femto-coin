package org.femtoframework.naming;

import javax.naming.CommunicationException;

/**
 * 名字服务通讯异常
 *
 * @author fengyun
 * @version Feb 13, 2003 11:42:38 AM
 */
public class CommException
    extends CommunicationException
{
    /**
     * Constructs a new instance of CommunicationException using the
     * arguments supplied.
     *
     * @param explanation Additional detail about this exception.
     * @see Throwable#getMessage
     */
    public CommException(String explanation)
    {
        super(explanation);
    }

    /**
     * Constructs a new instance of CommunicationException.
     */
    public CommException()
    {
        super();
    }

    /**
     * Constructs a new instance of CommunicationException using the
     * arguments supplied.
     *
     * @param cause       The cause exeception
     * @param explanation Additional detail about this exception.
     * @see Throwable#getMessage
     */
    public CommException(String explanation, Throwable cause)
    {
        super(explanation);
        setRootCause(cause);
    }

    /**
     * Constructs a new instance of CommunicationException using the
     * arguments supplied.
     *
     * @see Throwable#getMessage
     */
    public CommException(Throwable cause)
    {
        super(cause.getMessage());
        setRootCause(cause);
    }

}
