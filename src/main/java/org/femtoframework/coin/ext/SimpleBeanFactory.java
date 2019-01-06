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

import org.femtoframework.coin.*;

import java.util.Set;

/**
 * Simple Bean Factory
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public class SimpleBeanFactory extends BaseFactory<Object> implements BeanFactory {

    private ComponentFactory componentFactory;

    public SimpleBeanFactory(NamespaceFactory namespaceFactory, SimpleComponentFactory componentFactory) {
        super(namespaceFactory, componentFactory.getNamespace());
        this.componentFactory = componentFactory;
    }

    /**
     * Return all names
     *
     * @return all names
     */
    @Override
    public Set<String> getNames() {
        return componentFactory.getNames();
    }

    /**
     * Return object by given name
     *
     * @param name Name
     * @return object in this factory
     */
    @Override
    public Object get(String name) {
        Object bean = super.get(name);
        if (bean == null) {
            Component component = componentFactory.get(name);
            if (component != null) {
                bean = component.getBean();
                add(name, bean);
            }
        }
        return bean;
    }
}
