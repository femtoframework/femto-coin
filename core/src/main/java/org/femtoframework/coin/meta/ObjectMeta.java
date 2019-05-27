package org.femtoframework.coin.meta;

import org.femtoframework.bean.NamedBean;
import org.femtoframework.parameters.Parameters;

public interface ObjectMeta extends NamedBean {

    /**
     * Return generated name
     *
     * @return generated name
     */
    default String getGenerateName() {
        return getName();
    }

    /**
     * Return namespace of current object
     *
     * @return Namespace
     */
    String getNamespace();

    /**
     * System generated UID
     *
     * @return UID
     */
    String getUid();

    /**
     * Creation timestamp
     *
     * @return Creation Timestamp
     */
    long getCreationTimestamp();

    /**
     * Labels
     *
     * @return Labels
     */
    Parameters<String> getLabels();

    /**
     * Annotations
     *
     * @return Annotations
     */
    Parameters<String> getAnnotations();
}
