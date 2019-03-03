package org.femtoframework.coin.spec;

import org.femtoframework.coin.NamespaceAccess;

/**
 * Namespace Spec
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public interface NamespaceSpec extends ModelSpec {

    /**
     * Start visiting this spec
     *
     * @param visitor Visitor
     */
    default void accept(ElementVisitor visitor) {
        visitor.visitNamespace(this);
    }

    /**
     * Namespace Access define different strategy for accessing from outside namespace or JVM
     *
     * @return Access Information
     */
    NamespaceAccess getAccess();
}
