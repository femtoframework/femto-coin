package org.femtoframework.coin.cron;

import org.femtoframework.coin.Component;

/**
 * Cron执行器，根据类型来选择
 *
 * @author fengyun
 * @version 1.00 2005-11-16 13:54:14
 */
public interface CronInvoker
{
    String ACTION = "action";

    /**
     * 执行Cron
     *
     * @param component Component
     * @param cron Cron
     * @throws CronInvokeException
     */
    void invoke(Component component, Cron cron) throws CronInvokeException;
}
