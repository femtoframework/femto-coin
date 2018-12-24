package org.femtoframework.coin;

import org.femtoframework.bean.Nameable;

/**
 * 第一个对象
 *
 * @author fengyun
 * @version 1.00 2005-1-26 20:58:27
 */
public class FirstBean
    implements FirstInterface, Nameable
{
    private String name;
    private SecondInterface second;

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public SecondInterface getSecond()
    {
        return second;
    }

    public void setSecond(SecondInterface second)
    {
        this.second = second;
    }
}
