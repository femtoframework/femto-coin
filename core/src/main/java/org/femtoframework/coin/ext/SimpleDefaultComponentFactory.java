package org.femtoframework.coin.ext;

import org.femtoframework.coin.Component;
import org.femtoframework.coin.DefaultComponentFactory;
import org.femtoframework.pattern.ext.BaseFactory;

import java.util.Set;

/**
 * Simple DefaultComponentFactory
 */
public class SimpleDefaultComponentFactory extends BaseFactory<Component> implements DefaultComponentFactory {
    /**
     * Return all names
     *
     * @return all names
     */
    @Override
    public Set<String> getClassNames() {
        return super.getNames();
    }

    /**
     * Registry component as default of interface
     *
     * @param interfaceClass Interface Class
     * @param bean           Bean
     */
    @Override
    public void add(Class<?> interfaceClass, Component bean) {
        super.add(interfaceClass.getName(), bean);
    }
}
