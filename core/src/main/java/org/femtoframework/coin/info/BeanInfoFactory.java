package org.femtoframework.coin.info;

import org.femtoframework.coin.Factory;
import org.femtoframework.lang.reflect.Reflection;

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
        if (Reflection.isNonStructureClass(clazz)) {
            return null;
        }
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
