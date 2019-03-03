package org.femtoframework.coin;

/**
 * Namespace Access define different strategy for accessing from outside namespace or JVM
 *
 */
public enum NamespaceAccess {

    /**
     * Access only from namespace internally.
     */
    PRIVATE,

    /**
     * Allow accessing from JVM, default value.
     */
    LOCAL,

    /**
     * Allow accessing from Cluster.
     */
    PUBLIC
}
