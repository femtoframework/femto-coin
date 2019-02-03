package org.femtoframework.coin;

import org.femtoframework.bean.NamedBean;
import org.femtoframework.coin.annotation.Property;
import org.femtoframework.coin.spec.*;

/**
 * Namespace
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public interface Namespace extends NamedBean {

    /**
     * Namespace
     *
     * @return Namespace's name
     */
    String getName();

    /**
     * Component Factory under this namespace
     *
     * @return Component Factory
     */
    ComponentFactory getComponentFactory();

    /**
     * Bean Factory under this namespace;
     *
     * @return BeanFactory
     */
    BeanFactory getBeanFactory();


    /**
     * Return BeanSpecFactory
     *
     * @return BeanSpecFactory
     */
    @Property(writable = false)
    default SpecFactory<BeanSpec> getBeanSpecFactory() {
        return getSpecFactory(BeanSpec.class);
    }

    /**
     * Return ConfigSpecFactory
     *
     * @return ConfigSpecFactory
     */
    @Property(writable = false)
    default SpecFactory<ConfigSpec> getConfigSpecFactory() {
        return getSpecFactory(ConfigSpec.class);
    }

    /**
     * Spec Factory under this namespace;
     *
     * @param specClass Return spec factory by specClass
     * @return SpecFactory
     */
    <S extends ModelSpec> SpecFactory<S> getSpecFactory(Class<S> specClass);
}
