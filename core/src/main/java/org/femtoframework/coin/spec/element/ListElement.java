package org.femtoframework.coin.spec.element;


import org.femtoframework.coin.Component;
import org.femtoframework.coin.spec.Element;
import org.femtoframework.coin.spec.CoreKind;
import org.femtoframework.coin.spec.Kind;
import org.femtoframework.coin.spec.ListSpec;
import org.femtoframework.util.convert.ConverterUtil;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;

/**
 * List Element
 *
 * @author fengyun
 * @version 1.00 2011-04-28 12:52
 */
public class ListElement<E extends Element> extends ArrayList<E>
    implements ListSpec<E>
{

    public ListElement(List list) {
        super(list);
    }

    /**
     * Element Type
     *
     * @return Element Type
     */
    @Override
    public Kind getKind() {
        return CoreKind.LIST;
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
        return "java.util.ArrayList";
    }

    /**
     * Return the value of this element definition
     *
     * @param expectedType Expected kind
     * @param genericType
     * @param component    Component
     * @return the value
     */
    public <T> T getValue(Class<T> expectedType, Type genericType, Component component) {
        if (expectedType != null) {
            if (expectedType == List.class) {
                if (genericType instanceof ParameterizedType) {
                    Type[] actualTypes = ((ParameterizedType)genericType).getActualTypeArguments();
                    if (actualTypes != null && actualTypes.length == 1) {
                        List values = new ArrayList(size());
                        for (Element element : this) {
                            if (actualTypes[0] instanceof Class) {
                                values.add(element.getValue((Class) actualTypes[0], actualTypes[0], component));
                            }
                            else if (actualTypes[0] instanceof ParameterizedType) {
                                values.add(element.getValue((Class<?>)((ParameterizedType) actualTypes[0]).getRawType(), actualTypes[0], component));
                            }
                            else {
                                values.add(element.getValue(null, actualTypes[0], component));
                            }
                        }
                        return (T) values;
                    }
                }
            }
        }

        List values = new ArrayList(size());
        for (Element element : this) {
            values.add(element.getValue(null, null, component));
        }
        if (expectedType != null) {
            return ConverterUtil.convertToType(values, expectedType);
        }
        else {
            return (T)values;
        }
    }
}
