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
import org.femtoframework.coin.event.BeanEventListeners;
import org.femtoframework.coin.remote.RemoteGenerator;
import org.femtoframework.coin.spec.KindSpecFactory;
import org.femtoframework.coin.spec.VariableResolverFactory;
import org.femtoframework.coin.spec.ext.SimpleKindSpecFactory;
import org.femtoframework.coin.spec.ext.SimpleVariableResolverFactory;

import static org.femtoframework.coin.CoinConstants.*;

/**
 * Simple Coin Module
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public class SimpleCoinModule implements CoinModule, InitializableMBean {


    private SimpleKindSpecFactory kindSpecFactory = new SimpleKindSpecFactory();

    private SimpleCoinController coinController = new SimpleCoinController();


    private SimpleConfiguratorFactory configuratorFactory = new SimpleConfiguratorFactory();


    private SimpleLifecycleStrategy lifecycleStrategy = new SimpleLifecycleStrategy(configuratorFactory);


    private SimpleNamespaceFactory namespaceFactory = new SimpleNamespaceFactory(this, lifecycleStrategy);


    {
        coinController.setKindSpecFactory(kindSpecFactory);
        coinController.setNamespaceFactory(namespaceFactory);

        configuratorFactory.setNamespaceFactory(namespaceFactory);
        kindSpecFactory.setNamespaceFactory(namespaceFactory);
    }

    private Namespace namespaceCoin;

    private CoinLookup lookup = new SimpleCoinLookup(namespaceFactory);

    private VariableResolverFactory variableResolverFactory = new SimpleVariableResolverFactory(this, namespaceFactory);

    /**
     * Return namespace factory
     *
     * @return Namespace Factory
     *
     */
    @Override
    public NamespaceFactory getNamespaceFactory() {
        return namespaceFactory;
    }

    /**
     * Return KindSpecFactory
     *
     * @return KindSpecFactory
     */
    @Override
    public KindSpecFactory getKindSpecFactory() {
        return kindSpecFactory;
    }

    /**
     * VariableResolver Factory
     *
     * @return VariableResolver Factory
     */
    @Override
    public VariableResolverFactory getVariableResolverFactory() {
        return variableResolverFactory;
    }

    /**
     * Coin Control, maintain objects by Yaml or JSON
     *
     * @return Coin Control
     */
    @Override
    public CoinController getController() {
        return coinController;
    }

    /**
     * Coin Lookup
     *
     * @return CoinLookup
     */
    @Override
    public CoinLookup getLookup() {
        return lookup;
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

    /**
     * Return LifecycleStrategy
     *
     * @return LifecycleStrategy
     */
    @Override
    public LifecycleStrategy getLifecycleStrategy() {
        return lifecycleStrategy;
    }

    /**
     * Return BeanEventListeners
     */
    @Override
    public BeanEventListeners getBeanEventListeners() {
        return lifecycleStrategy.getEventListeners();
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
        namespaceCoin = namespaceFactory.get(CoinConstants.NAMESPACE_COIN);

        ComponentFactory componentFactory = namespaceCoin.getComponentFactory();
        componentFactory.create(NAME_KIND_SPEC_FACTORY, kindSpecFactory, BeanStage.INITIALIZE);
        componentFactory.create(NAME_NAMESPACE_FACTORY, namespaceFactory, BeanStage.INITIALIZE);
        componentFactory.create(NAME_CONFIGURATOR_FACTORY, configuratorFactory, BeanStage.INITIALIZE);
        componentFactory.create(NAME_VARIABLE_RESOLVER_FACTORY, variableResolverFactory, BeanStage.INITIALIZE);
        componentFactory.create(NAME_LIFECYCLE_STRATEGY, lifecycleStrategy, BeanStage.INITIALIZE);
        componentFactory.create(NAME_CONTROLLER, coinController, BeanStage.INITIALIZE);
        componentFactory.create(NAME_LOOKUP, lookup, BeanStage.INITIALIZE);
        componentFactory.create(NAME_MODULE, this, BeanStage.CREATE);
    }
}
