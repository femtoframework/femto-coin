package org.femtoframework.coin.ext;

import org.femtoframework.bean.Nameable;
import org.femtoframework.bean.BeanStage;
import org.femtoframework.coin.CoinModule;
import org.femtoframework.coin.Component;
import org.femtoframework.coin.Namespace;
import org.femtoframework.bean.annotation.Coined;
import org.femtoframework.bean.annotation.Ignore;
import org.femtoframework.bean.annotation.Property;
import org.femtoframework.bean.info.BeanInfo;
import org.femtoframework.coin.spec.*;
import org.femtoframework.coin.spec.element.PrimitiveElement;
import org.femtoframework.coin.status.BeanStatus;
import org.femtoframework.parameters.Parameters;
import org.femtoframework.util.convert.ConverterUtil;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.femtoframework.coin.CoinConstants.NAME;

/**
 * Simple Component
 *
 * @author Sheldon Shao
 * @version 1.0
 */
@Coined
public class SimpleComponent extends SimpleModel<BeanSpec, BeanStatus>
        implements Component, Nameable
{
    private BeanStage stage = BeanStage.CREATE;

    private transient Object bean;

    @Ignore
    private transient CoinModule module;

    @Ignore
    private transient Namespace currentNamespace;

    public SimpleComponent(CoinModule module, String namespace, BeanSpec spec) {
        setNamespace(namespace);
        setName(spec.getName());
        setKind(spec instanceof ComponentSpec ? CoreKind.COMPONENT : CoreKind.BEAN);
        setSpec(spec);
        setStatus(new SimpleBeanStatus());
        this.module = module;
        if (module != null) {
            this.currentNamespace = module.getNamespaceFactory().get(getNamespace());
        }
    }

    /**
     * Namespace of this component
     *
     * @return Namespace
     */
    @Property(writable = false)
    public String getNamespace() {
        return super.getNamespace();
    }

    /**
     * Name of this bean
     *
     * @return Name of the bean and component
     */
    @Property(writable = false)
    public String getName() {
        return super.getName();
    }

    /**
     * Generated Name
     */
    private String generateName = null;

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
    public String getGenerateName() {
        if (generateName == null) {
            generateName = getName();
        }
        return generateName;
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
        super.setName(name);
        if (getSpec() != null) {
            getSpec().put(NAME, new PrimitiveElement<>(CoreKind.STRING, name));
        }
    }

    /**
     * Labels
     *
     * @return Labels
     */
    @Override
    public Parameters<String> getLabels() {
        BeanSpec spec = getSpec();
        if (spec instanceof ComponentSpec) {
            ComponentSpec componentSpec = (ComponentSpec)spec;
            return new ParametersSpecAdapter(componentSpec.getMetadata().getLabels());
        }
        else {
            return super.getLabels();
        }
    }

    /**
     * Annotations
     *
     * @return Annotations
     */
    @Override
    public Parameters<String> getAnnotations() {
        BeanSpec spec = getSpec();
        if (spec instanceof ComponentSpec) {
            ComponentSpec componentSpec = (ComponentSpec)spec;
            return new ParametersSpecAdapter(componentSpec.getMetadata().getLabels());
        }
        else {
            return super.getAnnotations();
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
            simpleComponent.generateName = getGenerateName() + "." + propertyName;
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
