package org.femtoframework.coin.event;

import java.util.List;

/**
 * Event Listeners
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public interface BeanEventListeners {

    /**
     * Add new listener
     *
     * @param listener BeanEventListener
     */
    void addListener(BeanEventListener listener);

    /**
     * Delete listener
     *
     * @param listener BeanEventListener
     */
    void removeListener(BeanEventListener listener);

    /**
     * Return all listeners
     *
     * @return all listeners
     */
    List<BeanEventListener> getListeners();
}
