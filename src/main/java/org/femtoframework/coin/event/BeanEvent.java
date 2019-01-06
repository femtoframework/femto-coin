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
package org.femtoframework.coin.event;

import org.femtoframework.coin.BeanFactory;
import org.femtoframework.coin.BeanPhase;
import org.femtoframework.coin.Component;

import java.util.EventObject;

/**
 * Bean Lifecycle Event
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public class BeanEvent extends EventObject {


    private BeanFactory factory;

    private String name;

    private BeanPhase phase;

    private Object target;

    private Component component;


    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public BeanEvent(BeanFactory source, String name, BeanPhase phase, Object target, Component component) {
        super(source);
        this.factory = source;
        this.name = name;
        this.phase = phase;
        this.target = target;
        this.component = component;
    }

    /**
     * Bean Factory
     */
    public BeanFactory getFactory() {
        return factory;
    }

    /**
     * Bean Name
     */
    public String getName() {
        return name;
    }

    /**
     * Bean Phase
     */
    public BeanPhase getPhase() {
        return phase;
    }

    /**
     * Target bean
     */
    public Object getTarget() {
        return target;
    }

    /**
     * Component
     */
    public Component getComponent() {
        return component;
    }
}
