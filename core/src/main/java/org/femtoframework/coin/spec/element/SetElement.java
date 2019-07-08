/**
 * Licensed to the FemtoFramework under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.femtoframework.coin.spec.element;

import org.femtoframework.coin.Component;
import org.femtoframework.coin.spec.CoreKind;
import org.femtoframework.coin.spec.Element;
import org.femtoframework.coin.spec.Kind;
import org.femtoframework.coin.spec.SetSpec;
import org.femtoframework.util.convert.ConverterUtil;

import java.lang.reflect.Type;
import java.util.*;

/**
 * Set
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public class SetElement<E extends Element> extends HashSet<E> implements SetSpec<E> {


    public SetElement(Set set) {
        super(set);
    }

    /**
     * Element Type
     *
     * @return Element Type
     */
    @Override
    public Kind getKind() {
        return CoreKind.SET;
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
        return "java.util.LinkedHashSet";
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
        Set values = new LinkedHashSet(size());
        for(Element element : this) {
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
