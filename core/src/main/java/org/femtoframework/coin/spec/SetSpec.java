package org.femtoframework.coin.spec;

import java.util.Set;

/**
 * Set Spec
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public interface SetSpec <E extends Element> extends Element, Set<E> {

    /**
     * Start visiting this spec
     *
     * @param visitor Visitor
     */
    default void accept(ElementVisitor visitor) {
        visitor.visitSet(this);
    }
}
