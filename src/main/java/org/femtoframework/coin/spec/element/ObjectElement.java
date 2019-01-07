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

import org.femtoframework.coin.spec.Element;
import org.femtoframework.coin.spec.Kind;
import org.femtoframework.coin.spec.ObjectSpec;
import org.femtoframework.coin.spec.SpecConstants;
import org.femtoframework.util.DataUtil;

import java.util.Map;

/**
 * Abstract Object Element
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public abstract class ObjectElement extends MapElement<Element> implements ObjectSpec, SpecConstants {

    public ObjectElement() {
    }

    public ObjectElement(Map map) {
        super(map);
    }

    protected ObjectElement(Kind kind) {
        super(kind);
    }

    /**
     * Name of the object
     *
     * @return Name of the object
     */
    @Override
    public String getName() {
        String name = getString(_NAME, null);
        if (name == null) {
            name = getString(NAME, null);
        }
        return name;
    }


    public static Object getValue(Map map, String key) {
        Element element = (Element)map.get(key);
        if (element != null) {
            return element.getValue(null, null);
        }
        return null;
    }

    protected Object getValue(String key) {
        return getValue(this, key);
    }

    protected String getString(String key, String defaultValue) {
        return DataUtil.getString(getValue(key), defaultValue);
    }

}
