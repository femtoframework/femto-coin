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
package org.femtoframework.coin.spec;


import org.femtoframework.bean.NamedBean;

import java.util.Map;

/**
 * Kind Spec
 *
 * Kind can be used for extension, users can define their own spec structure, and plug them into COIN
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public interface KindSpec extends NamedBean {

    /**
     * Name of the object
     *
     * @return Name of the object
     */
    default String getName() {
        return getVersion();
    }

    /**
     * The version this Factory supports
     *
     * @return Version
     */
    String getVersion();

    /**
     * The Kind Class it uses
     *
     * @return The kind Class should be an Enum and Kind implementation
     */
    Class<? extends Kind> getKindClass();

    /**
     * Convert Kind to right spec
     *
     * @param map Map like data
     * @return right spec object
     */
    <S extends MapSpec> S toSpec(Map map);
}
