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

import java.util.ArrayList;
import java.util.List;

/**
 * Simple Bean Condition
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public class SimpleBeanCondition implements BeanCondition {

    private long timestamp;
    private List<String> types;
    private ConditionState state = ConditionState.UNKNOWN;

    public SimpleBeanCondition(BeanPhase phase) {
        this(phase.name(), ConditionState.TRUE);
    }

    public SimpleBeanCondition(String type, ConditionState state) {
        this.types = new ArrayList<>(2);
        this.types.add(type);
        this.state = state;
        this.timestamp = System.currentTimeMillis();
    }

    @Override
    public ConditionState getState() {
        return state;
    }

    @Override
    public List<String> getTypes() {
        return types;
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }
}