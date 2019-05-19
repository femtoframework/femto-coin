package org.femtoframework.coin.ext;

import org.femtoframework.bean.BeanPhase;
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
        status.setPhase(phase);
        status.addCondition(new SimpleBeanCondition(phase));
    }
}
