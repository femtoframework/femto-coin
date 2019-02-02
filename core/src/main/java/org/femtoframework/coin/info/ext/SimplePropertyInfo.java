package org.femtoframework.coin.info.ext;

import org.femtoframework.coin.annotation.Ignore;
import org.femtoframework.coin.annotation.Property;
import org.femtoframework.coin.info.AbstractFeatureInfo;
import org.femtoframework.coin.info.BeanNamingConvension;
import org.femtoframework.coin.info.PropertyInfo;
import org.femtoframework.lang.reflect.Reflection;
import org.femtoframework.text.NamingConvention;
import org.femtoframework.util.StringUtil;
import org.femtoframework.util.convert.ConverterUtil;

import java.lang.reflect.Field;
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
 * 2. With @Ignore
 *                                       name     getter       readable        setter      writable
 *    getXxx only + Ignore               xxx      getXxx       false            ""         false
 *    setXxx only + Ignore               xxx      ""           false           setXxx      false
 *    getXxx + setXxx  IgnoreOnGetter    xxx      getXxx       false           setXxx      true
 *    getXxx + setXxx  IgnoreOnSetter    xxx      getXxx       true            setXxx      false
 *
 * @Setter or @Getter can redefined the getter or setter method
 * @Property provides information for the property
 * @Description provides the description for the property
 * @Pojo is only supported on BeanClass, it defined the order of properties, it is lower priority than the index on Property.
 *       That means Coined assigns indexes to each properties, then it could be redefined by @Property
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public class SimplePropertyInfo extends AbstractFeatureInfo implements PropertyInfo {
    private String type;

    private boolean readable = true;
    private boolean writable = true;

    private String getter = null;
    private String setter = null;

    @Ignore
    private transient Method getterMethod = null;

    @Ignore
    private transient Method setterMethod = null;

    private String defaultValue = null;
    private int index = Property.INDEX_UNKNOWN;
    private boolean required = false;

    protected static String getType(String type) {
        if (StringUtil.isValid(type)) {
            return type;
        }
        else {
            return null;
        }
    }

    public SimplePropertyInfo(Property property, Field field) {
        setProperty(property, field);
    }

    /**
     * Constructs an <CODE>PropertyInfo</CODE> object.
     *
     * @param name        The name of the attribute.
     * @param type        The type or class name of the attribute.
     */
    public SimplePropertyInfo(String name,
                              String type) {
        super(name, "");
        setType(type);
    }

    public void setName(String name) {
        BeanNamingConvension.mustBeValidJavaIdentifier(name);

        super.setName(name);
        if (getter == null && getType() != null) {
            this.getter = NamingConvention.toGetter(name, getType());
        }
        if (setter == null) {
            this.setter = NamingConvention.toSetter(name);
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
        return writable;
    }

    /**
     * Return the getter method, default is "getXxx" or "isXxx"
     * But it could be redefined by "@JsonGetter"
     *
     * @see org.femtoframework.coin.annotation.Getter
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
     * @see org.femtoframework.coin.annotation.Setter
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
            Method setterMethod = this.setterMethod;
            if (setterMethod == null) {
                this.setterMethod = setterMethod = Reflection.getMethod(bean.getClass(), getSetter());
            }
            Reflection.invoke(bean, setterMethod, value);
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
            Method getterMethod = this.getterMethod;
            if (getterMethod == null) {
                this.getterMethod = getterMethod = Reflection.getMethod(bean.getClass(), getGetter());
            }
            Object value = Reflection.invoke(bean, getterMethod, null);
            return value != null ? (T)value : getExpectedDefaultValue();
        }
    }

    /**
     * Default value defined in String
     *
     * @see Property
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
    @Ignore
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
     * Set the properties from Property
     *
     * @param property Property
     */
    @Ignore
    public void setProperty(Property property, Field field) {
        if (StringUtil.isInvalid(type) && field != null) {
            setType(field.getType().getName());
        }

        String name = property.value();
        if (StringUtil.isInvalid(name) && field != null) {
            name = field.getName();
        }

        setName(name);

        defaultValue = property.defaultValue();
        setIndex(property.index());
        this.required = property.required();
        this.readable = property.readable();
        this.writable = property.writable();
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setReadable(boolean readable) {
        this.readable = readable;
    }

    public void setWritable(boolean writable) {
        this.writable = writable;
    }

    @Ignore
    public void setGetterMethod(Method getterMethod) {
        this.getterMethod = getterMethod;
        this.getter = getterMethod.getName();
    }

    @Ignore
    public void setSetterMethod(Method setterMethod) {
        this.setterMethod = setterMethod;
        this.setter = setterMethod.getName();
    }

    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * Property Info
     *
     * @return Property Info
     */
    public String toString() {
        return "{name:" + getName() + ",type:" + getType() + ", readable:" + readable
                + ",writable:" + writable + ",getter:" + getter + ",setter:" + setter
                + ",index:" + index + ",required:" + required + ",defaultValue:" + defaultValue + "}";
    }
}
