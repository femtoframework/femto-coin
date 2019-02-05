package org.femtoframework.coin.info;

import org.femtoframework.coin.Factory;

/**
 * BeanInfo Factory
 *
 *
 */
public interface BeanInfoFactory extends Factory<BeanInfo> {
    /**
     * Retrieve BeanInfo by class
     */
    default BeanInfo getBeanInfo(Class clazz) {
        return getBeanInfo(clazz, true);
    }

    /**
     * Retrieve BeanInfo by class
     *
     * @param clazz
     * @param generate Generate the beanInfo automatically when 'generate' is true
     */
    BeanInfo getBeanInfo(Class clazz, boolean generate);
}
