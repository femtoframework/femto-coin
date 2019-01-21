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
package org.femtoframework.coin.spec.var;

import org.femtoframework.coin.Component;
import org.femtoframework.coin.spec.VariableResolver;
import org.femtoframework.coin.spec.VariableSpec;

/**
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public class PropertiesResolver implements VariableResolver {
    /**
     * Resolve variable by spec
     *
     * @param var VariableSpec
     * @param expectedType
     * @param component
     * @return
     */
    @Override
    public <T> T resolve(VariableSpec var, Class<T> expectedType, Component component) {
        return (T)System.getProperty(var.getSuffix());
    }
}
