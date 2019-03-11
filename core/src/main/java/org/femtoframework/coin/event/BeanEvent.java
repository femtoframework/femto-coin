package org.femtoframework.coin.event;

import org.femtoframework.coin.BeanFactory;
import org.femtoframework.bean.BeanPhase;
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
     * Bean ResourceFactory
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
