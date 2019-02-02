package org.femtoframework.coin.info.ext;

import org.femtoframework.coin.info.BeanInfo;
import org.femtoframework.coin.info.PropertyInfo;

import java.util.Collection;
import java.util.LinkedHashMap;

/**
 * Simple Bean Info
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public class SimpleBeanInfo implements BeanInfo {
    private String className;
    private String description = "";
    private LinkedHashMap<String, PropertyInfo> infos = new LinkedHashMap<>(2);
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
        return infos.values();
    }

    /**
     * Return property Info by given propertyName
     *
     * @param propertyName Property Name
     * @return PropertyInfo
     */
    @Override
    public PropertyInfo getProperty(String propertyName) {
        return infos.get(propertyName);
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
        LinkedHashMap<String, PropertyInfo> map = simple.infos;
        if (map != null && !map.isEmpty()) {
            map = new LinkedHashMap<>(map);
            if (infos == null) {
                infos = map;
            }
            else {
                map.putAll(infos);
                infos = map;
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
        return infos.keySet();
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

    /**
     * Set Property Infos
     *
     * @param infos
     */
    public void setInfos(LinkedHashMap<String, PropertyInfo> infos) {
        this.infos = infos;
    }

    public void setIgnoreUnknownProperties(boolean ignoreUnknownProperties) {
        this.ignoreUnknownProperties = ignoreUnknownProperties;
    }

    public void setAlphabeticOrder(boolean alphabeticOrder) {
        this.alphabeticOrder = alphabeticOrder;
    }
}
