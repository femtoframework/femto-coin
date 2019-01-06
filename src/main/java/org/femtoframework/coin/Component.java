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
package org.femtoframework.coin;

import org.femtoframework.bean.NamedBean;
import org.femtoframework.coin.exception.NoSuchNamespaceException;
import org.femtoframework.coin.spec.BeanSpec;
import org.femtoframework.coin.status.BeanStatus;

/**
 * Component
 *
 * A component includes spec, bean, and lifecycle status.
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public interface Component extends NamedBean {
    /**
     * Namespace of this component
     *
     * @return Namespace
     */
    default String getNamespace() {
        BeanSpec spec = getSpec();
        return spec != null ? spec.getNamespace() : null;
    }

    /**
     * Name of this bean
     *
     * @return Name of the bean and component
     */
    default String getName() {
        BeanSpec spec = getSpec();
        return spec != null ? spec.getName() : null;
    }

    /**
     * Bean Spec
     *
     * @return BeanSpec
     */
    BeanSpec getSpec();

    /**
     * Qualified Name
     *
     * @return Qualified Name
     */
    default String getQualifiedName() {
        return getNamespace() + ":" + getName();
    }

    /**
     *
     * Get Namespace object by name
     *
     * @param namespace Namespace name, if it is null or empty string, return the current namespace object
     * @return Namespace object
     * @throws org.femtoframework.coin.exception.NoSuchNamespaceException if the namespace doesn't exist
     */
    default Namespace getNamespaceByName(String namespace) {
        if (namespace == null || namespace.isEmpty()) {
            return getCurrentNamespace();
        }
        Namespace ns = getNamespaceFactory().get(namespace);
        if (ns == null) {
            throw new NoSuchNamespaceException("No such namespace:" + namespace);
        }
        return ns;
    }

    /**
     * Return current namespace
     *
     * @return Current Namespace
     */
    Namespace getCurrentNamespace();

    /**
     * Bean Factory
     *
     * @return Current Bean Factory
     */
    default BeanFactory getCurrentBeanFactory() {
        return getCurrentNamespace().getBeanFactory();
    }

    /**
     * Component Factory
     *
     * @return Current Component Factory
     */
    default ComponentFactory getCurrentComponentFactory() {
        return getCurrentNamespace().getComponentFactory();
    }

    /**
     * Return current bean
     *
     * @return current bean
     */
    Object getBean();

    /**
     * Bean Status
     *
     * @return BeanStatus
     */
    BeanStatus getStatus();

    /**
     * Return expected Stage
     *
     * @return expected Stage
     */
    BeanStage getStage();

    /**
     * Set the target bean stage, that means the component will be executed to given stage
     */
    void setStage(BeanStage stage);

    /**
     * Return the right bean with expectedType
     *
     * @param expectedType ExpectedType, if the is null, that means returning the bean implementation object anyway
     * @return Bean
     */
    <T> T getBean(Class<T> expectedType);

    /**
     * Return namespace factory
     *
     * @return namespace factory
     */
    NamespaceFactory getNamespaceFactory();
}
