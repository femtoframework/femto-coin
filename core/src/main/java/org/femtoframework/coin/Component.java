package org.femtoframework.coin;

import org.femtoframework.bean.BeanStage;
import org.femtoframework.coin.exception.NoSuchNamespaceException;
import org.femtoframework.bean.info.BeanInfo;
import org.femtoframework.coin.spec.BeanSpec;
import org.femtoframework.coin.spec.ComponentSpec;
import org.femtoframework.coin.status.BeanStatus;

import java.util.List;
import java.util.Map;

import static org.femtoframework.coin.CoinConstants.*;

/**
 * Component
 *
 * A component includes spec, bean, and lifecycle status.
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public interface Component extends Model<BeanSpec, BeanStatus> {

    /**
     * Namespace of this component
     *
     * @return Namespace
     */
    default String getNamespace() {
        BeanSpec spec = getSpec();
        if (spec instanceof ComponentSpec) {
            return spec.getNamespace();
        }
        return null;
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
     * @return GeneratedName
     */
    default String getGenerateName() {
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
        return getNamespace() + CoinConstants.CHAR_COLON + getGenerateName();
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
     * Bean ResourceFactory
     *
     * @return Current Bean ResourceFactory
     */
    default BeanFactory getCurrentBeanFactory() {
        return getCurrentNamespace().getBeanFactory();
    }

    /**
     * Component ResourceFactory
     *
     * @return Current Component ResourceFactory
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


    /**
     * Return belongsTo
     *
     * belongsTo syntax
     *
     * [NAMESPACE:]&lt;NAME&gt;#&lt;METHOD_NAME&gt;
     * 1. The method must be declared on an interface
     * 2. The method must have only one argument
     * 3. The method argument should be an interface which the current bean implements
     * 4. NAMESPACE is optional, if it doesn't specify, use the bean's namespace
     *
     * @return A list of belongsTo
     */
    default List<String> getBelongsTo() {
        return getLabels().getStringList(LABEL_BELONGS_TO);
    }

    /**
     * Is it singleton?
     * If it is true, then coin will try to create it in singleton pattern first.
     *
     * @return Singleton
     */
    default boolean isSingleton() {
        return getLabels().getBoolean(LABEL_SINGLETON);
    }

    /**
     * Whether it is default for the specific interface.
     * If multiple beans set default==true, the last one will be enabled as "default=true" in the factory
     *
     * @return is it the default implementation of the interface
     */
    default boolean isDefault() {
        return getLabels().getBoolean(LABEL_DEFAULT);
    }

    /**
     * Whether the bean is enabled or not.
     *
     * @return Is enabled or not
     */
    default boolean isEnabled() {
        return getLabels().getBoolean(LABEL_ENABLED);
    }
}
