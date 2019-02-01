package org.femtoframework.coin.info.ext;

import org.femtoframework.coin.info.BeanInfo;
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
    }
}