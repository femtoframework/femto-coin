package org.femtoframework.coin.meta;

import org.femtoframework.bean.NamedBean;

public interface ObjectMeta extends NamedBean {

    /**
     * Return generated name
     *
     * @return generated name
     */
    String getGenerateName();

    /**
     * Return namespace of current object
     *
     * @return Namespace
     */
    String getNamespace();

    /**
     * Creation timestamp
     *
     * @return Creation Timestamp
     */
    long getCreationTimestamp();
}
