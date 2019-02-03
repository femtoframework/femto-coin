package org.femtoframework.coin.spec;

import java.util.List;

/**
 * List
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public interface ListSpec<E extends Element> extends Element, List<E> {

    /**
     * Start visiting this spec
     *
     * @param visitor Visitor
     */
    default void accept(ElementVisitor visitor) {
        visitor.visitList(this);
    }
}
