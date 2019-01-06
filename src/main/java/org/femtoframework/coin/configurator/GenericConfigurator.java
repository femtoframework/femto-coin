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
package org.femtoframework.coin.configurator;

import org.femtoframework.bean.Nameable;
import org.femtoframework.coin.Component;
import org.femtoframework.coin.Configurator;

/**
 * Configure container related stuff,
 * 1. Name
 * 2. Namespace
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public class GenericConfigurator implements Configurator {
    /**
     * Configure the bean
     *
     * @param component Component
     */
    @Override
    public void configure(Component component) {
        Object obj = component.getBean();
        if (obj instanceof Nameable) {
            String name = component.getName();
            if (name != null && !name.isEmpty()) {
                ((Nameable)obj).setName(name);
            }
        }

    }
}
