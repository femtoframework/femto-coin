package org.femtoframework.coin.codec.yaml;

import org.femtoframework.bean.annotation.Coined;
import org.femtoframework.bean.info.BeanInfo;
import org.femtoframework.bean.info.BeanInfoFactory;
import org.femtoframework.bean.info.PropertyInfo;
import org.yaml.snakeyaml.introspector.BeanAccess;
import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.introspector.PropertyUtils;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

public class CoinPropertyUtils extends PropertyUtils {

    private BeanInfoFactory beanInfoFactory;

    public CoinPropertyUtils(BeanInfoFactory beanInfoFactory) {
        this.beanInfoFactory = beanInfoFactory;
    }

    protected Set<Property> createPropertySet(Class<? extends Object> type, BeanAccess bAccess) {
        if (type.isAnnotationPresent(Coined.class)) {
            Set<Property> properties = new TreeSet<>();
            BeanInfo beanInfo = beanInfoFactory.getBeanInfo(type, true);
            Collection<PropertyInfo> infos = beanInfo.getProperties();
            for (PropertyInfo info : infos) {
                properties.add(new YamlProperty(info));
            }
            return properties;
        }
        else {
            return super.createPropertySet(type, bAccess);
        }
    }
}
