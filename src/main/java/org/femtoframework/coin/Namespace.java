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

import org.femtoframework.bean.NamedBean;
import org.femtoframework.coin.spec.BeanSpecFactory;

/**
 * Namespace
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public interface Namespace extends NamedBean {

    /**
     * Namespace
     *
     * @return Namespace's name
     */
    String getName();


    /**
     * Component Factory under this namespace
     *
     * @return Component Factory
     */
    ComponentFactory getComponentFactory();

    /**
     * Bean Factory under this namespace;
     *
     * @return BeanFactory
     */
    BeanFactory getBeanFactory();

    /**
     * Bean Spec Factory under this namespace;
     *
     * @return BeanSpecFactory
     */
    BeanSpecFactory getBeanSpecFactory();
}
