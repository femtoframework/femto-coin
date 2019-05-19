package org.femtoframework.cron;

import org.femtoframework.pattern.Factory;

import java.util.Collection;

/**
 * CrontabManager 服务器
 *
 * @author renex
 * @version 1.00  10:50:12 2003-7-7
 */
public interface CrontabManager<CT extends Crontab> extends Factory<CT>
{
    /**
     * 添加一个Crontab
     *
     * @param crontab crontab
     */
    void addCrontab(CT crontab);

    /**
     * 获取指定名称的CronManager
     *
     * @param name 名称
     * @return Crontab
     */
    CT getCrontab(String name);

    /**
     * 获得所有的Crontab
     *
     * @return 所有的Crontab
     */
    Collection<CT> getCrontabs();

    /**
     * 删除一个Crontab
     *
     * @param name 名称
     */
    CT removeCrontab(String name);
}
