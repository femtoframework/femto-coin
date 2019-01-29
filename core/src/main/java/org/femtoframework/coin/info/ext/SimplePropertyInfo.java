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
package org.femtoframework.coin.info.ext;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.femtoframework.coin.info.AbstractFeatureInfo;
import org.femtoframework.coin.info.PropertyInfo;
import org.femtoframework.lang.reflect.Reflection;
import org.femtoframework.text.NamingConvention;
import org.femtoframework.util.convert.ConverterUtil;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Simple Property Info implementation
 *
 * 1. No any annotations
 *                     name     getter       readable        setter      writable
 *    getXxx only      xxx      getXxx       true            ""          false
 *    setXxx only      xxx      ""           false           setXxx      true
 *    getXxx + setXxx  xxx      getXxx       true            setXxx      true
 *
 *
 * 2. With @JsonIgnore
 *                                       name     getter       readable        setter      writable
 *    getXxx only + JsonIgnore           xxx      getXxx       false            ""         false
 *    setXxx only + JsonIgnore           xxx      ""           false           setXxx      false
 *    getXxx + setXxx  IgnoreOnGetter    xxx      getXxx       false           setXxx      true
 *    getXxx + setXxx  IgnoreOnSetter    xxx      getXxx       true            setXxx      false
 *
 * JsonSetter or JsonGetter can redefined the getter or setter method
 * JsonProperty provides information for the property
 * JsonPropertyDescription provides the description for the property
 * JsonPropertyOrder is only supported on BeanClass, it defined the order of properties, it is lower priority than the index on JsonProperty.
 *                   That means JsonPropertyOrder assigns indexes to each properties, then it could be redefined by @JsonProperty
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public class SimplePropertyInfo extends AbstractFeatureInfo implements PropertyInfo {
    private String type;

    private boolean readable = true;
    private boolean writeable = true;

    private String getter = null;
    private String setter = null;

    private String defaultValue = null;
    private int index = JsonProperty.INDEX_UNKNOWN;
    private boolean required = false;


    public void setName(String name) {
        super.setName(name);
        if (getGetter() == null && getType() != null) {
            setGetter(NamingConvention.toGetter(name, getType()));
        }
        if (getSetter() == null) {
            setSetter(NamingConvention.toSetter(name));
        }
    }

    /**
     * Is the property has getter?
     *
     * @return Is the property readable
     */
    public boolean isReadable() {
        return readable;
    }

    /**
     * Is the property has setter?
     *
     * @return Is the property writable
     */
    public boolean isWritable() {
        return writeable;
    }

    /**
     * Return the getter method, default is "getXxx" or "isXxx"
     * But it could be redefined by "@JsonGetter"
     *
     * @see com.fasterxml.jackson.annotation.JsonGetter
     *
     * @return The real getter method name
     */
    public String getGetter() {
        return getter == null ? PropertyInfo.super.getGetter() : getter;
    }

    /**
     * Return the setter method, default is "setXxx"
     * It could be redefined by "@JsonSetter"
     *
     * @see com.fasterxml.jackson.annotation.JsonSetter
     *
     * @return the real setter method name
     */
    public String getSetter() {
        return setter == null ? PropertyInfo.super.getSetter() : setter;
    }

    /**
     * Invoke setter method to set the value
     *
     * @param bean  Bean
     * @param value Value
     */
    @Override
    public void invokeSetter(Object bean, Object value) {
        if (bean instanceof Map) {
            ((Map)bean).put(getName(), value);
        }
        else {
            String setter = getSetter();
            Method method = Reflection.getMethod(bean.getClass(), setter);
            Reflection.invoke(bean, method, value);
        }
    }

    /**
     * Invoke getter method to get the value
     *
     * @param bean Bean
     * @return property value
     */
    @Override
    public <T> T invokeGetter(Object bean) {
        if (bean instanceof Map) {
            return (T)((Map)bean).getOrDefault(getName(), getExpectedDefaultValue());
        }
        else {
            String getter = getGetter();
            Method method = Reflection.getMethod(bean.getClass(), getter);
            Object value = Reflection.invoke(bean, method, null);
            return value != null ? (T)value : getExpectedDefaultValue();
        }
    }

    /**
     * Default value defined in String
     *
     * @see com.fasterxml.jackson.annotation.JsonProperty
     */
    @Override
    public String getDefaultValue() {
        return defaultValue;
    }

    /**
     * Get default value as expected type
     *
     * @return default value as expected type
     */
    @Override
    @JsonIgnore
    public <T> T getExpectedDefaultValue() {
        return ConverterUtil.convertToType(defaultValue, getType());
    }

    @Override
    public String getType() {
        return type;
    }

    /**
     * Index of this property
     *
     * @return Index of this property, for ProtocolBuf or other schema enabled serialization
     */
    @Override
    public int getIndex() {
        return index;
    }

    /**
     * Is required or not
     *
     * @return is the properties a must have property? if so when the property is not set during deserialization, deserializer returns a validation exception.
     */
    @Override
    public boolean isRequired() {
        return required;
    }

    /**
     * Set the properties from JsonProperty
     *
     * @param jsonProperty JsonProperty
     */
    @JsonIgnore
    public void setJsonProperty(JsonProperty jsonProperty) {
        setName(jsonProperty.value());
        defaultValue = jsonProperty.defaultValue();
        setIndex(jsonProperty.index());
        this.required = jsonProperty.required();
        JsonProperty.Access access = jsonProperty.access();
        switch (access) {
            case READ_ONLY:
                readable = true;
                writeable = false;
                break;
            case WRITE_ONLY:
                readable = false;
                writeable = true;
                break;
            case READ_WRITE:
                readable = true;
                writeable = true;
                break;
            case AUTO:

        }
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setReadable(boolean readable) {
        this.readable = readable;
    }

    public boolean isWriteable() {
        return writeable;
    }

    public void setWriteable(boolean writeable) {
        this.writeable = writeable;
    }

    public void setGetter(String getter) {
        this.getter = getter;
    }

    public void setSetter(String setter) {
        this.setter = setter;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
