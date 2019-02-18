package org.femtoframework.coin.spec.var;

import org.femtoframework.bean.info.BeanInfo;
import org.femtoframework.bean.info.BeanInfoUtil;
import org.femtoframework.bean.info.PropertyInfo;
import org.junit.Test;

import static org.junit.Assert.*;

public class BeanResolverTest {

    @Test
    public void setCoinModule() {
        BeanInfo beanInfo = BeanInfoUtil.getBeanInfo(BeanResolver.class, true);

        PropertyInfo propertyInfo = beanInfo.getProperty("coinModule");
        assertNotNull(propertyInfo);
        assertFalse(propertyInfo.isReadable());
    }
}