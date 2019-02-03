package org.femtoframework.coin.ext;

import org.femtoframework.coin.*;
import org.femtoframework.coin.annotation.Ignore;
import org.femtoframework.coin.annotation.Property;
import org.femtoframework.coin.spec.BeanSpec;
import org.femtoframework.coin.spec.ConfigSpec;
import org.femtoframework.coin.spec.ModelSpec;
import org.femtoframework.coin.spec.SpecFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Simple Namespace
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public class SimpleNamespace implements Namespace {

    private String name;

    @Ignore
    private Map<Class<?>, SpecFactory> specFactories = new HashMap<>(4);

    private SimpleComponentFactory componentFactory;

    @Ignore
    private BeanFactory beanFactory;

    public SimpleNamespace(String name, CoinModule module, LifecycleStrategy strategy) {
        this.name = name;
        NamespaceFactory namespaceFactory = module.getNamespaceFactory();

        SimpleSpecFactory<BeanSpec> beanSpecFactory = new SimpleSpecFactory<>(namespaceFactory, name);
        SimpleSpecFactory<ConfigSpec> configSpecFactory = new SimpleSpecFactory<>(namespaceFactory, name);
        specFactories.put(BeanSpec.class, beanSpecFactory);
        specFactories.put(ConfigSpec.class, configSpecFactory);

        this.componentFactory = new SimpleComponentFactory(module, beanSpecFactory, strategy);
        this.beanFactory = new SimpleBeanFactory(namespaceFactory, componentFactory);
    }

    /**
     * Namespace
     *
     * @return Namespace's name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Component Factory under this namespace
     *
     * @return Component Factory
     */
    @Override
    @Property(writable = false)
    public ComponentFactory getComponentFactory() {
        return componentFactory;
    }

    /**
     * Bean Factory under this namespace;
     *
     * @return BeanFactory
     */
    @Override
    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    @Override
    public <S extends ModelSpec> SpecFactory<S> getSpecFactory(Class<S> specClass) {
        return specFactories.get(specClass);
    }

}
