package org.femtoframework.coin.info;

/**
 * BeanInfo Factory
 *
 *
 */
public interface BeanInfoFactory {
    /**
     * Retrieve BeanInfo by class
     */
    default BeanInfo getBeanInfo(Class clazz) {
        return getBeanInfo(clazz, false);
    }

    /**
     * Retrieve BeanInfo by class
     *
     * @param clazz
     * @param generate Generate the beanInfo automatically when 'generate' is true
     */
    BeanInfo getBeanInfo(Class clazz, boolean generate);
}
