package org.femtoframework.coin.info.ext;

import org.femtoframework.coin.info.BeanInfo;
import org.femtoframework.coin.info.PropertyInfo;
import org.junit.Test;

import static org.junit.Assert.*;

public class SimpleBeanInfoTest {

    @Test
    public void setInfos() {
    }

    @Test
    public void testPojo() {
        SimpleBeanInfoFactory beanInfoFactory = new SimpleBeanInfoFactory();
        BeanInfo beanInfo = beanInfoFactory.getBeanInfo(SimplePojo.class);
        assertNotNull(beanInfo);
        assertEquals(SimplePojo.class.getName(), beanInfo.getClassName());
        assertEquals(3, beanInfo.getProperties().size());
        assertEquals(beanInfo.getDescription(), "");

        PropertyInfo propertyInfo1 = beanInfo.getProperty("value1");
        assertNotNull(propertyInfo1);
        assertEquals(propertyInfo1.getType(), "java.lang.String");

        PropertyInfo propertyInfo2 = beanInfo.getProperty("value2");
        assertNotNull(propertyInfo2);
        assertEquals(propertyInfo2.getType(), "int");

        PropertyInfo propertyInfo3 = beanInfo.getProperty("value3");
        assertNotNull(propertyInfo3);
        assertEquals(propertyInfo3.getType(), "boolean");
    }

    @Test
    public void testAlphabeticProperties() {

    }
}