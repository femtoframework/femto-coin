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
package org.femtoframework.coin.status;

import org.femtoframework.bean.BeanPhase;

import java.util.Collections;
import java.util.List;

/**
 * Bean Status
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public interface BeanStatus {

    /**
     * Return conditions, container changes will add log information into this field.
     *
     * @return Condition
     */
    default List<BeanCondition> getConditions() {
        return Collections.emptyList();
    }


    /**
     * Add bean condition, it should merge the similar condition together
     *
     * @param condition BeanCondition
     */
    void addCondition(BeanCondition condition);

    /**
     * Bean Phase
     *
     * @return Phase
     */
    default BeanPhase getPhase() {
        return BeanPhase.DISABLED;
    }

    /**
     * Set current phase
     *
     * @param phase Current phase
     */
    void setPhase(BeanPhase phase);
}
