package org.femtoframework.coin.spec;

/**
 * PrimitiveSpec
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public interface PrimitiveSpec<E> extends Element {


    E getPrimitiveValue();

    /**
     * Start visiting this spec
     *
     * @param visitor Visitor
     */
    default void accept(ElementVisitor visitor) {
        visitor.visitPrimitive(this);
    }
}
