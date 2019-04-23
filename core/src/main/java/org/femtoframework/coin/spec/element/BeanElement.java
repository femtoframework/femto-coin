package org.femtoframework.coin.spec.element;

import org.femtoframework.coin.Component;
import org.femtoframework.coin.ComponentFactory;
import org.femtoframework.bean.annotation.Ignore;
import org.femtoframework.coin.spec.BeanSpec;
import org.femtoframework.coin.spec.CoreKind;
import org.femtoframework.coin.spec.Element;
import org.femtoframework.coin.spec.ModelSpec;
import org.femtoframework.lang.reflect.NoSuchClassException;
import org.femtoframework.lang.reflect.Reflection;
import org.femtoframework.parameters.ParametersMap;

import java.util.Map;

import static org.femtoframework.coin.CoinConstants.*;

/**
 * Bean Element
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public class BeanElement extends MapElement<Element> implements BeanSpec {

    public BeanElement() {
        super(CoreKind.BEAN);
    }

    public BeanElement(Map map) {
        super(map);
        setKind(CoreKind.BEAN);
    }

    public BeanElement(String namespace, String name, Class<?> implClass) {
        super(CoreKind.BEAN);
        setNamespace(namespace);
        setName(name);
        setTypeClass(implClass);
    }

    public void setNamespace(String namespace) {
        put(NAMESPACE, new PrimitiveElement<>(CoreKind.STRING, namespace));
    }


    public void setType(String type) {
        put(CLASS, new PrimitiveElement<>(CoreKind.STRING, type));
    }

    public void setTypeClass(Class<?> implClass) {
        setType(implClass.getName());
        this.typeClass = implClass;
    }

    /**
     * Return generated name
     *
     * @return generated name
     */
    public String getGenerateName() {
        return BeanSpec.super.getGenerateName();
    }

    @Ignore
    private transient Class<?> typeClass;

    /**
     * Indicate the kind of this bean
     *
     * @return
     */
    public String getType() {
        return getString(CLASS, null);
    }

    /**
     * Name of the object
     *
     * @return Name of the object
     */
    @Override
    public String getName() {
        return getString(NAME, null);
    }

    @Override
    public String getNamespace() {
        return getString(NAMESPACE, null);
    }

    public void setName(String name) {
        put(NAME, new PrimitiveElement<>(CoreKind.STRING, name));
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
     * @param expectedType Expected kind
     * @param parentComponent    Component
     * @return the value
     */
    public <T> T getValue(Class<T> expectedType, Component parentComponent) {
        Map values = new ParametersMap();
        for(Map.Entry<String, Element> entry: entrySet()) {
            String key = entry.getKey();
            if (!key.startsWith("_")) {
                values.put(key, entry.getValue().getValue(null, parentComponent));
            }
        }

        if (expectedType == null) {
            expectedType = (Class<T>)getTypeClass();
        }

        ComponentFactory factory = parentComponent.getCurrentComponentFactory();
        Component component = factory.create(null, this, parentComponent.getStage());
        return component.getBean(expectedType);
    }
}
