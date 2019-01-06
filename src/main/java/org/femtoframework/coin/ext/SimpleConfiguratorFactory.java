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

import org.femtoframework.bean.Initializable;
import org.femtoframework.bean.NamedBean;
import org.femtoframework.bean.exception.InitializeException;
import org.femtoframework.coin.*;
import org.femtoframework.implement.ImplementUtil;
import org.femtoframework.lang.reflect.Reflection;

import java.util.Iterator;

/**
 * Simple Configurator Factory
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public class SimpleConfiguratorFactory extends BaseFactory<Configurator> implements ConfiguratorFactory, Initializable {

    protected SimpleConfiguratorFactory() {
        super(null, CoinConstants.NAMESPACE_CONFIGURQTOR);

    }

    /**
     * Configure the bean
     *
     * @param component Component
     */
    @Override
    public void configure(Component component) {
        for(Configurator configurator: this) {
            configurator.configure(component);
        }
    }

    /**
     * Initialize the bean
     *
     * @throws InitializeException
     */
    @Override
    public void initialize() {
        Iterator<Class<? extends Configurator>> implementations = ImplementUtil.getImplements(Configurator.class);
        while(implementations.hasNext()) {
            Class<? extends Configurator> clazz = implementations.next();
            Configurator configurator = Reflection.newInstance(clazz);
            if (configurator instanceof NamedBean) {
                add((NamedBean)configurator);
            }
            else {
                add(clazz.getSimpleName(), configurator);
            }
        }
    }
}
