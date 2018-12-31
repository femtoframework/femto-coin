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
package org.femtoframework.coin.ext;

import org.femtoframework.coin.BeanFactory;
import org.femtoframework.coin.ComponentFactory;
import org.femtoframework.coin.Namespace;
import org.femtoframework.coin.spec.BeanSpecFactory;

/**
 * Simple Namespace
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public class SimpleNamespace implements Namespace {

    private String name;

    private BeanSpecFactory specFactory;
    private SimpleComponentFactory componentFactory;
    private BeanFactory beanFactory;

    public SimpleNamespace(String name) {
        this.name = name;
        this.specFactory = new SimpleBeanSpecFactory(name);
        this.componentFactory = new SimpleComponentFactory(specFactory);
        this.beanFactory = new SimpleBeanFactory(componentFactory);
    }

    /**
     * Namespace
     *
     * @return Namespace's name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Component Factory under this namespace
     *
     * @return Component Factory
     */
    @Override
    public ComponentFactory getComponentFactory() {
        return componentFactory;
    }

    /**
     * Bean Factory under this namespace;
     *
     * @return BeanFactory
     */
    @Override
    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    /**
     * Bean Spec Factory under this namespace;
     *
     * @return BeanSpecFactory
     */
    @Override
    public BeanSpecFactory getBeanSpecFactory() {
        return specFactory;
    }
}
