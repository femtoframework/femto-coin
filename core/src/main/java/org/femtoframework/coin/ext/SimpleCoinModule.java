package org.femtoframework.coin.ext;

import org.femtoframework.bean.AbstractLifecycle;
import org.femtoframework.bean.BeanStage;
import org.femtoframework.bean.info.BeanInfoUtil;
import org.femtoframework.coin.*;
import org.femtoframework.coin.event.BeanEventListeners;
import org.femtoframework.bean.info.BeanInfoFactory;
import org.femtoframework.coin.remote.RemoteGenerator;
import org.femtoframework.coin.spec.KindSpecFactory;
import org.femtoframework.coin.spec.VariableResolverFactory;
import org.femtoframework.coin.spec.ext.SimpleKindSpecFactory;
import org.femtoframework.coin.spec.ext.SimpleVariableResolverFactory;
import org.femtoframework.implement.ImplementUtil;

import static org.femtoframework.coin.CoinConstants.*;

/**
 * Simple Coin Module
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public class SimpleCoinModule extends AbstractLifecycle implements CoinModule {

//    private DefaultComponentFactory defaultComponentFactory = new SimpleDefaultComponentFactory();

    private SimpleKindSpecFactory kindSpecFactory = new SimpleKindSpecFactory();

    private SimpleCoinController coinController = new SimpleCoinController();

    private SimpleConfiguratorFactory configuratorFactory = new SimpleConfiguratorFactory();


    private SimpleLifecycleStrategy lifecycleStrategy = new SimpleLifecycleStrategy(configuratorFactory);


    private SimpleNamespaceFactory namespaceFactory = new SimpleNamespaceFactory(this, lifecycleStrategy);


    {
        coinController.setKindSpecFactory(kindSpecFactory);
        coinController.setNamespaceFactory(namespaceFactory);

        configuratorFactory.setNamespaceFactory(namespaceFactory);
        kindSpecFactory.setNamespaceFactory(namespaceFactory);
    }

    private Namespace namespaceCoin;

    private CoinLookup lookup = new SimpleCoinLookup(namespaceFactory);

    private RemoteGenerator remoteGenerator;

    private VariableResolverFactory variableResolverFactory = new SimpleVariableResolverFactory(namespaceFactory);


    /**
     * Bean Info ResourceFactory
     */
    private BeanInfoFactory beanInfoFactory = BeanInfoUtil.getBeanInfoFactory();


    private SimpleCoinReloader reloader;

    /**
     * Return namespace factory
     *
     * @return Namespace ResourceFactory
     *
     */
    @Override
    public NamespaceFactory getNamespaceFactory() {
        return namespaceFactory;
    }

//    /**
//     * Return default component factory
//     *
//     * @return Default Component Factory
//     */
//    @Override
//    public DefaultComponentFactory getDefaultComponentFactory() {
//        return defaultComponentFactory;
//    }

    /**
     * Return KindSpecFactory
     *
     * @return KindSpecFactory
     */
    @Override
    public KindSpecFactory getKindSpecFactory() {
        return kindSpecFactory;
    }

    /**
     * VariableResolver ResourceFactory
     *
     * @return VariableResolver ResourceFactory
     */
    @Override
    public VariableResolverFactory getVariableResolverFactory() {
        return variableResolverFactory;
    }

    /**
     * Coin Control, maintain objects by Yaml or JSON
     *
     * @return Coin Control
     */
    @Override
    public CoinController getController() {
        return coinController;
    }

    /**
     * Coin Lookup
     *
     * @return CoinLookup
     */
    @Override
    public CoinLookup getLookup() {
        return lookup;
    }

    /**
     * Remote Generator, for RMI or gRPC extension
     *
     * @return Remote Generator
     */
    @Override
    public RemoteGenerator getRemoteGenerator() {
        if (remoteGenerator == null) {
            remoteGenerator = ImplementUtil.getInstance(RemoteGenerator.class);
        }
        return remoteGenerator;
    }

    /**
     * Return LifecycleStrategy
     *
     * @return LifecycleStrategy
     */
    @Override
    public LifecycleStrategy getLifecycleStrategy() {
        return lifecycleStrategy;
    }

    /**
     * Return BeanEventListeners
     */
    @Override
    public BeanEventListeners getBeanEventListeners() {
        return lifecycleStrategy.getEventListeners();
    }

    @Override
    public BeanInfoFactory getBeanInfoFactory() {
        return beanInfoFactory;
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
        namespaceCoin = namespaceFactory.get(CoinConstants.NAMESPACE_COIN);
        reloader = new SimpleCoinReloader(coinController);

        ComponentFactory componentFactory = namespaceCoin.getComponentFactory();
        componentFactory.create(NAME_KIND_SPEC_FACTORY, kindSpecFactory, BeanStage.INITIALIZE);
        componentFactory.create(NAME_NAMESPACE_FACTORY, namespaceFactory, BeanStage.INITIALIZE);
        componentFactory.create(NAME_CONFIGURATOR_FACTORY, configuratorFactory, BeanStage.INITIALIZE);
        componentFactory.create(NAME_VARIABLE_RESOLVER_FACTORY, variableResolverFactory, BeanStage.INITIALIZE);
        componentFactory.create(NAME_LIFECYCLE_STRATEGY, lifecycleStrategy, BeanStage.INITIALIZE);
        componentFactory.create(NAME_CONTROLLER, coinController, BeanStage.INITIALIZE);
        componentFactory.create(NAME_LOOKUP, lookup, BeanStage.INITIALIZE);
        componentFactory.create(NAME_MODULE, this, BeanStage.CREATE);
    }

    /**
     * Start internally
     */
    public void _doStart() {
        ComponentFactory componentFactory = namespaceCoin.getComponentFactory();
        componentFactory.create(NAME_RELOADER, reloader, BeanStage.START);
    }
}
