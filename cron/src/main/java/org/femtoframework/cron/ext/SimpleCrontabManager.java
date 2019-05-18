package org.femtoframework.cron.ext;

import org.femtoframework.bean.BeanPhase;
import org.femtoframework.bean.LifecycleMBean;
import org.femtoframework.cron.*;
import org.femtoframework.implement.ImplementUtil;
import org.femtoframework.pattern.ext.BaseFactory;
import org.femtoframework.util.ArrayUtil;
import org.femtoframework.util.SortedList;
import org.femtoframework.util.thread.ExecutorUtil;
import org.femtoframework.util.thread.ScheduleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * CrontabManager 服务实现
 * *
 *
 * @author renex
 * @version 1.00  16:16:12 2003-7-7
 */
public final class SimpleCrontabManager
    extends BaseFactory<Crontab>
    implements CrontabManager<Crontab>, CronInvoker, LifecycleMBean {
    private static final long RELOAD_MIN_INTERVAL = 10 * 1000;//10s

    protected ScheduleService ses = null;

    protected final SortedList<Cron> cronList = new SortedList<>(new CronComparator());

    private long reloadInterval = 60 * 1000;

    private long checkInterval = 1000;

    private final Map<String, CrontabFile> file2Crontab = new HashMap<>();

    protected static Logger log = LoggerFactory.getLogger("apsis/crontab");

    public long getReloadInterval() {
        return reloadInterval;
    }

    public void setReloadInterval(long reloadInterval) {
        if (reloadInterval < RELOAD_MIN_INTERVAL) {
            throw new IllegalArgumentException("reload interval must large than " + RELOAD_MIN_INTERVAL);
        }
        this.reloadInterval = reloadInterval;
    }

    public void addCrontab(String descFile) {
        if (log.isInfoEnabled()) {
            log.info("Add crontab file[" + descFile + "]");
        }
        //TODO
//        XmlConfig config = new XmlConfig();
//        try {
//            ObjectMeta crontabDef = (ObjectMeta)config.parse(descFile);
//            Crontab crontab = (Crontab)CoinUtil.createObject("apsis_crontabs", crontabDef);
//            addCrontab(crontab);
//        }
//        catch (Exception e) {
//            log.warn("parser file ioexception", e);
//        }
    }

    public void addCrontab(Crontab crontab) {
        if (crontab instanceof CrontabFile) {
            String fileName = ((CrontabFile)crontab).getFile();
            if (fileName != null) {
                log.info("Add crontab [" + fileName + "] reload:" + ((CrontabFile)crontab).isReload());
                if (file2Crontab.containsKey(fileName)) {
                    removeCrontab(fileName);
                }
                file2Crontab.put(fileName, (CrontabFile)crontab);
            }
        }
        else {
            add(crontab);
        }
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

    public Crontab getCrontab(String file) {
        return get(file);
    }

    public Collection<Crontab> getCrontabs() {
        return getObjects();
    }

    public Crontab removeCrontab(String file) {
        Crontab crontab = delete(file);
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

    private BeanPhase beanPhase = BeanPhase.DISABLED;

    /**
     * Implement method of getPhase
     *
     * @return BeanPhase
     */
    @Override
    public BeanPhase _doGetPhase() {
        return beanPhase;
    }

    /**
     * Phase setter for internal
     *
     * @param phase BeanPhase
     */
    @Override
    public void _doSetPhase(BeanPhase phase) {
        this.beanPhase = phase;
    }

    /**
     * 初始化实现
     */
    public void _doInit() {
        ses = ExecutorUtil.newSingleThreadScheduler();
    }

    public void _doStart() {
        //启动更新线程
        ses.scheduleAtFixedRate(new CrontabReloader(), 1000, reloadInterval);
    }

    public synchronized void _doStop() {
        ses.stop();
    }

    /**
     * 实际销毁实现
     */
    public void _doDestroy() {
        ses.destroy();
    }

    /**
     * 实际要执行的任务方法。<br>
     * 通过这个方法，来执行实际的程序<br>
     * 如果出现异常，ErrorHandler的错误处理返回<code>true</code>，<br>
     * 那么该循环线程就终止循环。
     *
     * @throws Exception 各类执行异常
     */
    protected void doRun() throws Exception {
        if (cronList.isEmpty()) {
            Thread.sleep(getCheckInterval());
            return;
        }
        Cron cron = (Cron)cronList.first();
        if (cron == null) {
            return;
        }

        long now = System.currentTimeMillis();
        long next = cron.getNextRunningTime();
        while (next <= now) {
            synchronized (cronList) {
                cronList.remove(0);
            }
            invoke(cron);
            next = cron.nextRunningTime();
            if (next != -1) {//仍然需要执行
                synchronized (cronList) {
                    cronList.add(cron);
                }
            }
            else if (cronList.isEmpty()) {
                break;
            }
            cron = (Cron)cronList.first();
            if (cron == null) {
                return;
            }
            next = cron.getNextRunningTime();
            now = System.currentTimeMillis();
        }

        long sleep = next - now;
        if (sleep < getCheckInterval()) {
            sleep = getCheckInterval();
        }

        synchronized (cronList) {
            try {
                cronList.wait(sleep);
            }
            catch (InterruptedException e) {
            }
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
            String modelType = cron.getComponentType();
            if (modelType == null) {
                log.warn("No model type:" + cron);
                return;
            }
            CronInvoker invoker = null;
            try {
                invoker = ImplementUtil.getInstance(modelType, CronInvoker.class);
            }
            catch (RuntimeException re) {
            }
            if (invoker == null) {
                log.warn("No such model type:" + modelType);
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

    public long getCheckInterval() {
        return checkInterval;
    }

    public void setCheckInterval(long checkInterval) {
        this.checkInterval = checkInterval;
    }


    private static boolean isLaunched() {
        Object value =  System.getProperties().get("cube.system.launched");
        return value == Boolean.TRUE;
    }

    private class CrontabReloader implements Runnable {
        public void run() {
            while (!isLaunched()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
            }
            // 采用数组不会受数据更改的影响
            Object[] tabs;
            synchronized (file2Crontab) {
                tabs = file2Crontab.values().toArray();
            }
            if (ArrayUtil.isValid(tabs)) {
                for (Object tab : tabs) {
                    CrontabFile crontab = (CrontabFile)tab;
                    if (crontab.isReload() && crontab.isNeedUpdate()) {
                        String fileName = crontab.getFile();
                        if (log.isInfoEnabled()) {
                            log.info("Update crontab file:" + fileName);
                        }
                        addCrontab(fileName);
                    }
                }
            }
        }
    }
}
