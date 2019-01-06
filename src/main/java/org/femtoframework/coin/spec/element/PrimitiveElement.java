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

import com.fasterxml.jackson.annotation.JsonValue;
import org.femtoframework.coin.Component;
import org.femtoframework.coin.spec.CoreKind;
import org.femtoframework.coin.spec.PrimitiveSpec;
import org.femtoframework.util.convert.ConverterUtil;

/**
 * PrimitiveSpec Element
 *
 * @author Sheldon Shao
 * @version 1.0
 */

public class PrimitiveElement<E> extends AbstractElement implements PrimitiveSpec<E> {

    private E primitiveValue;

    public PrimitiveElement(CoreKind type, E value) {
        setKind(type);
        this.primitiveValue = value;
        if (value != null) {
            setKindClass(value.getClass().getName());
        }
    }

    /**
     * Return the value of this element definition
     *
     * @param expectedType Expected kind
     * @param component    Component
     * @return the value
     */
    public <T> T getValue(Class<T> expectedType, Component component) {
        if (kind == CoreKind.NULL) {
            return null;
        }
        if (expectedType == null) {
            return (T)primitiveValue;
        }
        else {
            return ConverterUtil.convertToType(primitiveValue, expectedType);
        }
    }

    public String toString() {
        return String.valueOf(primitiveValue);
    }

    @JsonValue
    public E getPrimitiveValue() {
        return primitiveValue;
    }

    public void setPrimitiveValue(E value) {
        this.primitiveValue = value;
    }
}
