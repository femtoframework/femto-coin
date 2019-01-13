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

import org.femtoframework.bean.InitializableMBean;
import org.femtoframework.coin.CoinConstants;
import org.femtoframework.coin.ext.BaseFactory;
import org.femtoframework.coin.spec.KindSpec;
import org.femtoframework.coin.spec.KindSpecFactory;
import org.femtoframework.coin.spec.SpecConstants;
import org.femtoframework.implement.ImplementUtil;
import org.femtoframework.implement.InstancesFunction;

/**
 * Simple Kind Spec Factory
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public class SimpleKindSpecFactory extends BaseFactory<KindSpec> implements KindSpecFactory, InitializableMBean {

    public SimpleKindSpecFactory() {
        super(null, CoinConstants.NAMESPACE_COIN);
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
        ImplementUtil.applyInstances(KindSpec.class, (InstancesFunction<String, KindSpec>) this::add);
    }

    /**
     * Return the core kind spec
     *
     * @return Core Kind Spec
     */
    @Override
    public KindSpec getCoreKindSpec() {
        return get(SpecConstants.VERSION_CORE_KIND);
    }
}
