package org.femtoframework.cron.ext;

import org.femtoframework.cron.*;
import org.femtoframework.implement.ImplementUtil;
import org.femtoframework.pattern.ext.BaseFactory;

import org.femtoframework.util.SortedList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * CrontabManager 服务实现
 *
 *
 * @author renex
 * @version 1.00  16:16:12 2003-7-7
 */
public final class SimpleCrontabManager
    extends BaseFactory<Crontab>
    implements CrontabManager<Crontab>, CronInvoker {

    protected final SortedList<Cron> cronList = new SortedList<>(new CronComparator());

    protected static Logger log = LoggerFactory.getLogger("apsis/crontab");

    public void addCrontab(Crontab crontab) {
        add(crontab);

        for (Cron cron : crontab.getCrons()) {
            synchronized (cronList) {
                cronList.add(cron);
            }
        }
        synchronized (cronList) {
            cronList.notifyAll();
        }
    }

    public SortedList getCronList() {
        return cronList;
    }

    public Crontab getCrontab(String name) {
        return get(name);
    }

    public Collection<Crontab> getCrontabs() {
        return getObjects();
    }

    public Crontab removeCrontab(String name) {
        Crontab crontab = delete(name);
        if (crontab == null) {
            return null;
        }
        else {
            for (Cron cron : crontab.getCrons()) {
                synchronized (cronList) {
                    cronList.remove(cron);
                }
            }
            return crontab;
        }
    }

    private static class CronRunnable implements Runnable {

        private final Cron cron;

        private CronRunnable(final Cron cron) {
            this.cron = cron;
        }

        public void run() {
            if (log.isDebugEnabled()) {
                log.debug("Invoke cron:" + cron);
            }
            String componentType = cron.getComponentType();
            if (componentType == null) {
                log.warn("No component type:" + cron);
                return;
            }
            //TODO auto match
            CronInvoker invoker = null;
            try {
                invoker = ImplementUtil.getInstance(componentType, CronInvoker.class);
            }
            catch (RuntimeException re) {
            }
            if (invoker == null) {
                log.warn("No such component type:" + componentType);
                return;
            }
            try {
                invoker.invoke(cron);
            }
            catch (Throwable t) {
                log.error("Invoke cron exception", t);
            }
        }
    }

    /**
     * 执行Cron
     *
     * @param cron Cron
     */
    public void invoke(final Cron cron) {
        Thread thread = new Thread(new CronRunnable(cron), "Crontab-" + cron.getName());
        thread.start();
    }
}
