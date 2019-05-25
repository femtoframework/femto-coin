package org.femtoframework.coin.cron;

/**
 * Cron controller
 */
public interface CronController {

    /**
     * Add Cron
     *
     * @param cron Cron
     */
    void addCron(Cron cron);

    /**
     * Remove Cron
     *
     * @param cron Cron
     */
    void removeCron(Cron cron);


    /**
     * Apply cron, if configuration changed
     *
     * @param cron Cron
     */
    void applyCron(Cron cron);

    /**
     * 执行Cron
     *
     * @param cron Cron
     * @throws CronInvokeException
     */
    void invoke(Cron cron) throws CronInvokeException;
}
