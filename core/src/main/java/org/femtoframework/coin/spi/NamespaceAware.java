package org.femtoframework.coin.spi;

import org.femtoframework.coin.Namespace;

/**
 * Namespace Aware. Allow bean to get current namespace
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public interface NamespaceAware {

    /**
     * Current Namespace
     *
     * @param namespace Current Namespace
     */
    void setNamespace(Namespace namespace);
}
