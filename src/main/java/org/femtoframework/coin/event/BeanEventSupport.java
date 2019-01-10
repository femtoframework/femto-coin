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

import org.femtoframework.bean.Initializable;
import org.femtoframework.bean.InitializableMBean;
import org.femtoframework.coin.BeanFactory;
import org.femtoframework.bean.BeanPhase;
import org.femtoframework.coin.Component;
import org.femtoframework.coin.ext.SimpleStatusUpdater;
import org.femtoframework.implement.ImplementUtil;
import org.femtoframework.lang.reflect.Reflection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Bean Event Support
 * 
 * @author Sheldon Shao
 * @version 1.0
 */
public class BeanEventSupport implements BeanEventListeners, BeanEventListener, InitializableMBean {
    
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
     * @param component Component
     */
    public void fireEvent(BeanPhase phase, BeanFactory factory,
                          String beanName, Object bean, Component component)
    {
        if (!listeners.isEmpty()) {
            handleEvent(new BeanEvent(factory, beanName, phase, bean, component));
        }
    }

    /**
     * Fire Event
     *
     * @param phase
     * @param component
     */
    public void fireEvent(BeanPhase phase, Component component)
    {
        if (!listeners.isEmpty()) {
            handleEvent(new BeanEvent(component.getCurrentBeanFactory(), component.getName(),
                    phase, component.getBean(), component));
        }
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
//        addListener(new SimpleStatusUpdater());
        Iterator<Class<? extends BeanEventListener>> it = ImplementUtil.getImplements(BeanEventListener.class);
        while(it.hasNext()) {
            Class<? extends BeanEventListener> clazz = it.next();
            BeanEventListener listener = Reflection.newInstance(clazz);
            listeners.add(listener);
        }
    }
}
