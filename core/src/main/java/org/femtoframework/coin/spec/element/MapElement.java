package org.femtoframework.coin.spec.element;

import org.femtoframework.bean.info.BeanInfo;
import org.femtoframework.bean.info.BeanInfoUtil;
import org.femtoframework.bean.info.PropertyInfo;
import org.femtoframework.coin.Component;
import org.femtoframework.coin.spec.*;
import org.femtoframework.util.convert.ConverterUtil;

import java.util.*;

/**
 * Map Element
 *
 * @author fengyun
 * @version 1.00 2011-04-28 13:04
 */
public class MapElement<E extends Element> extends LinkedHashMap<String, E>
    implements MapSpec<E>
{

    private Kind kind = CoreKind.MAP;

    public MapElement() {
    }

    public MapElement(Map map) {
        super(map);
    }

    protected MapElement(Kind kind) {
        this.kind = kind;
    }

    /**
     * Element Type
     *
     * @return Element Type
     */
    @Override
    public Kind getKind() {
        return kind;
    }

    /**
     * Class of the element, for example "java.util.ArrayList"
     * For all other element except "Bean" are certain class.
     * <p>
     * MAP: org.femtoframework.parameters.ParametersMap
     * LIST: java.util.ArrayList
     * STRING: java.lang.String
     * INT: java.lang.Integer
     * LONG: java.lang.Long
     * DOUBLE: java.lang.Double
     * Bean: the class in the "class" field
     *
     * @return
     */
    @Override
    public String getKindClass() {
        return null;
    }

    /**
     * Return the value of this element definition
     *
     * @param expectedType Expected kind
     * @param component    Component
     * @return the value
     */
    public <T> T getValue(Class<T> expectedType, Component component) {
        Map<String, Object> values = new HashMap<>(size());
        for(Map.Entry<String, E> entry: entrySet()) {
            values.put(entry.getKey(), entry.getValue().getValue(null, component));
        }
        if (expectedType != null) {
            Object value = ConverterUtil.convertToType(values, expectedType);
            if (value != null) {
                return (T)value;
            }
            else {
                BeanInfo beanInfo = BeanInfoUtil.getBeanInfo(expectedType);
                if (beanInfo != null) {
                    try {
                        value = expectedType.newInstance();
                        for(Map.Entry<String, Object> entry: values.entrySet()) {
                            if (entry.getValue() != null) {
                                PropertyInfo propertyInfo = beanInfo.getProperty(entry.getKey());
                                if (propertyInfo != null) {
                                    propertyInfo.invokeSetter(value, entry.getValue());
                                }
                            }
                        }
                        return (T)value;
                    } catch (Exception x) {
                        return null;
                    }
                }
                return null;
            }
        }
        else {
            return (T)values;
        }
    }

    public void setKind(CoreKind kind) {
        this.kind = kind;
    }


    public Object getValue(String key) {
        return MapSpec.getValue(this, key);
    }

    public Object getValue(String key, Object defValue) {
        return MapSpec.getValue(this, key, defValue);
    }

    public String getString(String key, String defaultValue) {
        return MapSpec.getString(this, key, defaultValue);
    }
}
