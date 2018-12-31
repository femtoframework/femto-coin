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

import org.femtoframework.coin.BeanStage;
import org.femtoframework.coin.Component;
import org.femtoframework.coin.ComponentFactory;
import org.femtoframework.coin.spec.BeanSpecFactory;

/**
 * Simple Component Factory
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public class SimpleComponentFactory extends BaseFactory<Component> implements ComponentFactory {

    private BeanSpecFactory specFactory;


    public SimpleComponentFactory(BeanSpecFactory specFactory) {
        this.specFactory = specFactory;
        setNamespace(specFactory.getNamespace());
    }
    /**
     * Create component on-demand
     *
     * @param name        Component name, the name could be null, if it is null, the created object won'be
     * @param implClass   Implement class
     * @param targetStage TargetStage, since the required implementation should have same stage with the parent bean
     * @return
     */
    @Override
    public Component create(String name, Class implClass, BeanStage targetStage) {
        return null;
    }

    /**
     * Get the default implementation by interface class
     *
     * @param name           Bean Name, it could be null
     * @param interfaceClass Interface Class
     * @return The implement class, return null, if it is not able to find a right implementation
     */
    @Override
    public Class<?> getImplement(String name, Class<?> interfaceClass) {
        return null;
    }

    public BeanSpecFactory getSpecFactory() {
        return specFactory;
    }
}
