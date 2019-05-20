package org.femtoframework.coin.event;


import org.femtoframework.bean.InitializableMBean;
import org.femtoframework.coin.BeanFactory;
import org.femtoframework.bean.BeanPhase;
import org.femtoframework.coin.CoinModule;
import org.femtoframework.coin.Component;
import org.femtoframework.coin.spi.CoinModuleAware;
import org.femtoframework.implement.ImplementUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Bean Event Support
 * 
 * @author Sheldon Shao
 * @version 1.0
 */
public class BeanEventSupport implements BeanEventListeners, BeanEventListener, InitializableMBean, CoinModuleAware {
    
    private List<BeanEventListener> listeners = new ArrayList<>(2);

    private CoinModule coinModule;
    
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
        if (listener instanceof CoinModuleAware) {
            ((CoinModuleAware)listener).setCoinModule(coinModule);
        }
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
     * @param factory Bean ResourceFactory
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
    public void _doInit() {
        ImplementUtil.applyInstances(BeanEventListener.class, this::addListener);
    }

    @Override
    public void setCoinModule(CoinModule module) {
        this.coinModule = module;
    }
}
