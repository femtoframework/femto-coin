/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
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
package org.femtoframework.coin.spec;

import org.femtoframework.coin.BeanContext;

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
     * Class of the element, for example "java.util.ArrayList"
     * For all other element except "Bean" are certain class.
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
     * Return the value of this element definition
     *
     * @param expectedType Expected kind
     * @param context      Bean context
     * @return the value
     */
    <T> T getValue(Class<T> expectedType, BeanContext context);

    /**
     * Start visiting this spec
     *
     * @param visitor Visitor
     */
    void accept(ElementVisitor visitor);
}
