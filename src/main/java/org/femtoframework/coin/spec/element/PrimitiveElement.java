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
package org.femtoframework.coin.spec.element;

import com.fasterxml.jackson.annotation.JsonValue;
import org.femtoframework.coin.BeanContext;
import org.femtoframework.coin.spec.ElementType;
import org.femtoframework.coin.spec.PrimitiveSpec;

/**
 * PrimitiveSpec Element
 *
 * @author Sheldon Shao
 * @version 1.0
 */

public class PrimitiveElement<E> extends AbstractElement implements PrimitiveSpec<E> {

    private E value;

    public PrimitiveElement(ElementType type, E value) {
        setType(type);
        this.value = value;
        if (value != null) {
            setElementClass(value.getClass().getName());
        }
    }

    /**
     * Return the value of this element definition
     *
     * @param expectedType Expected type
     * @param context      Bean context
     * @return the value
     */
    @Override
    public Object getValue(String expectedType, BeanContext context) {
        if (type == ElementType.NULL) {
            return null;
        }
        //TODO
        return null;
    }

    public String toString() {
        return String.valueOf(value);
    }

    @JsonValue
    public E getValue() {
        return value;
    }

    public void setValue(E value) {
        this.value = value;
    }
}
