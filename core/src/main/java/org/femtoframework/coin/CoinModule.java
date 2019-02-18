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
package org.femtoframework.coin;

import org.femtoframework.annotation.ImplementedBy;
import org.femtoframework.coin.event.BeanEventListeners;
import org.femtoframework.bean.info.BeanInfoFactory;
import org.femtoframework.coin.remote.RemoteGenerator;
import org.femtoframework.coin.spec.KindSpecFactory;
import org.femtoframework.coin.spec.VariableResolverFactory;

/**
 * Coin Module
 *
 * special namespaces
 *
 *
 * @author Sheldon Shao
 * @version 1.0
 */
@ImplementedBy("org.femtoframework.coin.ext.SimpleCoinModule")
public interface CoinModule {

    /**
     * Return namespace factory
     *
     * @return Namespace ResourceFactory
     */
    NamespaceFactory getNamespaceFactory();


    /**
     * Return KindSpecFactory
     *
     * @return KindSpecFactory
     */
    default KindSpecFactory getKindSpecFactory() {
        return null;
    }


    /**
     * VariableResolver ResourceFactory
     *
     * @return VariableResolver ResourceFactory
     */
    default VariableResolverFactory getVariableResolverFactory() {
        return null;
    }

    /**
     * Coin Control, maintain objects by Yaml or JSON
     *
     * @return Coin Control
     */
    CoinController getController();

    /**
     * Coin Lookup
     *
     * @return CoinLookup
     */
    default CoinLookup getLookup() {
        return null;
    }

    /**
     * Remote Generator, for RMI or gRPC extension
     *
     * @return Remote Generator
     */
    default RemoteGenerator getRemoteGenerator() {
        return null;
    }


    /**
     * Return LifecycleStrategy
     *
     * @return LifecycleStrategy
     */
    LifecycleStrategy getLifecycleStrategy();

    /**
     * Return BeanEventListeners
     */
    default BeanEventListeners getBeanEventListeners() {
        return null;
    }

    /**
     * Bean Info factory
     *
     * BeanInfo makes the following things very easy
     * 1. JMX MBean, BeanInfo can help BusinessObject to generate JMX MBean automatically.
     * 2. POJO, BeanInfo helps it to ignore some properties or have different getters or setters.
     * 3. DTO, BeanInfo can use it as decoding specification.
     *
     * @return BeanInfoFactory
     */
    BeanInfoFactory getBeanInfoFactory();
}
