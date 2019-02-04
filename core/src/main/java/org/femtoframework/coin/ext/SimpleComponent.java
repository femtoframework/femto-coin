package org.femtoframework.coin.ext;

import org.femtoframework.bean.Nameable;
import org.femtoframework.bean.BeanStage;
import org.femtoframework.coin.CoinModule;
import org.femtoframework.coin.Component;
import org.femtoframework.coin.Namespace;
import org.femtoframework.coin.NamespaceFactory;
import org.femtoframework.coin.annotation.Coined;
import org.femtoframework.coin.annotation.Ignore;
import org.femtoframework.coin.annotation.Property;
import org.femtoframework.coin.info.BeanInfo;
import org.femtoframework.coin.naming.CoinName;
import org.femtoframework.coin.spec.BeanSpec;
import org.femtoframework.coin.spec.CoreKind;
import org.femtoframework.coin.spec.element.PrimitiveElement;
import org.femtoframework.coin.status.BeanStatus;
import org.femtoframework.util.convert.ConverterUtil;

import javax.naming.Name;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.femtoframework.coin.spec.SpecConstants.NAME;

/**
 * Simple Component
 *
 * @author Sheldon Shao
 * @version 1.0
 */
@Coined
public class SimpleComponent implements Component, Nameable {

    private BeanSpec spec;

    private BeanStatus status = new SimpleBeanStatus();
    private BeanStage stage = BeanStage.CREATE;

    private transient Object bean;

    @Ignore
    private transient CoinModule module;

    @Ignore
    private transient Namespace currentNamespace;

    public SimpleComponent(CoinModule module, BeanSpec spec) {
        this.module = module;
        this.spec = spec;
        this.currentNamespace = module.getNamespaceFactory().get(spec.getNamespace());
    }

    /**
     * Namespace of this component
     *
     * @return Namespace
     */
    @Property(writable = false)
    public String getNamespace() {
        return Component.super.getNamespace();
    }

    /**
     * Name of this bean
     *
     * @return Name of the bean and component
     */
    @Property(writable = false)
    public String getName() {
        return Component.super.getName();
    }

    /**
     * Absolute Name
     */
    private String absoluteName = null;

    /**
     * Absolute name in coin container
     * <p>
     * 1. Level 1 components,
     * name
     * 2. Level 2 components,
     * name.childName
     *
     * @return
     */
    @Override
    public String getAbsoluteName() {
        if (absoluteName == null) {
            absoluteName = getName();
        }
        return absoluteName;
    }

    /**
     * Bean Spec
     *
     * @return BeanSpec
     */
    @Override
    public BeanSpec getSpec() {
        return spec;
    }

    /**
     * Return current namespace
     *
     * @return Current Namespace
     */
    @Override
    public Namespace getCurrentNamespace() {
        return currentNamespace;
    }

    /**
     * Bean Status
     *
     * @return BeanStatus
     */
    @Override
    public BeanStatus getStatus() {
        return status;
    }

    /**
     * Set the target bean stage, that means the component will be executed to given stage
     *
     * @param stage
     */
    @Override
    public void setStage(BeanStage stage) {
        this.stage = stage;
    }

    /**
     * Return the right bean with expectedType
     *
     * @param expectedType ExpectedType, if the is null, that means returning the bean implementation object anyway
     * @return Bean
     */
    @Override
    public <T> T getBean(Class<T> expectedType) {
        if (expectedType == null) {
            return (T)getBean();
        }
        else {
            return ConverterUtil.convertToType(getBean(), expectedType);
        }
    }

    /**
     * Return coin module
     *
     * @return module;
     */
    @Override
    public CoinModule getModule() {
        return module;
    }

    /**
     * Return expected Stage
     *
     * @return expected Stage
     */
    @Override
    public BeanStage getStage() {
        return stage;
    }

    public Object getBean() {
        return bean;
    }

    public void setBean(Object bean) {
        this.bean = bean;
    }

    @Property(writable = false)
    public BeanInfo getBeanInfo() {
        return Component.super.getBeanInfo();
    }

    /**
     * Set name of the object
     *
     * @param name Name
     */
    @Override
    public void setName(String name) {
        if (spec != null) {
            spec.put(NAME, new PrimitiveElement<>(CoreKind.STRING, name));
        }
    }

    private Map<String, Component> children = null;

    /**
     * Return all children
     *
     * @return all Children
     */
    public Map<String, Component> getChildren() {
        return children;
    }

    /**
     * Add Child component, if the child bean is an anonymous Component, it will be put into the parent instead of adding it to ComponentFactory
     *
     * @param propertyName Property Name
     * @param component Child Component
     */
    public void addChild(String propertyName, Component component) {
        if (children == null) {
            children = new LinkedHashMap<>(4);
        }
        String name = component.getName();
        if (name == null && component instanceof Nameable) {
            ((Nameable)component).setName(propertyName);
        }
        if (component instanceof SimpleComponent) {
            SimpleComponent simpleComponent = (SimpleComponent)component;
            simpleComponent.absoluteName = getAbsoluteName() + "." + propertyName;
        }
        children.put(propertyName, component);
    }

    /**
     * Return Component by Formatted property name
     *
     * @param name Formatted Property Name,
     * @return
     */
    public Component getChild(String name) {
        return children != null ? children.get(name) : null;
    }
}
