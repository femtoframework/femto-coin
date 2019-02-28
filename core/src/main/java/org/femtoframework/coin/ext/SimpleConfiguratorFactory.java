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

import org.femtoframework.bean.BeanStage;
import org.femtoframework.bean.InitializableMBean;
import org.femtoframework.coin.*;
import org.femtoframework.implement.ImplementUtil;
import org.femtoframework.implement.InstancesFunction;

/**
 * Simple Configurator ResourceFactory
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public class SimpleConfiguratorFactory extends BaseResourceFactory<Configurator> implements ConfiguratorFactory, InitializableMBean {

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
    public void _doInit() {
        ImplementUtil.applyInstances(Configurator.class, (InstancesFunction<String, Configurator>) this::add);
        applyStageToChildren("configurator_", BeanStage.INITIALIZE);
    }
}
