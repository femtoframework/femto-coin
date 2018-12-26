/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
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

import org.femtoframework.coin.BeanContext;
import org.femtoframework.coin.BeanFactory;
import org.femtoframework.coin.BeanPhase;

import java.util.ArrayList;
import java.util.List;

/**
 * Bean Event Support
 * 
 * @author Sheldon Shao
 * @version 1.0
 */
public class BeanEventSupport implements BeanEventListener {
    
    private List<BeanEventListener> listeners = new ArrayList<>(2);
    
    /**
     * Handle bean event
     *
     * @param event Bean Event
     */
    @Override
    public void handleEvent(BeanEvent event) {
        List<BeanEventListener> listeners = this.listeners;
        if (!listeners.isEmpty()) {
            for(BeanEventListener listener: listeners) {
                listener.handleEvent(event);
            }
        }
    }

    public BeanEventSupport()
    {
    }

    public BeanEventSupport(BeanEventListener listener)
    {
        listeners.add(listener);
    }

    /**
     * Add new listener
     *
     * @param listener BeanEventListener
     */
    public void addListener(BeanEventListener listener)
    {
        List<BeanEventListener> newListeners = new ArrayList<>(this.listeners);
        newListeners.add(listener);
        this.listeners = newListeners;
    }

    /**
     * Delete listener
     *
     * @param listener BeanEventListener
     */
    public void removeListener(BeanEventListener listener)
    {
        List<BeanEventListener> newListeners = new ArrayList<>(this.listeners);
        newListeners.remove(listener);
        this.listeners = newListeners;
    }

    /**
     * Return all listeners
     *
     * @return all listeners
     */
    public List<BeanEventListener> getListeners() {
        return new ArrayList<>(listeners);
    }

    /**
     * Fire Event
     *
     * @param phase Phase
     * @param factory Bean Factory
     * @param beanName BeanName
     * @param bean Bean
     * @param context Context
     */
    public void fireEvent(BeanPhase phase, BeanFactory factory,
                          String beanName, Object bean, BeanContext context)
    {
        if (!listeners.isEmpty()) {
            handleEvent(new BeanEvent(factory, beanName, phase, bean, context));
        }
    }
}
