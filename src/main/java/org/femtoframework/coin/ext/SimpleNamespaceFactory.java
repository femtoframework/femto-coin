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

import org.femtoframework.coin.*;

/**
 * Namespace Factory implementation
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public class SimpleNamespaceFactory extends BaseFactory<Namespace> implements NamespaceFactory {

    private LifecycleStrategy strategy;

    private CoinModule module;

    public SimpleNamespaceFactory(CoinModule module, LifecycleStrategy strategy) {
        super(null, CoinConstants.NAMESPACE_NAMESPACE);
        this.strategy = strategy;
        this.module = module;
        setNamespaceFactory(this);
        createNamespace(CoinConstants.NAMESPACE_NAMESPACE);
        createNamespace(CoinConstants.NAMESPACE_COIN);
        createNamespace(CoinConstants.DEFAULT_NAMESPACE);
    }

    /**
     * Get namespace with given name
     *
     * @param name Name
     * @return
     */
    @Override
    public Namespace createNamespace(String name) {
        if (name.length() < 2) {
            throw new IllegalArgumentException("Namespace has to be more than 2 characters");
        }
        SimpleNamespace ns = new SimpleNamespace(name, module, strategy);
        add(ns);
        return ns;
    }

    /**
     * Get namespace with given name
     *
     * @param name       Name
     * @param createAuto true, Create the namespace automatically if it does not exist
     * @return
     */
    @Override
    public Namespace getNamespace(String name, boolean createAuto) {
        Namespace ns = get(name);
        if (ns == null && createAuto) {
            ns = createNamespace(name);
        }
        return ns;
    }
}
