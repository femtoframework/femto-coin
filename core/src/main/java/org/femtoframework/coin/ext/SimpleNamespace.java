package org.femtoframework.coin.ext;

import org.femtoframework.bean.InitializableMBean;
import org.femtoframework.coin.*;
import org.femtoframework.bean.annotation.Coined;
import org.femtoframework.bean.annotation.Ignore;
import org.femtoframework.bean.annotation.Property;
import org.femtoframework.coin.spec.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Simple Namespace
 *
 * @author Sheldon Shao
 * @version 1.0
 */
@Coined
public class SimpleNamespace implements Namespace, InitializableMBean {

    private String name;

    private NamespaceAccess access;

    @Ignore
    private Map<Class<?>, SpecFactory> specFactories = new HashMap<>(4);

    private SimpleComponentFactory componentFactory;

    @Ignore
    private BeanFactory beanFactory;

    private CoinModule coinModule;

    private LifecycleStrategy lifecycleStrategy;

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
     * Component ResourceFactory under this namespace
     *
     * @return Component ResourceFactory
     */
    @Override
    @Property(writable = false)
    public ComponentFactory getComponentFactory() {
        return componentFactory;
    }

    /**
     * Bean ResourceFactory under this namespace;
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

    @Override
    public NamespaceAccess getAccess() {
        return access;
    }

    private boolean initialized = false;

    /**
     * Return whether it is initialized
     *
     * @return whether it is initialized
     */
    @Override
    public boolean isInitialized() {
        return initialized;
    }

    /**
     * Initialized setter for internal
     *
     * @param initialized BeanPhase
     */
    @Override
    public void _doSetInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    /**
     * Initiliaze internally
     */
    @Override
    public void _doInit() {
        NamespaceFactory namespaceFactory = coinModule.getNamespaceFactory();

        SimpleSpecFactory<BeanSpec> beanSpecFactory = new SimpleSpecFactory<>(namespaceFactory, name);
        SimpleSpecFactory<ConfigSpec> configSpecFactory = new SimpleSpecFactory<>(namespaceFactory, name);
        specFactories.put(BeanSpec.class, beanSpecFactory);
        specFactories.put(ConfigSpec.class, configSpecFactory);

        this.componentFactory = new SimpleComponentFactory(coinModule, beanSpecFactory, lifecycleStrategy);
        this.beanFactory = new SimpleBeanFactory(namespaceFactory, componentFactory);
    }

    protected void setAccess(NamespaceAccess access) {
        this.access = access;
    }

    protected void setCoinModule(CoinModule coinModule) {
        this.coinModule = coinModule;
    }

    protected void setLifecycleStrategy(LifecycleStrategy lifecycleStrategy) {
        this.lifecycleStrategy = lifecycleStrategy;
    }

    protected void setName(String name) {
        this.name = name;
    }

    protected void setSpec(NamespaceSpec spec) {
        if (spec != null) {
            setName(spec.getName());
            setAccess(spec.getAccess());
        }
    }
}
