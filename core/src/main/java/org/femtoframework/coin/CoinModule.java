package org.femtoframework.coin;

import org.femtoframework.annotation.ImplementedBy;
import org.femtoframework.coin.event.BeanEventListeners;
import org.femtoframework.bean.info.BeanInfoFactory;
import org.femtoframework.coin.remote.RemoteGenerator;
import org.femtoframework.coin.spec.KindSpecFactory;
import org.femtoframework.coin.spec.VariableResolverFactory;

/**
 * Coin Module
 *
 * special namespaces
 *
 *
 * @author Sheldon Shao
 * @version 1.0
 */
@ImplementedBy("org.femtoframework.coin.ext.SimpleCoinModule")
public interface CoinModule {

    /**
     * Return namespace factory
     *
     * @return Namespace ResourceFactory
     */
    NamespaceFactory getNamespaceFactory();


    /**
     * Return KindSpecFactory
     *
     * @return KindSpecFactory
     */
    default KindSpecFactory getKindSpecFactory() {
        return null;
    }


    /**
     * VariableResolver ResourceFactory
     *
     * @return VariableResolver ResourceFactory
     */
    default VariableResolverFactory getVariableResolverFactory() {
        return null;
    }

    /**
     * Coin Control, maintain objects by Yaml or JSON
     *
     * @return Coin Control
     */
    CoinController getController();

    /**
     * Coin Lookup
     *
     * @return CoinLookup
     */
    default CoinLookup getLookup() {
        return null;
    }

    /**
     * Remote Generator, for RMI or gRPC extension
     *
     * @return Remote Generator
     */
    default RemoteGenerator getRemoteGenerator() {
        return null;
    }


    /**
     * Return LifecycleStrategy
     *
     * @return LifecycleStrategy
     */
    LifecycleStrategy getLifecycleStrategy();

    /**
     * Return BeanEventListeners
     */
    default BeanEventListeners getBeanEventListeners() {
        return null;
    }

    /**
     * Bean Info factory
     *
     * BeanInfo makes the following things very easy
     * 1. JMX MBean, BeanInfo can help BusinessObject to generate JMX MBean automatically.
     * 2. POJO, BeanInfo helps it to ignore some properties or have different getters or setters.
     * 3. DTO, BeanInfo can use it as decoding specification.
     *
     * @return BeanInfoFactory
     */
    BeanInfoFactory getBeanInfoFactory();
}
