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
import org.femtoframework.coin.ComponentFactory;
import org.femtoframework.coin.spec.BeanSpec;
import org.femtoframework.coin.spec.CoreKind;
import org.femtoframework.coin.spec.Element;
import org.femtoframework.coin.spec.SpecConstants;
import org.femtoframework.lang.reflect.NoSuchClassException;
import org.femtoframework.lang.reflect.Reflection;
import org.femtoframework.parameters.ParametersMap;
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
public class BeanElement extends ModelElement implements BeanSpec, SpecConstants {

    public BeanElement() {
        super(CoreKind.BEAN);
    }

    public BeanElement(Map map) {
        super(map);
        setKind(CoreKind.BEAN);
    }

    public BeanElement(String namespace, String name, Class<?> implClass) {
        super(CoreKind.BEAN);
        setNamespace(namespace);
        setName(name);
        setTypeClass(implClass);
    }

    public void setNamespace(String namespace) {
        put(_NAMESPACE, new PrimitiveElement<>(CoreKind.STRING, namespace));
    }

    public void setName(String name) {
        put(_NAME, new PrimitiveElement<>(CoreKind.STRING, name));
    }

    public void setType(String type) {
        put(_TYPE, new PrimitiveElement<>(CoreKind.STRING, type));
    }

    public void setTypeClass(Class<?> implClass) {
        setType(implClass.getName());
        this.typeClass = implClass;
    }

    private transient Class<?> typeClass;

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
        String type = getType();
        if (typeClass == null) {
            if (type != null) {
                if (typeClass == null) {
                    try {
                        typeClass = Reflection.getClass(type);
                    } catch (ClassNotFoundException e) {
                        throw new NoSuchClassException("No such type:" + type + " in spec:" + getName());
                    }
                }
            }
        }
        return typeClass;
    }

    /**
     * Return the value of this element definition
     *
     * @param expectedType Expected kind
     * @param parentComponent    Component
     * @return the value
     */
    public <T> T getValue(Class<T> expectedType, Component parentComponent) {
        Map values = new ParametersMap();
        for(Map.Entry<String, Element> entry: entrySet()) {
            String key = entry.getKey();
            if (!key.startsWith("_")) {
                values.put(key, entry.getValue().getValue(null, parentComponent));
            }
        }

        if (expectedType == null) {
            expectedType = (Class<T>)getTypeClass();
        }

        ComponentFactory factory = parentComponent.getCurrentComponentFactory();
        Component component = factory.create(null, this, parentComponent.getStage());
        return component.getBean(expectedType);
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
