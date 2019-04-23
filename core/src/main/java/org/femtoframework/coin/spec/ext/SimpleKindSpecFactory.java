package org.femtoframework.coin.spec.ext;

import org.femtoframework.bean.BeanStage;
import org.femtoframework.bean.InitializableMBean;
import org.femtoframework.coin.CoinConstants;
import org.femtoframework.coin.ext.BaseResourceFactory;
import org.femtoframework.coin.spec.KindSpec;
import org.femtoframework.coin.spec.KindSpecFactory;
import org.femtoframework.implement.ImplementUtil;
import org.femtoframework.implement.InstancesFunction;

import static org.femtoframework.coin.CoinConstants.VERSION_COIN_V1;

/**
 * Simple Kind Spec ResourceFactory
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public class SimpleKindSpecFactory extends BaseResourceFactory<KindSpec> implements KindSpecFactory, InitializableMBean {

    public SimpleKindSpecFactory() {
        super(null, CoinConstants.NAMESPACE_COIN);
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
        ImplementUtil.applyInstances(KindSpec.class, (InstancesFunction<String, KindSpec>) this::add);
        applyStageToChildren("kind_spec_", BeanStage.INITIALIZE);
    }

    /**
     * Return the core kind spec
     *
     * @return Core Kind Spec
     */
    @Override
    public KindSpec getCoreKindSpec() {
        return get(VERSION_COIN_V1);
    }
}
