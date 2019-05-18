package org.femtoframework.coin.configurator;

import org.femtoframework.coin.Component;
import org.femtoframework.coin.Configurator;
import org.femtoframework.coin.Namespace;
import org.femtoframework.bean.info.BeanInfo;
import org.femtoframework.bean.info.PropertyInfo;
import org.femtoframework.coin.spec.ConfigSpec;
import org.femtoframework.lang.reflect.Reflection;
import org.femtoframework.parameters.Parameters;
import org.femtoframework.text.NamingConvention;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Inject all configs
 *
 * @author Sheldon Shao
 * @version 1.0.0
 */
public class ConfigInjection implements Configurator {

    private Logger logger = LoggerFactory.getLogger(ConfigInjection.class);

    @Override
    public void configure(Component component) {
        Namespace ns = component.getCurrentNamespace();
        if (ns != null) {
            String name = component.getName();
            for(ConfigSpec spec: ns.getConfigSpecFactory()) {
                Parameters<Object> parameters = spec.getParameters();
                Parameters<Object> configForThisBean = parameters.getParameters(name);
                if (configForThisBean != null) {
                    inject(component, configForThisBean);
                }
            }
        }
    }

    public void inject(Component component, Parameters<Object> config) {
        BeanInfo beanInfo = component.getBeanInfo();
        for(String key: config.keySet()) {
            String propertyName = NamingConvention.parse(key, false);
            PropertyInfo propertyInfo = beanInfo.getProperty(propertyName);
            if (propertyInfo == null) {
                logger.warn("No such property:" + key + " in bean '" + component.getQualifiedName() + "' ignore this config.");
                continue;
            }
            if (!propertyInfo.isWritable()) {
                logger.warn("Property:" + key + " in bean '" + component.getQualifiedName() + "' is declared as not writable.");
            }
            Class<?> typeClass = propertyInfo.getTypeClass();
            Object bean = component.getBean();
            if (Reflection.isNonStructureClass(typeClass)) {
                if (Enum.class.isAssignableFrom(typeClass)) {
                    propertyInfo.invokeSetter(bean, config.getEnum((Class<? extends Enum>)typeClass, key));
                }
                else {
                    propertyInfo.invokeSetter(bean, config.get(key));
                }
            }
            else {
                Component childComp = component.getChild(key);
                if (childComp != null) {
                    Parameters<Object> configForChild = config.getParameters(key);
                    if (configForChild != null) {
                        inject(childComp, configForChild);
                    }
                }
                else {
                    if (Map.class.isAssignableFrom(typeClass) && propertyInfo.isReadable()) {
                        Map map = propertyInfo.invokeGetter(bean);
                        Parameters<Object> configForChild = config.getParameters(key);
                        if (configForChild != null) {
                            if (map == null || map.isEmpty()) {
                                propertyInfo.invokeSetter(bean, configForChild);
                            }
                            else {
                                for(String n: configForChild.keySet()) {
                                    map.put(n, configForChild.get(n));
                                }
                            }
                        }
                    }
                    else {
                        logger.warn("No such child:" + key + " in bean '" + component.getQualifiedName() + "'.");
                    }
                }
            }
        }
    }
}
