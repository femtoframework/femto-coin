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


import org.femtoframework.bean.BeanPhase;
import org.femtoframework.coin.status.BeanCondition;
import org.femtoframework.coin.status.BeanStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple Bean Status
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public class SimpleBeanStatus implements BeanStatus {

    private BeanPhase phase = BeanPhase.DISABLED;
    private List<BeanCondition> conditions = new ArrayList<>(3);

    /**
     * Return conditions, container changes will add log information into this field.
     *
     * @return Condition
     */
    public List<BeanCondition> getConditions() {
        return conditions;
    }

    /**
     * Bean Phase
     *
     * @return Phase
     */
    public BeanPhase getPhase() {
        return phase;
    }

    public void setConditions(List<BeanCondition> conditions) {
        this.conditions = conditions;
    }

    public void setPhase(BeanPhase phase) {
        this.phase = phase;
    }
}
