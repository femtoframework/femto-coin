package org.femtoframework.coin.spec.ext;

import org.femtoframework.bean.BeanStage;
import org.femtoframework.bean.InitializableMBean;
import org.femtoframework.coin.*;
import org.femtoframework.coin.exception.SpecSyntaxException;
import org.femtoframework.coin.ext.BaseResourceFactory;
import org.femtoframework.coin.spec.VariableResolver;
import org.femtoframework.coin.spec.VariableResolverFactory;
import org.femtoframework.coin.spec.VariableSpec;
import org.femtoframework.implement.ImplementUtil;
import org.femtoframework.implement.InstancesFunction;


/**
 * VariableResolverFactory
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public class SimpleVariableResolverFactory extends BaseResourceFactory<VariableResolver> implements VariableResolverFactory, InitializableMBean {


    public SimpleVariableResolverFactory(NamespaceFactory namespaceFactory) {
        super(namespaceFactory, CoinConstants.NAMESPACE_COIN);
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
    public void _doInitialize() {
        ImplementUtil.applyInstances(VariableResolver.class, (InstancesFunction<String, VariableResolver>) this::add);

        applyStageToChildren("vr_", BeanStage.INITIALIZE);
    }

    /**
     * Resolve variable by spec
     *
     * @param var          VariableSpec
     * @param expectedType
     * @param component
     * @return
     */
    @Override
    public <T> T resolve(VariableSpec var, Class<T> expectedType, Component component) {
        String prefix = var.getPrefix();
        if (prefix == null) {
            prefix = "b";
        }
        VariableResolver vr = get(prefix);
        if (vr == null) {
            throw new SpecSyntaxException("No such prefix;" + prefix +" in variable:" + var.getName());
        }
        return vr.resolve(var, expectedType, component);
    }
}
