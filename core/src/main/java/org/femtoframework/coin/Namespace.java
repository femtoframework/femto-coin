package org.femtoframework.coin;

import org.femtoframework.bean.NamedBean;
import org.femtoframework.bean.annotation.Property;
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
     * Component ResourceFactory under this namespace
     *
     * @return Component ResourceFactory
     */
    ComponentFactory getComponentFactory();

    /**
     * Bean ResourceFactory under this namespace;
     *
     * @return BeanFactory
     */
    BeanFactory getBeanFactory();

    /**
     * Return the factory by resource,
     *
     * Resources:
     * component
     * bean
     * spec
     * config
     *
     * @param resource Resource Type
     * @param <T> Component, BeanSpec, ConfigSpec or Bean
     * @return ResourceFactory
     */
    default <T> ResourceFactory<T> getFactory(ResourceType resource) {
        switch (resource){
            case COMPONENT:
                return (ResourceFactory<T>)getComponentFactory();
            case BEAN:
                return (ResourceFactory<T>)getBeanFactory();
            case SPEC:
                return (ResourceFactory<T>)getBeanSpecFactory();
            case CONFIG:
                return (ResourceFactory<T>)getConfigSpecFactory();
            default:
                throw new IllegalArgumentException("Unsupport resource type:" + resource);
        }
    }

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
     * Spec ResourceFactory under this namespace;
     *
     * @param specClass Return spec factory by specClass
     * @return SpecFactory
     */
    <S extends ModelSpec> SpecFactory<S> getSpecFactory(Class<S> specClass);
}
