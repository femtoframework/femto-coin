package org.femtoframework.coin.ext;

import org.femtoframework.coin.*;
import org.femtoframework.bean.annotation.Ignore;

import java.util.Set;

/**
 * Simple Bean ResourceFactory
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public class SimpleBeanFactory extends BaseResourceFactory<Object> implements BeanFactory {

    @Ignore
    private ComponentFactory componentFactory;

    public SimpleBeanFactory(NamespaceFactory namespaceFactory, SimpleComponentFactory componentFactory) {
        super(namespaceFactory, componentFactory.getNamespace());
        this.componentFactory = componentFactory;
    }

    /**
     * Return all names
     *
     * @return all names
     */
    @Override
    public Set<String> getNames() {
        return componentFactory.getNames();
    }

    /**
     * Return object by given name
     *
     * @param name Name
     * @return object in this factory
     */
    @Override
    public Object get(String name) {
        Object bean = super.get(name);
        if (bean == null) {
            Component component = componentFactory.get(name);
            if (component != null) {
                bean = component.getBean();
                if (bean != null) {
                    add(name, bean);
                }
            }
        }
        return bean;
    }

    @Override
    public void add(String name, Object bean) {
        if (bean != null) {
            super.add(name, bean);
        }
        else {
            throw new IllegalArgumentException("Bean of " + name + " is null");
        }
    }
}
