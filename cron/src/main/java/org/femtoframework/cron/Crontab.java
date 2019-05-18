package org.femtoframework.cron;


import org.femtoframework.bean.NamedBean;
import org.femtoframework.pattern.Factory;

import java.util.Collection;

/**
 * Cron对象管理器的接口对象
 *
 * @author renex
 * @version 1.00  14:18:59 2003-7-7
 */
public interface Crontab extends NamedBean, Factory<Cron> {

    /**
     * 添加一个Cron信息
     *
     * @param cron
     */

    void addCron(Cron cron);

    /**
     * 删除一个Cron
     *
     * @param name 任务名称
     */
    Cron removeCron(String name);

    /**
     * 依照名称得到Cron
     *
     * @param name
     * @return Cron
     */
    Cron getCron(String name);

    /**
     * 返回所有的Cron
     *
     * @return 描述枚举
     */
    Collection<Cron> getCrons();

    /**
     * 更新文件
     */
    void clear();


}
