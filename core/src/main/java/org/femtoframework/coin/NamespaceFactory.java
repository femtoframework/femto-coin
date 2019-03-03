package org.femtoframework.coin;

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
        return createNamespace(name, NamespaceAccess.LOCAL);
    }

    /**
     * Creates namespace with given name
     *
     * @param name Name
     * @return Created Namespace
     */
    Namespace createNamespace(String name, NamespaceAccess access);

    /**
     * Gets namespace with given name
     *
     * @param name Name
     * @param createAuto true, Create the namespace automatically if it does not exist
     * @return Created Namespace
     */
    Namespace getNamespace(String name, boolean createAuto);
}
