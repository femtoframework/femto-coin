package org.femtoframework.coin;

import javax.annotation.Resource;

/**
 * @author Sailing
 * @since 2005-3-3
 */
public class ArrayBeanContainer
{
    private ArrayBean bean;

    public ArrayBean getBean()
    {
        return bean;
    }

    @Resource
    public void setBean(ArrayBean bean)
    {
        this.bean = bean;
    }
}
