package org.femtoframework.coin.spec.types;

import org.femtoframework.coin.BeanContext;
import org.femtoframework.coin.spec.*;

import java.util.LinkedHashMap;
import java.util.Map;


/**
 * Map Element
 *
 * @author fengyun
 * @version 1.00 2011-04-28 13:04
 */
public class MapElement<E extends Element> extends LinkedHashMap<String, E>
    implements MapSpec<E>
{

    private ElementType type = ElementType.MAP;

    public MapElement() {
    }

    public MapElement(Map map) {
        super(map);
    }

    protected MapElement(ElementType type) {
        this.type = type;
    }

    /**
     * Element Type
     *
     * @return Element Type
     */
    @Override
    public ElementType getType() {
        return type;
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
        return null;
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

    public void setType(ElementType type) {
        this.type = type;
    }
}
