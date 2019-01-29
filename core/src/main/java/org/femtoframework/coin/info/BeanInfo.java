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
package org.femtoframework.coin.info;

import java.util.Collection;

/**
 * Bean Info
 *
 * BeanInfo is to collect the properties information and help to generate JMXBean information, for other configuration management
 *  or generate serialization/deserialization code.
 * We don't want to create new annotations, since jackson has very good annotations for porperties already.
 *
 * Support annotations
 *
 * @see com.fasterxml.jackson.annotation.JsonProperty
 * @see com.fasterxml.jackson.annotation.JsonGetter
 * @see com.fasterxml.jackson.annotation.JsonSetter
 * @see com.fasterxml.jackson.annotation.JsonIgnore
 * @see com.fasterxml.jackson.annotation.JsonPropertyOrder
 * @see com.fasterxml.jackson.annotation.JsonPropertyDescription
 * @see com.fasterxml.jackson.annotation.JsonIgnoreProperties
 *
 * In coin, by default the bean is auto detected
 * @see com.fasterxml.jackson.annotation.JsonAutoDetect
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
}
