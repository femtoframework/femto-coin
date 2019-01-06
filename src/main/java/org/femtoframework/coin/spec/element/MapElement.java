package org.femtoframework.coin.spec.element;

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
        Map values = new HashMap(size());
        for(Map.Entry<String, E> entry: entrySet()) {
            values.put(entry.getKey(), entry.getValue().getValue(null, component));
        }
        return ConverterUtil.convertToType(values, expectedType);
    }

    public void setKind(CoreKind kind) {
        this.kind = kind;
    }
}
