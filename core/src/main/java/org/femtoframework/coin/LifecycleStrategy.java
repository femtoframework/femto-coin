package org.femtoframework.coin;

import org.femtoframework.bean.BeanStage;

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
    void stop(Component component);

    /**
     * Destroy component
     *
     * @param component Component
     */
    void destroy(Component component);

    /**
     * Ensure the Bean in given stage
     *
     * @param bean Bean
     * @param stage Target Stage
     */
    void ensure(Object bean, BeanStage stage);

}
