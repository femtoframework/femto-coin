package org.femtoframework.coin.configurator;

import org.femtoframework.bean.Nameable;
import org.femtoframework.coin.Component;
import org.femtoframework.coin.Configurator;
import org.femtoframework.coin.spi.*;

/**
 * Configure container related stuff,
 * 1. Name
 * 2. Namespace
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public class AwarenessConfigurator implements Configurator {
    /**
     * Configure the bean
     *
     * @param component Component
     */
    @Override
    public void configure(Component component) {
        Object obj = component.getBean();
        if (obj instanceof Nameable) {
            String name = component.getName();
            if (name != null && !name.isEmpty()) {
                ((Nameable) obj).setName(name);
            }
        }
        if (obj instanceof NamespaceAware) {
            ((NamespaceAware) obj).setNamespace(component.getCurrentNamespace());
        }
        if (obj instanceof ComponentAware) {
            ((ComponentAware) obj).setComponent(component);
        }
        if (obj instanceof BeanFactoryAware) {
            ((BeanFactoryAware) obj).setBeanFactory(component.getCurrentBeanFactory());
        }
        if (obj instanceof NamespaceFactoryAware) {
            ((NamespaceFactoryAware) obj).setNamespaceFactory(component.getNamespaceFactory());
        }
        if (obj instanceof CoinModuleAware) {
            ((CoinModuleAware) obj).setCoinModule(component.getModule());
        }
    }
}
