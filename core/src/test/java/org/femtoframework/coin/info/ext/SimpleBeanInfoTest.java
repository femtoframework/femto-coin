package org.femtoframework.coin.info.ext;

import org.femtoframework.coin.info.ActionInfo;
import org.femtoframework.coin.info.BeanInfo;
import org.femtoframework.coin.info.PropertyInfo;
import org.junit.Test;

import java.util.Set;

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
    public void testActions() throws Exception {
        SimpleBeanInfoFactory beanInfoFactory = new SimpleBeanInfoFactory();
        BeanInfo beanInfo = beanInfoFactory.getBeanInfo(SimpleCounter.class);
        assertNotNull(beanInfo);
        assertEquals(SimpleCounter.class.getName(), beanInfo.getClassName());
        assertEquals(2, beanInfo.getProperties().size());
        assertEquals(beanInfo.getDescription(), "");

        PropertyInfo propertyInfo1 = beanInfo.getProperty("name");
        assertNotNull(propertyInfo1);
        assertEquals(propertyInfo1.getType(), "java.lang.String");

        PropertyInfo propertyInfo2 = beanInfo.getProperty("count");
        assertNotNull(propertyInfo2);
        assertEquals(propertyInfo2.getType(), "int");

        Set<String> actionNames = beanInfo.getActionNames();
        assertNotNull(actionNames);
        assertTrue(actionNames.contains("increase"));
        assertTrue(actionNames.contains("decrease"));

        ActionInfo actionInfo1 = beanInfo.getAction("increase");
        assertNotNull(actionInfo1);
        assertEquals(1, actionInfo1.getArguments().size());
        assertEquals("0", actionInfo1.getArguments().get(0).getName());
        assertEquals("int", actionInfo1.getArguments().get(0).getType());
        assertEquals("int", actionInfo1.getReturnType());
        assertEquals("ACTION_INFO", actionInfo1.getImpact());

        ActionInfo actionInfo2 = beanInfo.getAction("decrease");
        assertNotNull(actionInfo2);
        assertEquals(1, actionInfo1.getArguments().size());

        SimpleCounter counter = new SimpleCounter();
        counter.setCount(10);

        int newCount = actionInfo1.invoke(counter, 5);
        assertEquals(15, newCount);

        int newCountFromGetter = propertyInfo2.invokeGetter(counter);
        assertEquals(15, newCountFromGetter);

        propertyInfo2.invokeSetter(counter, 1000);
        newCountFromGetter = propertyInfo2.invokeGetter(counter);
        assertEquals(1000, newCountFromGetter);
    }
}