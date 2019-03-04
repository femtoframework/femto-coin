package org.femtoframework.coin.ext;

import org.femtoframework.coin.*;
import org.femtoframework.coin.spec.NamespaceSpec;
import org.femtoframework.coin.spec.SpecConstants;
import org.femtoframework.coin.spec.element.ModelElement;
import org.femtoframework.lang.reflect.Reflection;

/**
 * Namespace ResourceFactory implementation
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public class SimpleNamespaceFactory extends BaseResourceFactory<Namespace> implements NamespaceFactory {

    private LifecycleStrategy lifecycleStrategy;

    private CoinModule coinModule;

    public SimpleNamespaceFactory(CoinModule module, LifecycleStrategy strategy) {
        super(null, CoinConstants.NAMESPACE_NAMESPACE);
        this.lifecycleStrategy = strategy;
        this.coinModule = module;
        setNamespaceFactory(this);
        createNamespace(CoinConstants.NAMESPACE_NAMESPACE);
        createNamespace(CoinConstants.NAMESPACE_COIN);
        createNamespace(CoinConstants.DEFAULT_NAMESPACE);
    }

    /**
     * Creates namespace with given name
     *
     * @param name Name
     * @param spec Namespace Spec, Nullable
     * @return Created Namespace
     */
    public Namespace createNamespace(String name, NamespaceSpec spec) {
        if (name.length() < 2) {
            throw new IllegalArgumentException("Namespace has to be more than 2 characters");
        }
        SimpleNamespace ns = null;
        if (spec == null) {
            ns = new SimpleNamespace();
        }
        else {
            String type = ModelElement.getString(spec, SpecConstants._TYPE, null);
            if (type != null) {
                ns = (SimpleNamespace)Reflection.newInstance(type);
            }
            else {
                ns = new SimpleNamespace();
            }
        }
        ns.setSpec(spec);
        ns.setCoinModule(coinModule);
        ns.setLifecycleStrategy(lifecycleStrategy);
        ns.init();

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
