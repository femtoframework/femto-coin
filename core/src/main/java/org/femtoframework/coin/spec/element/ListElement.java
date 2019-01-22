package org.femtoframework.coin.spec.element;


import org.femtoframework.coin.Component;
import org.femtoframework.coin.spec.Element;
import org.femtoframework.coin.spec.CoreKind;
import org.femtoframework.coin.spec.Kind;
import org.femtoframework.coin.spec.ListSpec;
import org.femtoframework.util.convert.ConverterUtil;

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
     * @param component    Component
     * @return the value
     */
    public <T> T getValue(Class<T> expectedType, Component component) {
        List values = new ArrayList(size());
        for(Element element : this) {
            values.add(element.getValue(null, component));
        }
        if (expectedType != null) {
            return ConverterUtil.convertToType(values, expectedType);
        }
        else {
            return (T)values;
        }
    }
}
