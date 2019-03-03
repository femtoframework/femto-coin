package org.femtoframework.coin.ext;

import org.femtoframework.coin.*;

/**
 * Namespace ResourceFactory implementation
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public class SimpleNamespaceFactory extends BaseResourceFactory<Namespace> implements NamespaceFactory {

    private LifecycleStrategy strategy;

    private CoinModule module;

    public SimpleNamespaceFactory(CoinModule module, LifecycleStrategy strategy) {
        super(null, CoinConstants.NAMESPACE_NAMESPACE);
        this.strategy = strategy;
        this.module = module;
        setNamespaceFactory(this);
        createNamespace(CoinConstants.NAMESPACE_NAMESPACE);
        createNamespace(CoinConstants.NAMESPACE_COIN);
        createNamespace(CoinConstants.DEFAULT_NAMESPACE);
    }

    /**
     * Creates namespace with given name
     *
     * @param name Name
     * @return Created Namespace
     */
    public Namespace createNamespace(String name, NamespaceAccess access) {
        if (name.length() < 2) {
            throw new IllegalArgumentException("Namespace has to be more than 2 characters");
        }
        SimpleNamespace ns = new SimpleNamespace(name, module, strategy, access);
        add(ns);
        return ns;
    }

    /**
     * Get namespace with given name
     *
     * @param name       Name
     * @param createAuto true, Create the namespace automatically if it does not exist
     * @return
     */
    @Override
    public Namespace getNamespace(String name, boolean createAuto) {
        Namespace ns = get(name);
        if (ns == null && createAuto) {
            ns = createNamespace(name);
        }
        return ns;
    }
}
