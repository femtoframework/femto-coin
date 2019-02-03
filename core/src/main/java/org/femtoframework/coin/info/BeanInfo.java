package org.femtoframework.coin.info;

import org.femtoframework.parameters.Parameters;

import java.util.Collection;
import java.util.Set;

/**
 * Bean Info
 *
 * BeanInfo is to collect the properties information and help to generate JMXBean information, for other configuration management
 *  or generate serialization/deserialization code.
 * We don't want to create new annotations, since jackson has very good annotations for porperties already.
 *
 * Support annotations
 *
 * @see org.femtoframework.coin.annotation.Property
 * @see org.femtoframework.coin.annotation.Getter
 * @see org.femtoframework.coin.annotation.Setter
 * @see org.femtoframework.coin.annotation.Ignore
 * @see org.femtoframework.coin.annotation.Description
 *
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public interface BeanInfo {

    /**
     * Return the class name of the bean
     *
     * @return the class name of the bean
     */
    String getClassName();

    /**
     * Set the class name of this bean
     *
     * @param name Class Name
     */
    void setClassName(String name);

    /**
     * Return the description
     *
     * @return the description of this bean
     */
    String getDescription();

    /**
     * Return all properties
     *
     * @return all properties
     */
    Collection<PropertyInfo> getProperties();

    /**
     * Return property Info by given propertyName
     *
     * @param propertyName Property Name
     * @return PropertyInfo
     */
    PropertyInfo getProperty(String propertyName);

    /**
     * Merge the information from supper class BeanInfo
     *
     * @param beanInfo BeanInfo of super class
     */
    void mergeSuper(BeanInfo beanInfo);

    /**
     * Return ordered property names
     *
     * @return ordered property names
     */
    Collection<String> getOrderedPropertyNames();

    /**
     * Ignore unknown properties
     *
     * @return Ignore unknown properties
     */
    boolean isIgnoreUnknownProperties();

    /**
     * Should the properties be in alphabeticOrder order?
     *
     * @return Alphabetic order
     */
    boolean isAlphabeticOrder();

    //Actions

    /**
     * Return all actions
     *
     * @return all actions
     */
    Collection<ActionInfo> getActions();

    /**
     * Return all action names
     *
     * @return all action names
     */
    Set<String> getActionNames();

    /**
     * Return action by name
     *
     * @param name action Name
     * @return ActionInfo, if doesn't exist, it returns <code>null</code>
     */
    ActionInfo getAction(String name);

    /**
     * Convert bean to parameters information.
     * Supports non-structure properties only.
     *
     * @param bean Bean
     * @return
     */
    Parameters toParameters(Object bean);
}
