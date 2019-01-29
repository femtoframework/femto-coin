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

import org.femtoframework.coin.info.BeanInfo;
import org.femtoframework.coin.info.PropertyInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Simple Bean Info
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public class SimpleBeanInfo implements BeanInfo {
    private String className;
    private String description;
    private List<PropertyInfo> infos = new ArrayList<>(2);

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
        return infos;
    }

    /**
     * Return property Info by given propertyName
     *
     * @param propertyName Property Name
     * @return PropertyInfo
     */
    @Override
    public PropertyInfo getProperty(String propertyName) {
        return null;
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

        //TODO
//        SimpleBeanInfo simple = (SimpleBeanInfo)beanInfo;
//        Map<String, PropertyInfo> map = simple.properties;
//        if (map != null && !map.isEmpty()) {
//            map = CollectionUtil.clone(map);
//            if (properties == null) {
//                properties = map;
//            }
//            else {
//                map.putAll(properties);
//                properties = map;
//            }
//        }
    }

    /**
     * Return ordered property names
     *
     * @return ordered property names
     */
    @Override
    public Collection<String> getOrderedPropertyNames() {
        return null;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
