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
package org.femtoframework.coin.spec.ext;

import org.femtoframework.bean.BeanStage;
import org.femtoframework.bean.InitializableMBean;
import org.femtoframework.coin.*;
import org.femtoframework.coin.exception.BeanSpecSyntaxException;
import org.femtoframework.coin.ext.BaseFactory;
import org.femtoframework.coin.spec.VariableResolver;
import org.femtoframework.coin.spec.VariableResolverFactory;
import org.femtoframework.coin.spec.VariableSpec;
import org.femtoframework.implement.ImplementUtil;
import org.femtoframework.implement.InstancesFunction;


/**
 * VariableResolverFactory
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public class SimpleVariableResolverFactory extends BaseFactory<VariableResolver>  implements VariableResolverFactory, InitializableMBean {

    private CoinModule coinModule;

    public SimpleVariableResolverFactory(CoinModule coinModule, NamespaceFactory namespaceFactory) {
        super(namespaceFactory, CoinConstants.NAMESPACE_COIN);
        this.coinModule = coinModule;
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
        ImplementUtil.applyInstances(VariableResolver.class, (InstancesFunction<String, VariableResolver>) this::add);

        NamespaceFactory namespaceFactory = coinModule.getNamespaceFactory();
        Namespace namespace = namespaceFactory.get(CoinConstants.NAMESPACE_COIN);
        ComponentFactory componentFactory = namespace.getComponentFactory();
        for(String name: getNames()) {
            VariableResolver vr = get(name);
            componentFactory.create("vr_" + name , vr, BeanStage.INITIALIZE);
        }
    }

    /**
     * Resolve variable by spec
     *
     * @param var          VariableSpec
     * @param expectedType
     * @param component
     * @return
     */
    @Override
    public <T> T resolve(VariableSpec var, Class<T> expectedType, Component component) {
        String prefix = var.getPrefix();
        if (prefix == null) {
            prefix = "b";
        }
        VariableResolver vr = get(prefix);
        if (vr == null) {
            throw new BeanSpecSyntaxException("No such prefix;" + prefix +" in variable:" + var.getName());
        }
        return vr.resolve(var, expectedType, component);
    }
}
