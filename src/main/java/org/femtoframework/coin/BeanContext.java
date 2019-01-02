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

import org.femtoframework.coin.exception.NoSuchNamespaceException;
import org.femtoframework.coin.spec.BeanSpec;

/**
 * Bean lifecycle context
 *
 * @author fengyun
 * @version 1.00 2005-2-4 12:46:07
 */
public interface BeanContext {

    /**
     * Target Stage, How to deal with all the object on the object tree
     */
    BeanStage getTargetStage();

    /**
     * Return namespace of this object
     *
     * @return Namespace
     */
    default String getNamespace() {
        return getSpec().getNamespace();
    }

    /**
     * The name of current bean
     *
     * @return Current name
     */
    default String getName() {
        return getSpec().getName();
    }

    /**
     * Set bean name
     *
     * @param name bean name
     */
    void setName(String name);

    /**
     * Qualified Name
     *
     * @return Qualified Name
     */
    default String getQualifiedName() {
        return getNamespace() + ":" + getName();
    }

    /**
     * Return current bean
     *
     * @return current bean
     */
    Object getBean();


    /**
     * Return bean spec of this bean
     *
     * @return bean spec of this bean
     */
    BeanSpec getSpec();

    /**
     * Bean Factory
     *
     * @return Current Bean Factory
     */
    default BeanFactory getCurrentBeanFactory() {
        return getCurrentNamespace().getBeanFactory();
    }

    /**
     * Return current namespace
     *
     * @return Current Namespace
     */
    Namespace getCurrentNamespace();

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
     * Return namespace factory
     *
     * @return namespace factory
     */
    NamespaceFactory getNamespaceFactory();
}
