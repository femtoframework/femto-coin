package org.femtoframework.coin.spec;

import org.femtoframework.coin.Component;

import java.lang.reflect.Type;

/**
 * Element
 *
 * bean, has a must have field "class" which indicates the kind of this bean.
 * map, if there is no "class" specified in a map structure, that means it is a map
 * list, multiple values
 * string, text value
 * int,  Integer
 * double, Any Float or Double
 *
 * @author fengyun
 * @version 1.00 2011-04-28 08:56
 */
public interface Element
{
    /**
     * Element Type
     *
     * @return Element Type
     */
    Kind getKind();

    /**
     * Class of the spec, for example "java.util.ArrayList"
     * For all other spec except "Bean" are certain class.
     *
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
    String getKindClass();

    /**
     * Return the value of this spec definition
     *
     * @param expectedType Expected kind
     * @param genericType
     * @param parentComponent  Component
     * @return the value
     */
    <T> T getValue(Class<T> expectedType, Type genericType, Component parentComponent);

    /**
     * Start visiting this spec
     *
     * @param visitor Visitor
     */
    void accept(ElementVisitor visitor);
}
