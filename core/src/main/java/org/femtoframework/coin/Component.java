package org.femtoframework.coin;

import org.femtoframework.bean.BeanStage;
import org.femtoframework.bean.NamedBean;
import org.femtoframework.coin.annotation.Property;
import org.femtoframework.coin.exception.NoSuchNamespaceException;
import org.femtoframework.coin.info.BeanInfo;
import org.femtoframework.coin.naming.CoinName;
import org.femtoframework.coin.spec.BeanSpec;
import org.femtoframework.coin.status.BeanStatus;

import javax.naming.Name;
import java.util.Map;

/**
 * Component
 *
 * A component includes spec, bean, and lifecycle status.
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public interface Component extends NamedBean {
    /**
     * Namespace of this component
     *
     * @return Namespace
     */
    default String getNamespace() {
        BeanSpec spec = getSpec();
        return spec != null ? spec.getNamespace() : null;
    }

    /**
     * Name of this bean
     *
     * @return Name of the bean and component
     */
    default String getName() {
        BeanSpec spec = getSpec();
        return spec != null ? spec.getName() : null;
    }

    /**
     * Absolute name in coin container, it doesn't include namespace
     *
     * 1. Level 1 components,
     * name
     * 2. Level 2 components,
     * name.childName
     *
     * @return
     */
    default String getAbsoluteName() {
        return getName();
    }

    /**
     * Bean Spec
     *
     * @return BeanSpec
     */
    BeanSpec getSpec();

    /**
     * Qualified Name
     *
     * @return Qualified Name
     */
    default String getQualifiedName() {
        return getNamespace() + CoinConstants.CHAR_COLON + getAbsoluteName();
    }

    /**
     *
     * Get Namespace object by name
     *
     * @param namespace Namespace name, if it is null or empty string, return the current namespace object
     * @return Namespace object
     * @throws org.femtoframework.coin.exception.NoSuchNamespaceException if the namespace doesn't exist
     */
    default Namespace getNamespaceByName(String namespace) {
        if (namespace == null || namespace.isEmpty()) {
            return getCurrentNamespace();
        }
        Namespace ns = getNamespaceFactory().get(namespace);
        if (ns == null) {
            throw new NoSuchNamespaceException("No such namespace:" + namespace);
        }
        return ns;
    }

    /**
     * Return current namespace
     *
     * @return Current Namespace
     */
    Namespace getCurrentNamespace();

    /**
     * Bean Factory
     *
     * @return Current Bean Factory
     */
    default BeanFactory getCurrentBeanFactory() {
        return getCurrentNamespace().getBeanFactory();
    }

    /**
     * Component Factory
     *
     * @return Current Component Factory
     */
    default ComponentFactory getCurrentComponentFactory() {
        return getCurrentNamespace().getComponentFactory();
    }

    /**
     * Return current bean
     *
     * @return current bean
     */
    Object getBean();

    /**
     * Bean Status
     *
     * @return BeanStatus
     */
    BeanStatus getStatus();

    /**
     * Return expected Stage
     *
     * @return expected Stage
     */
    BeanStage getStage();

    /**
     * Return the bean info of this bean
     *
     * @return BeanInfo
     */
    default BeanInfo getBeanInfo() {
        return getModule().getBeanInfoFactory().getBeanInfo(getSpec().getTypeClass());
    }

    /**
     * Set the target bean stage, that means the component will be executed to given stage
     */
    void setStage(BeanStage stage);

    /**
     * Return the right bean with expectedType
     *
     * @param expectedType ExpectedType, if the is null, that means returning the bean implementation object anyway
     * @return Bean
     */
    <T> T getBean(Class<T> expectedType);

    /**
     * Return namespace factory
     *
     * @return namespace factory
     */
    default NamespaceFactory getNamespaceFactory() {
        return getModule().getNamespaceFactory();
    }


    /**
     * Return coin module
     *
     * @return module;
     */
    CoinModule getModule();

    //Children
    /**
     * Return all children
     *
     * @return all Children
     */
    default Map<String, Component> getChildren() {
        return null;
    }

    /**
     * Add Child component, if the child bean is an anonymous Component, it will be put into the parent instead of adding it to ComponentFactory
     *
     * @param propertyName Property Name
     * @param component Child Component
     */
    default void addChild(String propertyName, Component component) {
    }

    /**
     * Return Component by Formatted property name
     *
     * @param name Formatted Property Name,
     * @return
     */
    default Component getChild(String name) {
        return null;
    }

    /**
     * Return resource by given resource Type
     *
     * @param resourceType COMPONENT, BEAN, INFO, SPEC
     * @throws IllegalArgumentException if other resource type
     */
    default Object getResource(ResourceType resourceType) {
        switch (resourceType) {
            case COMPONENT:
                return this;
            case BEAN:
                return getBean();
            case SPEC:
                return getSpec();
            case INFO:
                return getBeanInfo();
        }
        throw new IllegalArgumentException("Resource type:" + resourceType + " doesn't support");
    }
}
