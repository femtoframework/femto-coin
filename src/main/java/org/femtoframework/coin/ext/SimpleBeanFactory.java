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
import org.femtoframework.coin.BeanStage;
import org.femtoframework.coin.ComponentFactory;
import org.femtoframework.coin.spec.BeanSpec;
import org.femtoframework.coin.spec.BeanSpecFactory;

/**
 * Simple Bean Factory
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public class SimpleBeanFactory extends BaseFactory<Object> implements BeanFactory {

    private BeanSpecFactory specFactory;
    private ComponentFactory componentFactory;


    public SimpleBeanFactory(SimpleComponentFactory componentFactory) {
        this.componentFactory = componentFactory;
        this.specFactory = componentFactory.getSpecFactory();
        setNamespace(componentFactory.getNamespace());
    }

    /**
     * Return object by given name
     *
     * @param name   Name
     * @param create Create object automatically
     * @return object in this factory
     */
    @Override
    public Object get(String name, boolean create) {
        return null;
    }

    /**
     * Create Object by BeanSpec
     *
     * @param name Bean Name
     * @param spec Bean Spec
     * @param stage BeanStage
     */
    @Override
    public Object create(String name, BeanSpec spec, BeanStage stage) {
        return null;
    }

    public BeanSpecFactory getSpecFactory() {
        return specFactory;
    }

    public ComponentFactory getComponentFactory() {
        return componentFactory;
    }
}
