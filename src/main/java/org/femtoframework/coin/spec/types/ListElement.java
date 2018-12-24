package org.femtoframework.coin.spec.types;


import org.femtoframework.coin.BeanContext;
import org.femtoframework.coin.spec.Element;
import org.femtoframework.coin.spec.ElementType;
import org.femtoframework.coin.spec.ElementVisitor;
import org.femtoframework.coin.spec.ListSpec;

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
    public ElementType getType() {
        return ElementType.LIST;
    }

    /**
     * Class of the types, for example "java.util.ArrayList"
     * For all other types except "Bean" are certain class.
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
    public String getElementClass() {
        return "java.util.ArrayList";
    }

    /**
     * Return the value of this types definition
     *
     * @param expectedType Expected type
     * @param context      Bean context
     * @return the value
     */
    @Override
    public Object getValue(String expectedType, BeanContext context) {
        return null;
    }

    @Override
    public void accept(ElementVisitor visitor) {

    }
}
