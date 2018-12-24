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

/**
 * Element Type
 *
 * bean, it must have a "class" field which indicates the type of this bean
 * map, if there is no "class" specified in a map structure, that means it is a map
 * list, multiple components
 * string, text value
 * int,  Integer
 * long, LONG
 * double, Any Float or Double
 * boolean, True of False
 *
 * in yaml
 *
 * bool, Boolean
 * float, Tags, Floating Point
 * int, Tags, Integer
 * map, Tags, Generic Mapping
 * null, Tags, Empty Nodes, Null
 * seq, Tags, Generic Sequence
 * str, Tags, Generic String
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public enum ElementType {
    /**
     * Null
     */
    NULL(true),

    /**
     * Text value, "str" in yaml
     */
    STRING(true),

    /**
     * Integer,  "int" in yaml
     */
    INT(true),

    /**
     * Long
     */
    LONG(true),

    /**
     * Float value, "float" in yaml
     */
    DOUBLE(true),

    /**
     * Boolean, "bool" in yaml
     */
    BOOLEAN(true),

    /**
     * Basic object in yaml
     */
    MAP(false),

    /**
     * multiple components, "seq" in yaml
     */
    LIST(false),

    /**
     * An extension of MAP
     *
     * It must have a "class" which indicates the type of this bean
     */
    BEAN(false),

    /**
     * VAR
     *
     * @param primitive
     */
    VAR(false);

    private boolean primitive;

    private ElementType(boolean primitive) {
        this.primitive = primitive;
    }

    public boolean isPrimitive() {
        return primitive;
    }
}
