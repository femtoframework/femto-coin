package org.femtoframework.coin;

import org.femtoframework.annotation.ImplementedBy;

import java.util.List;

/**
 * 第二个接口
 *
 * @author fengyun
 * @version 1.00 2005-1-27 15:41:28
 */
@ImplementedBy(value = "org.femtoframework.coin.SecondBean")
public interface SecondInterface
{
    List<ThirdInterface> getThirds();
}
