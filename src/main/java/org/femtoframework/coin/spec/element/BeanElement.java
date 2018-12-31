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

import org.femtoframework.coin.exception.NoSuchClassException;
import org.femtoframework.coin.spec.BeanSpec;
import org.femtoframework.coin.spec.Element;
import org.femtoframework.coin.spec.ElementType;
import org.femtoframework.coin.spec.SpecConstants;
import org.femtoframework.lang.reflect.Reflection;
import org.femtoframework.util.DataUtil;

import java.util.Map;

/**
 * Bean Element
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public class BeanElement<E extends Element> extends MapElement<E> implements BeanSpec<E>, SpecConstants {

    public BeanElement() {
        super(ElementType.BEAN);
    }

    public BeanElement(Map map) {
        super(map);
        setType(ElementType.BEAN);
    }

    private transient Class<?> kindClass;

    /**
     * Class of the bean
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
    public String getKind() {
        return getString(_KIND, null);
    }

    /**
     * The real class of the kind
     *
     * @return Kind class
     */
    @Override
    public Class<?> getKindClass() {
        String kind = getKind();
        if (kindClass == null) {
            if (kind != null) {
                if (kindClass == null) {
                    try {
                        kindClass = Reflection.getClass(kind);
                    } catch (ClassNotFoundException e) {
                        throw new NoSuchClassException("No such kind:" + kind);
                    }
                }
            }
        }
        return kindClass;
    }


    protected String getString(String key, String defaultValue) {
        return DataUtil.getString(get(key), defaultValue);
    }

    /**
     * Bean Name
     *
     * @return Bean Name
     */
    public String getName() {
        String name = getString(_NAME, null);
        if (name == null) {
            name = getString(NAME, null);
        }
        return name;
    }

    /**
     * Return namespace of current bean spec
     *
     * @return namespace
     */
    public String getNamespace() { //Default is "current" namespace
        return getString(_NAMESPACE, BeanSpec.super.getNamespace());
    }

    /**
     * Whether it is default for the specific interface.
     * If multiple beans set default==true, the last one will be enabled as "default=true" in the factory
     *
     * @return is it the default implementation of the interface
     */
    @Override
    public boolean isDefault() {
        return DataUtil.getBoolean(get(_DEFAULT), BeanSpec.super.isDefault());
    }

    /**
     * Whether the bean is enabled or not.
     *
     * @return
     */
    @Override
    public boolean isEnabled() {
        return DataUtil.getBoolean(get(_ENABLED), BeanSpec.super.isEnabled());
    }
}
