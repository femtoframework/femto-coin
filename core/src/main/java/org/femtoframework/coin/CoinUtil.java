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
package org.femtoframework.coin;

import org.femtoframework.coin.spec.KindSpecFactory;
import org.femtoframework.implement.ImplementUtil;

/**
 * Coin Util
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public class CoinUtil {

    private static CoinModule module;

    static {
        module = ImplementUtil.getInstance(CoinModule.class);
        ImplementUtil.initialize(module);
    }

    public static CoinModule getModule() {
        return module;
    }

    /**
     * Coin Control, maintain objects by Yaml or JSON
     *
     * @return Coin Control
     */
    public static CoinController getController() {
        return getModule().getController();
    }

    /**
     * Return KindSpecFactory
     *
     * @return KindSpecFactory
     */
    public static KindSpecFactory getKindSpecFactory() {
        return getModule().getKindSpecFactory();
    }
}
