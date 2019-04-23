package org.femtoframework.coin.spec.element;

import org.femtoframework.bean.annotation.Ignore;
import org.femtoframework.coin.Component;
import org.femtoframework.coin.ComponentFactory;
import org.femtoframework.coin.spec.BeanSpec;
import org.femtoframework.coin.spec.ComponentSpec;
import org.femtoframework.coin.spec.CoreKind;
import org.femtoframework.coin.spec.Element;
import org.femtoframework.lang.reflect.NoSuchClassException;
import org.femtoframework.lang.reflect.Reflection;
import org.femtoframework.parameters.ParametersMap;

import java.util.Map;

import static org.femtoframework.coin.CoinConstants.CLASS;
import static org.femtoframework.coin.CoinConstants.NAMESPACE;

public class ComponentElement extends ModelElement<BeanSpec> implements ComponentSpec {

    public ComponentElement() {
        super(CoreKind.COMPONENT);
    }

    public ComponentElement(Map map) {
        super(map);
        setKind(CoreKind.COMPONENT);
    }

    public ComponentElement(String namespace, String name, Class<?> implClass) {
        super(CoreKind.COMPONENT);
        setNamespace(namespace);
        setName(name);
        setTypeClass(implClass);
    }

    public void setNamespace(String namespace) {
        getMetadata().put(NAMESPACE, new PrimitiveElement<>(CoreKind.STRING, namespace));
    }

    public void setType(String type) {
        put(CLASS, new PrimitiveElement<>(CoreKind.STRING, type));
    }

    public void setTypeClass(Class<?> implClass) {
        setType(implClass.getName());
        this.typeClass = implClass;
    }

    @Ignore
    private transient Class<?> typeClass;

//    /**
//     * Return belongsTo
//     * <p>
//     * belongsTo syntax
//     *
//     * @return
//     */
//    public List<String> getBelongsTo() {
//        return DataUtil.getStringList(getValue(LABEL_BELONGS_TO), BeanSpec.super.getBelongsTo());
//    }
//
//    /**
//     * Set belongsTo
//     *
//     * @param belongsTo BelongsTo
//     */
//    public void setBelongsTo(String belongsTo) {
//        put(LABEL_BELONGS_TO, new PrimitiveElement<>(CoreKind.STRING, belongsTo));
//    }

    /**
     * Indicate the kind of this bean
     *
     * @return
     */
    public String getType() {
        return getSpec().getString(CLASS, null);
    }

    /**
     * The real class of the kind
     *
     * @return kind class
     */
    @Override
    public Class<?> getTypeClass() {
        String type = getType();
        if (typeClass == null) {
            if (type != null) {
                try {
                    typeClass = Reflection.getClass(type);
                } catch (ClassNotFoundException e) {
                    throw new NoSuchClassException("No such type:" + type + " in spec:" + getName());
                }
            }
        }
        return typeClass;
    }

    /**
     * Return the value of this element definition
     *
     * @param expectedType    Expected kind
     * @param parentComponent Component
     * @return the value
     */
    public <T> T getValue(Class<T> expectedType, Component parentComponent) {
        Map values = new ParametersMap();
        for (Map.Entry<String, Element> entry : getSpec().entrySet()) {
            String key = entry.getKey();
//            if (!key.startsWith("_")) {
            values.put(key, entry.getValue().getValue(null, parentComponent));
//            }
        }

        if (expectedType == null) {
            expectedType = (Class<T>) getTypeClass();
        }

        ComponentFactory factory = parentComponent.getCurrentComponentFactory();
        Component component = factory.create(null, this, parentComponent.getStage());
        return component.getBean(expectedType);
    }
}
