package org.femtoframework.coin;

/**
 * Bean Lifecycle Strategy
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public interface LifecycleStrategy extends Configurator {

    /**
     * Create bean
     *
     * @param component Component
     * @return the created bean
     */
    Object create(Component component);

    /**
     * Initialize component
     *
     * @param component
     */
    void init(Component component);

    /**
     * Start component
     *
     * @param component Component
     * @throws org.femtoframework.coin.exception.BeanLifecycleException
     */
    void start(Component component);


    /**
     * Stop component
     *
     * @param component Component
     * @throws org.femtoframework.coin.exception.BeanLifecycleException
     */
    default void stop(Component component) {

    }


    default void destroy(Component component) {

    }


}
