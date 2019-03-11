package org.femtoframework.coin.event;

import java.util.EventListener;

/**
 * Bean Event Listener
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public interface BeanEventListener extends EventListener {

    /**
     * Handle bean event
     *
     * @param event Bean Event
     */
    void handleEvent(BeanEvent event);
}
