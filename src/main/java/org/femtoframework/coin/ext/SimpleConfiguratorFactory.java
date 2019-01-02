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

import org.femtoframework.coin.BeanContext;
import org.femtoframework.coin.Configurator;
import org.femtoframework.coin.ConfiguratorFactory;

/**
 * Simple Configurator Factory
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public class SimpleConfiguratorFactory extends BaseFactory<Configurator> implements ConfiguratorFactory {

    private static SimpleConfiguratorFactory instance = new SimpleConfiguratorFactory();

    public static ConfiguratorFactory getInstance() {
        return instance;
    }

    /**
     * Configure the bean
     *
     * @param context BeanContext
     */
    @Override
    public void configure(BeanContext context) {
        for(Configurator configurator: this) {
            configurator.configure(context);
        }
    }
}
