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

import org.femtoframework.coin.BeanPhase;
import org.femtoframework.coin.Component;
import org.femtoframework.coin.event.BeanEvent;
import org.femtoframework.coin.event.BeanEventListener;
import org.femtoframework.coin.status.BeanStatus;

/**
 * Update the component phase to Component Object
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public class SimpleStatusUpdater implements BeanEventListener {

    /**
     * Handle bean event
     *
     * @param event Bean Event
     */
    @Override
    public void handleEvent(BeanEvent event) {
        Component component = event.getComponent();
        BeanStatus status = component.getStatus();
        BeanPhase phase = event.getPhase();
        if (status instanceof SimpleBeanStatus) {
            SimpleBeanStatus simpleBeanStatus = (SimpleBeanStatus)status;
            simpleBeanStatus.setPhase(phase);
            simpleBeanStatus.getConditions().add(new SimpleBeanCondition(phase));
        }
    }
}
