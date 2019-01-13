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
package org.femtoframework.coin.ext;

import org.femtoframework.bean.InitializableMBean;
import org.femtoframework.bean.NamedBean;
import org.femtoframework.coin.*;
import org.femtoframework.coin.configurator.GenericConfigurator;
import org.femtoframework.coin.spec.KindSpec;
import org.femtoframework.implement.ImplementConfig;
import org.femtoframework.implement.ImplementManager;
import org.femtoframework.implement.ImplementUtil;
import org.femtoframework.implement.InstancesFunction;
import org.femtoframework.lang.reflect.Reflection;

import java.util.Iterator;
import java.util.Map;

/**
 * Simple Configurator Factory
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public class SimpleConfiguratorFactory extends BaseFactory<Configurator> implements ConfiguratorFactory, InitializableMBean {

    protected SimpleConfiguratorFactory() {
        super(null, CoinConstants.NAMESPACE_COIN);
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

    private boolean initialized = false;

    /**
     * Return whether it is initialized
     *
     * @return whether it is initialized
     */
    @Override
    public boolean isInitialized() {
        return initialized;
    }

    /**
     * Initialized setter for internal
     *
     * @param initialized BeanPhase
     */
    @Override
    public void _doSetInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    /**
     * Initiliaze internally
     */
    @Override
    public void _doInitialize() {
        ImplementUtil.applyInstances(Configurator.class, (InstancesFunction<String, Configurator>) this::add);
    }
}
