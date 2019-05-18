package org.femtoframework.cron;

import org.femtoframework.bean.NamedBean;

import java.util.Calendar;


/**
 * Cron对象的接口定义<br>
 * Cron对象是任务和其执行时间的集合
 *
 * @author renex
 * @version 1.00  14:19:07 2003-7-7
 */
public interface Cron extends NamedBean
{
    /**
     * 获取执行的组件名称
     *
     * @return 组件名称
     */
    String getComponentName();

    /**
     * 组件类型，如"runnable", "method","event"等等
     *
     * @return 组件类型
     */
    String getComponentType();

    /**
     * Action, optional
     *
     * @return Method
     */
    String getAction();

    /**
     * 获取 second
     *
     * @return second
     */
    String getSecond();

    /**
     * 获取分
     *
     * @return minute
     */
    String getMinute();

    /**
     * 获取hour
     *
     * @return hour
     */
    String getHour();

    /**
     * 获取 day-of-month
     *
     * @return day-of-month
     */
    String getDayOfMonth();

    /**
     * 获取 month
     *
     * @return month
     */
    String getMonth();

    /**
     * 获取 year
     *
     * @return year
     */
    String getYear();

    /**
     * 获取 day-of-week
     *
     * @return day-of-week
     */
    String getDayOfWeek();

    /**
     * 返回下次任务执行的时间
     *
     * @return 下次任务执行的时间
     */
    long getNextRunningTime();

    /**
     * 返回下次任务执行的时间
     *
     * @return 下次任务执行的时间
     */
    long nextRunningTime();

    /**
     * 返回下次任务执行的时间
     *
     * @param now   当前时间
     * @return 下次任务执行的时间
     */
    long nextRunningTime(long now);

    /**
     * This method builds a Date from a CrontabEntryBean. launching the same
     * method with now as parameter
     *
     * @return Date
     */
    Calendar next();

    /**
     * This method builds a Date from a CrontabEntryBean. launching the same
     * method with now as parameter
     *
     * @param now   当前时间
     * @return Date
     */
    Calendar next(long now);

    /**
     * This method builds a Date from a CrontabEntryBean. launching the same
     * method with now as parameter
     *
     * @param cal   表示当前时间的日历
     * @return Date
     */
    Calendar next(Calendar cal);
}
