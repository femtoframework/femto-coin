package org.femtoframework.cron;

/**
 * 基于CrontabFile的Crontab实现
 *
 * @author fengyun
 * @version 1.00 2005-11-16 13:36:13
 */
public interface CrontabFile extends Crontab
{
    /**
     * 是否需要更新
     *
     * @return 是否需要更新
     */
    boolean isNeedUpdate();

    /**
     * 获取文件
     *
     * @return 获取文件
     */
    String getFile();

    /**
     * 设置文件
     *
     * @param file
     */
    void setFile(String file);

    /**
     * 获取更新选项
     *
     * @return 更新周期
     */
    boolean isReload();

    /**
     * 设置更新选项
     *
     * @param reload
     */
    void setReload(boolean reload);
}
