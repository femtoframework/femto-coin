package org.femtoframework.coin;


import org.femtoframework.bean.Nameable;

/**
 * 第三层的对象
 *
 * @author fengyun
 * @version 1.00 2005-1-26 20:59:22
 */
public class ThirdBean
    implements ThirdInterface, Nameable
{
    private String name;
    private int integer;
    private String string;
    private boolean bool;

    public int getInteger()
    {
        return integer;
    }

    public void setInteger(int integer)
    {
        this.integer = integer;
    }

    public String getString()
    {
        return string;
    }

    public void setString(String string)
    {
        this.string = string;
    }

    public boolean isBool()
    {
        return bool;
    }

    public void setBool(boolean bool)
    {
        this.bool = bool;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
