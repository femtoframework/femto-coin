package org.femtoframework.coin.spec.var;

import org.femtoframework.coin.annotation.Property;
import org.femtoframework.coin.info.BeanInfo;
import org.femtoframework.coin.info.PropertyInfo;
import org.femtoframework.coin.info.ext.SimpleBeanInfoFactory;
import org.junit.Test;

import static org.junit.Assert.*;

public class BeanResolverTest {

    @Test
    public void setCoinModule() {

        SimpleBeanInfoFactory factory = new SimpleBeanInfoFactory(null, null);
        BeanInfo beanInfo = factory.getBeanInfo(BeanResolver.class, true);

        PropertyInfo propertyInfo = beanInfo.getProperty("coinModule");
        assertNotNull(propertyInfo);
        assertFalse(propertyInfo.isReadable());
    }
}