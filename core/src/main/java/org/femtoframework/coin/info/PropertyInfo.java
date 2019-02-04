package org.femtoframework.coin.info;

import org.femtoframework.text.NamingConvention;

/**
 * Property Info
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
public interface PropertyInfo extends FeatureInfo, DefaultValued {

    /**
     * The type of the property
     *
     * @return he type of the property
     */
    String getType();


    /**
     * Return the type class
     *
     * @return the type class
     */
    Class<?> getTypeClass();


    /**
     * Index of this property
     *
     * @return Index of this property, for ProtocolBuf or other schema enabled serialization
     */
    int getIndex();


    /**
     * Is required or not
     *
     * @return is the properties a must have property? if so when the property is not set during deserialization, deserializer returns a validation exception.
     */
    boolean isRequired();

    /**
     * Is the property has getter and it is not ignored by @JsonIgnored
     *
     * @return Is the property readable
     */
    default boolean isReadable() {
        return true;
    }

    /**
     * Is the property has setter?
     *
     * @return Is the property writable
     */
    default boolean isWritable() {
        return true;
    }

    /**
     * Is the property has a getter method with name pattern like with "isXxxx"?
     *
     * @return Is boolean style getter?
     */
    default boolean isIs() {
        Class<?> type = getTypeClass();
        return Boolean.class == type || boolean.class == type;
    }

    /**
     * Return the getter method, default is "getXxx" or "isXxx"
     * But it could be redefined by "@JsonGetter"
     *
     * @see org.femtoframework.coin.annotation.Getter
     *
     * @return The real getter method name
     */
    default String getGetter() {
        return NamingConvention.toGetter(getName(), getType());
    }

    /**
     * Return the setter method, default is "setXxx"
     * It could be redefined by "@JsonSetter"
     *
     * @see org.femtoframework.coin.annotation.Setter
     *
     * @return the real setter method name
     */
    default String getSetter() {
        return NamingConvention.toSetter(getName());
    }

    /**
     * Invoke setter method to set the value
     *
     * @param bean Bean
     * @param value Value
     */
    void invokeSetter(Object bean, Object value);

    /**
     * Invoke getter method to get the value
     *
     * @param bean Bean
     * @return property value
     */
    <T> T invokeGetter(Object bean);




}
