package org.femtoframework.coin;

import org.femtoframework.pattern.Factory;

import java.util.Set;

/**
 * Some interfaces, like DataSource, need to have a default implementation for components
 * who don't have particular implementation.
 *
 * TODO: AutoConfig
 */
public interface DefaultComponentFactory extends Factory<Component> {

    /**
     * Return all names
     *
     * @return all names
     */
    Set<String> getClassNames();

    /**
     * Return all names
     *
     * @return all names
     */
    default Set<String> getNames() {
        return getClassNames();
    }

    /**
     * Return object by given name
     *
     * @param interfaceClassName Interface Class Name
     * @return object in this factory
     */
    Component get(String interfaceClassName);

    /**
     * Delete the object by given name
     *
     * @param interfaceClassName Interface Class Name
     * @return Deleted object
     */
    Component delete(String interfaceClassName);

    /**
     * Registry component as default of interface
     *
     * @param interfaceClass Interface Class
     * @param bean Bean
     */
    void add(Class<?> interfaceClass, Component bean);
}
