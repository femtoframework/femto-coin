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

import org.femtoframework.coin.CoinController;
import org.femtoframework.coin.CoinModule;
import org.femtoframework.coin.NamespaceFactory;
import org.femtoframework.coin.remote.RemoteGenerator;

/**
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public class SimpleCoinModule implements CoinModule {

    private SimpleNamespaceFactory namespaceFactory = new SimpleNamespaceFactory();

    private SimpleCoinController coinControl = new SimpleCoinController();

    /**
     * Return namespace factory
     *
     * @return Namespace Factory
     */
    @Override
    public NamespaceFactory getNamespaceFactory() {
        return namespaceFactory;
    }

    /**
     * Coin Control, maintain objects by Yaml or JSON
     *
     * @return Coin Control
     */
    @Override
    public CoinController getControlller() {
        return coinControl;
    }

    /**
     * Remote Generator, for RMI or gRPC extension
     *
     * @return Remote Generator
     */
    @Override
    public RemoteGenerator getRemoteGenerator() {
        return null;
    }
}
