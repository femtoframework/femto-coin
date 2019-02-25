package org.femtoframework.coin.spec;

/**
 * Kind
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public interface Kind {

    String name();

    int ordinal();

    default boolean isPrimitive() {
        return false;
    }
}
