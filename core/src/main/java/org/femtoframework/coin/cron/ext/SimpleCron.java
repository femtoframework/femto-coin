package org.femtoframework.coin.cron.ext;

import org.femtoframework.bean.Nameable;
import org.femtoframework.bean.NamedBean;
import org.femtoframework.bean.annotation.Ignore;
import org.femtoframework.bean.annotation.Property;
import org.femtoframework.coin.Namespace;
import org.femtoframework.coin.cron.Cron;
import org.femtoframework.coin.spi.NamespaceAware;
import org.femtoframework.parameters.Parameters;
import org.femtoframework.parameters.ParametersMap;
import org.femtoframework.util.timer.CronEntry;

import java.util.Calendar;
import java.util.Iterator;

/**
 * 简单的Cron对象的实现
 *
 * @author renex
 * @version 2.00  2005-11-10
 */
public class SimpleCron
    implements Cron, Nameable, NamedBean, NamespaceAware
{
    static final String TYPE_ACTION = "action";
    static final String TYPE_RUNNABLE = "runnable";
    static final String ACTION_RUN = "run";

    @Ignore
    private CronEntry entry = new CronEntry();

    private String namespace = null;
    private String name = null;
    private String componentName = null;
    private String componentType;

    private String action;

    private Parameters arguments;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * Namespace
     *
     * @return Namespace
     */
    @Override
    public String getNamespace() {
        return namespace;
    }

    /**
     * 获取执行的命令
     *
     * @return 获取执行的命令
     */
    public String getComponentName() {
        return componentName;
    }

    /**
     * 命令类型，如"command","event"等等
     *
     * @return 获取执行的命令类型
     */
    public String getComponentType() {
        return componentType;
    }

    /**
     * Action, optional
     *
     * @return Method
     */
    @Override
    public String getAction() {
        return action;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public void setSecond(String second) {
        entry.setSecond(second);
    }

    @Property
    public String getSecond() {
        return entry.getSecond();
    }

    public void setMinute(String minute) {
        entry.setMinute(minute);
    }

    @Property
    public String getMinute() {
        return entry.getMinute();
    }

    public void setHour(String hour) {
        entry.setHour(hour);
    }

    @Property
    public String getHour() {
        return entry.getHour();
    }

    public void setDayOfMonth(String dom) {
        entry.setDayOfMonth(dom);
    }

    @Property
    public String getDayOfMonth() {
        return entry.getDayOfMonth();
    }

    public void setMonth(String moy) {
        entry.setMonth(moy);
    }

    @Property
    public String getMonth() {
        return entry.getMonth();
    }

    public void setYear(String year) {
        entry.setYear(year);
    }

    @Property
    public String getYear() {
        return entry.getYear();
    }

    public void setDayOfWeek(String dow) {
        entry.setDayOfWeek(dow);
    }

    @Property
    public String getDayOfWeek() {
        return entry.getDayOfWeek();
    }

    /**
     * 返回下次任务执行的时间
     *
     * @return 下次任务执行的时间
     */
    public long getNextRunningTime() {
        return entry.getNextRunningTime();
    }

    public long nextRunningTime() {
        return entry.nextRunningTime();
    }

    /**
     * 返回下次任务执行的时间
     *
     * @param now 当前时间
     * @return 下次任务执行的时间
     */
    public long nextRunningTime(long now) {
        return entry.nextRunningTime(now);
    }

    /**
     * This method builds a Date from a CrontabEntryBean. launching the same
     * method with now as parameter
     *
     * @return Date
     */
    public Calendar next() {
        return entry.next();
    }

    /**
     * This method builds a Date from a CrontabEntryBean. launching the same
     * method with now as parameter
     *
     * @param now 当前时间
     * @return Date
     */
    public Calendar next(long now) {
        return entry.next(now);
    }

    public Calendar next(Calendar cal) {
        return entry.buildCalendar(cal);
    }


    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("SimpleCron");
        sb.append("[entry=").append(entry);
        sb.append(",namespace=").append(namespace);
        sb.append(",name=").append(name);
        sb.append(",componentName=").append(componentName);
        sb.append(",componentType=").append(componentType);
        sb.append(']');
        Parameters params = arguments;
        if (params != null) {
            Iterator names = params.keySet().iterator();
            sb.append("{");
            boolean notFirst = false;
            while (names.hasNext()) {
                String name = (String)names.next();
                Object obj = arguments.get(name);
                if (obj != this) {
                    if (notFirst) {
                        sb.append(',');
                    }
                    else {
                        notFirst = true;
                    }
                    sb.append(name).append('=').append(obj);
                }
            }
            sb.append('}');
        }
        return sb.toString();
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Parameters getArguments() {
        if (arguments == null) {
            arguments = new ParametersMap();
        }
        return arguments;
    }

    public void setArguments(Parameters arguments) {
        this.arguments = arguments;
    }

    /**
     * Current Namespace
     *
     * @param namespace Current Namespace
     */
    @Override
    public void setNamespace(Namespace namespace) {
        this.namespace = namespace.getName();
    }
}
