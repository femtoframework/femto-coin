package org.femtoframework.coin;

import org.femtoframework.annotation.ImplementedBy;

/**
 * 第三接口
 *
 * @author fengyun
 * @version 1.00 2005-1-27 15:42:02
 */
@ImplementedBy(value = "org.femtoframework.coin.ThirdBean")
public interface ThirdInterface
{
    /**
     * 返回对象名称
     *
     * @return
     */
    String getName();

    int getInteger();

    String getString();

    boolean isBool();
}
