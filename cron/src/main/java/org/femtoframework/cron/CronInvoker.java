package org.femtoframework.cron;

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
     * @param cron Cron
     * @throws CronInvokeException
     */
    void invoke(Cron cron) throws CronInvokeException;
}
