package org.femtoframework.coin.spec;

import java.util.List;

/**
 * Remote Spec
 */
public interface RemoteSpec extends BeanSpec {

    /**
     * Interfaces for generating this remote reference
     *
     * @return Interfaces
     */
    String[] getInterfaces();

    /**
     * URI of this remote reference
     *
     * @return URI of this reference
     */
    String getUri();

    /**
     * Start visiting this spec
     *
     * @param visitor Visitor
     */
    default void accept(ElementVisitor visitor) {
        visitor.visitRemote(this);
    }
}
