package org.femtoframework.coin;

import org.femtoframework.coin.spec.NamespaceSpec;

/**
 * Namespace ResourceFactory
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public interface NamespaceFactory extends ResourceFactory<Namespace> {
    /**
     * Creates namespace with given name
     *
     * @param name Name
     * @return Created Namespace
     */
    default Namespace createNamespace(String name) {
        return createNamespace(name, null);
    }

    /**
     * Creates namespace with given name
     *
     * @param name Name
     * @param spec Namespace Spec, Nullable
     * @return Created Namespace
     */
    Namespace createNamespace(String name, NamespaceSpec spec);

    /**
     * Gets namespace with given name
     *
     * @param name Name
     * @param createAuto true, Create the namespace automatically if it does not exist
     * @return Created Namespace
     */
    Namespace getNamespace(String name, boolean createAuto);
}
