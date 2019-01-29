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
package org.femtoframework.coin.api.ext;

import org.femtoframework.coin.CoinModule;
import org.femtoframework.coin.api.APIHandler;
import org.femtoframework.coin.api.APIRequest;
import org.femtoframework.coin.api.APIResponse;
import org.femtoframework.coin.spi.CoinModuleAware;

/**
 * API Handler
 * 
 * @author Sheldon Shao
 * @version 1.0
 */
public class SimpleAPIHandler implements APIHandler, CoinModuleAware {

    private CoinModule coinModule;

    /**
     * Handle API Request
     *
     * @param request API Request
     * @return
     */
    @Override
    public APIResponse handleRequest(APIRequest request) {

        return null;
    }

    @Override
    public void setCoinModule(CoinModule module) {
        this.coinModule = module;
    }
}
