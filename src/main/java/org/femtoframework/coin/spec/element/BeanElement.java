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

import org.femtoframework.coin.BeanContext;
import org.femtoframework.coin.BeanFactory;
import org.femtoframework.coin.spec.BeanSpec;
import org.femtoframework.coin.spec.Element;
import org.femtoframework.coin.spec.CoreKind;
import org.femtoframework.coin.spec.SpecConstants;
import org.femtoframework.lang.reflect.NoSuchClassException;
import org.femtoframework.lang.reflect.Reflection;
import org.femtoframework.util.DataUtil;
import org.femtoframework.util.convert.ConverterUtil;

import java.util.List;
import java.util.Map;

/**
 * Bean Element
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public class BeanElement extends MapElement<Element> implements BeanSpec, SpecConstants {

    public BeanElement() {
        super(CoreKind.BEAN);
    }

    public BeanElement(Map map) {
        super(map);
        setKind(CoreKind.BEAN);
    }

    public BeanElement(String name, Class<?> implClass) {
        put(_NAME, new PrimitiveElement<>(CoreKind.STRING, name));
        put(_TYPE, new PrimitiveElement<>(CoreKind.STRING, implClass.getName()));
        this.typeClass = implClass;
    }

    private transient Class<?> typeClass;

    /**
     * Version
     */
    public String getVersion() {
        return getString(_VERSION, BeanSpec.super.getVersion());
    }

    /**
     * Return aliases
     *
     * @return aliases
     */
    public List<String> getAliases() {
        return DataUtil.getStringList(getValue(_ALIASES), BeanSpec.super.getAliases());
    }

    /**
     * Return belongsTo
     *
     * belongsTo syntax
     *
     * @return
     */
    public List<String> getBelongsTo() {
        return DataUtil.getStringList(getValue(_BELONGS_TO), BeanSpec.super.getBelongsTo());
    }

    /**
     * Indicate the kind of this bean
     *
     * @return
     */
    public String getType() {
        return getString(_TYPE, null);
    }

    /**
     * The real class of the kind
     *
     * @return kind class
     */
    @Override
    public Class<?> getTypeClass() {
        String kind = getType();
        if (typeClass == null) {
            if (kind != null) {
                if (typeClass == null) {
                    try {
                        typeClass = Reflection.getClass(kind);
                    } catch (ClassNotFoundException e) {
                        throw new NoSuchClassException("No such kind:" + kind + " in spec:" + getName());
                    }
                }
            }
        }
        return typeClass;
    }


    protected Object getValue(String key) {
        Element element = get(key);
        if (element != null) {
            return element.getValue(null, null);
        }
        return null;
    }

    protected String getString(String key, String defaultValue) {
        return DataUtil.getString(getValue(key), defaultValue);
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
     * Return the value of this element definition
     *
     * @param expectedType Expected kind
     * @param context      Bean context
     * @return the value
     */
    public <T> T getValue(Class<T> expectedType, BeanContext context) {
//        Map values = new ParametersMap();
//        for(Map.Entry<String, Element> entry: entrySet()) {
//            String key = entry.getKey();
//            if (!key.startsWith("_")) {
//                values.put(, entry.getValue().getValue(null, context));
//            }
//        }
//
//        return ConverterUtil.convertToType(values, expectedType);
        BeanFactory factory = context.getCurrentBeanFactory();
        Object obj = factory.create(getName(), this, context.getTargetStage());
        return ConverterUtil.convertToType(obj, expectedType);
    }

    /**
     * Is it singleton?
     * If it is true, then coin will try to create it in singleton pattern first.
     *
     * @return Singleton
     */
    public boolean isSingleton() {
        return DataUtil.getBoolean(getValue(_SINGLETON), BeanSpec.super.isSingleton());
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
