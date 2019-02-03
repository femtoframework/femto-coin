package org.femtoframework.coin.spec;

import java.util.Map;

/**
 * Element Map
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public interface MapSpec<E extends Element> extends Element, Map<String, E> {
    /**
     * Start visiting this spec
     *
     * @param visitor Visitor
     */
    default void accept(ElementVisitor visitor) {
        visitor.visitMap(this);
    }
}
