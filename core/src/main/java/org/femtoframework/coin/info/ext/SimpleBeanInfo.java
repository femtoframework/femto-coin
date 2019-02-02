package org.femtoframework.coin.info.ext;

import org.femtoframework.coin.info.ActionInfo;
import org.femtoframework.coin.info.BeanInfo;
import org.femtoframework.coin.info.PropertyInfo;

import java.util.*;

/**
 * Simple Bean Info
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public class SimpleBeanInfo implements BeanInfo {
    private String className;
    private String description = "";
    private LinkedHashMap<String, PropertyInfo> properties = new LinkedHashMap<>(4);
    private Map<String, ActionInfo> actions = null;

    private boolean ignoreUnknownProperties = false;
    private boolean alphabeticOrder = false;

    /**
     * Return the class name of the bean
     *
     * @return the class name of the bean
     */
    @Override
    public String getClassName() {
        return className;
    }

    /**
     * Set the class name of this bean
     *
     * @param name Class Name
     */
    @Override
    public void setClassName(String name) {
        this.className = name;
    }

    /**
     * Return the description
     *
     * @return the description of this bean
     */
    @Override
    public String getDescription() {
        return description;
    }

    /**
     * Return all properties
     *
     * @return all properties
     */
    @Override
    public Collection<PropertyInfo> getProperties() {
        return properties.values();
    }

    /**
     * Return property Info by given propertyName
     *
     * @param propertyName Property Name
     * @return PropertyInfo
     */
    @Override
    public PropertyInfo getProperty(String propertyName) {
        return properties.get(propertyName);
    }

    /**
     * Merge the information from supper class BeanInfo
     *
     * @param beanInfo BeanInfo of super class
     */
    @Override
    public void mergeSuper(BeanInfo beanInfo) {
        if (description == null) {
            description = beanInfo.getDescription();
        }

        //TODO Reorder
        SimpleBeanInfo simple = (SimpleBeanInfo)beanInfo;
        LinkedHashMap<String, PropertyInfo> map = simple.properties;
        if (map != null && !map.isEmpty()) {
            map = new LinkedHashMap<>(map);
            if (properties == null) {
                properties = map;
            }
            else {
                map.putAll(properties);
                properties = map;
            }
        }

        Map<String, ActionInfo> actions = simple.actions;
        if (actions != null && !actions.isEmpty()) {
            if (this.actions == null) {
                this.actions = actions;
            }
            else { //Puts the parent actions, then puts actions from current bean
                Map<String, ActionInfo> newActions = new HashMap<>(actions);
                newActions.putAll(this.actions);
                this.actions = newActions;
            }
        }
    }

    /**
     * Return ordered property names
     *
     * @return ordered property names
     */
    @Override
    public Collection<String> getOrderedPropertyNames() {
        return properties.keySet();
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Ignore unknown properties
     *
     * @return Ignore unknown properties
     */
    public boolean isIgnoreUnknownProperties() {
        return ignoreUnknownProperties;
    }

    /**
     * Should the properties be in alphabeticOrder order?
     *
     * @return Alphabetic order
     */
    public boolean isAlphabeticOrder() {
        return alphabeticOrder;
    }

    @Override
    public Collection<ActionInfo> getActions() {
        return actions != null ? actions.values() : Collections.emptyList();
    }

    @Override
    public Set<String> getActionNames() {
        return actions != null ? actions.keySet() : Collections.emptySet();
    }

    @Override
    public ActionInfo getAction(String name) {
        return actions != null ? actions.get(name) : null;
    }

    /**
     * Set action infos
     *
     * @param actions Action Infos
     */
    public void setActions(Map<String, ActionInfo> actions) {
        this.actions = actions;
    }

    /**
     * Set Property Infos
     *
     * @param properties
     */
    public void setProperties(LinkedHashMap<String, PropertyInfo> properties) {
        this.properties = properties;
    }

    public void setIgnoreUnknownProperties(boolean ignoreUnknownProperties) {
        this.ignoreUnknownProperties = ignoreUnknownProperties;
    }

    public void setAlphabeticOrder(boolean alphabeticOrder) {
        this.alphabeticOrder = alphabeticOrder;
    }
}
