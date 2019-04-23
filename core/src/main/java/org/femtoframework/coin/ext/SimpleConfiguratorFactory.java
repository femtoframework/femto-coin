package org.femtoframework.coin.ext;

import org.femtoframework.bean.BeanStage;
import org.femtoframework.bean.InitializableMBean;
import org.femtoframework.coin.*;
import org.femtoframework.implement.ImplementUtil;
import org.femtoframework.implement.InstancesFunction;

/**
 * Simple Configurator ResourceFactory
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public class SimpleConfiguratorFactory extends BaseResourceFactory<Configurator> implements ConfiguratorFactory, InitializableMBean {

    protected SimpleConfiguratorFactory() {
        super(null, CoinConstants.NAMESPACE_COIN);

    }

    /**
     * Configure the bean
     *
     * @param component Component
     */
    @Override
    public void configure(Component component) {
        for(Configurator configurator: this) {
            configurator.configure(component);
        }
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
        ImplementUtil.applyInstances(Configurator.class, (InstancesFunction<String, Configurator>) this::add);
        applyStageToChildren("configurator_", BeanStage.INITIALIZE);
    }
}
