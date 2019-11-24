package org.femtoframework.coin.cron.ext;

import org.femtoframework.bean.info.ActionInfo;
import org.femtoframework.bean.info.BeanInfo;
import org.femtoframework.coin.CoinModule;
import org.femtoframework.coin.Component;
import org.femtoframework.coin.ComponentFactory;
import org.femtoframework.coin.Namespace;
import org.femtoframework.coin.cron.Cron;
import org.femtoframework.coin.cron.CronController;
import org.femtoframework.coin.cron.CronInvokeException;
import org.femtoframework.coin.cron.CronInvoker;
import org.femtoframework.implement.ImplementUtil;
import org.femtoframework.util.SortedList;
import org.femtoframework.util.thread.LifecycleThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SimpleCronController extends LifecycleThread
        implements CronController {

    protected static Logger log = LoggerFactory.getLogger("coin/cron");

    private CoinModule coinModule;

    private long checkInterval = 1000;

    protected final SortedList<Cron> cronList = new SortedList<>(new CronComparator());

    public SimpleCronController(CoinModule coinModule) {
        this.coinModule = coinModule;
    }

    /**
     * Cron List to show the details
     *
     * @return Cron List
     */
    public List<Cron> getCronList() {
        return cronList;
    }

    /**
     * Add Cron
     *
     * @param cron Cron
     */
    @Override
    public void addCron(Cron cron) {
        synchronized (cronList) {
            cronList.add(cron);
        }
    }

    /**
     * Remove Cron
     *
     * @param cron Cron
     */
    @Override
    public void removeCron(Cron cron) {
        synchronized (cronList) {
            cronList.remove(cron);
        }
    }

    /**
     * Apply cron, if configuration changed
     *
     * @param cron Cron
     */
    @Override
    public void applyCron(Cron cron) {
        synchronized (cronList) {
            removeCron(cron);
            addCron(cron);
        }
    }

    /**
     * 执行Cron
     *
     * @param cron Cron
     * @throws CronInvokeException
     */
    @Override
    public void invoke(Cron cron) throws CronInvokeException {
        if (log.isDebugEnabled()) {
            log.debug("Invoke cron:" + cron);
        }
        String namespace = cron.getNamespace();
        Namespace ns = coinModule.getNamespaceFactory().get(namespace);
        if (ns == null) {
            log.error("No such namespace:" + namespace);
            return;
        }
        ComponentFactory componentFactory = ns.getComponentFactory();
        String componentName= cron.getComponentName();
        Component component = componentFactory.get(componentName);
        if (component == null) {
            log.warn("No such component:" + componentName + " in namespace:" + namespace);
            return;
        }

        String componentType = cron.getComponentType();
        if (componentType == null) {
            Object bean = component.getBean();
            if (bean instanceof Runnable) {
                componentType = SimpleCron.TYPE_RUNNABLE;
            }
            else if (cron.getAction() != null) {
                BeanInfo beanInfo = component.getBeanInfo();
                ActionInfo actionInfo = beanInfo.getAction(cron.getAction());
                if (actionInfo != null) {
                    componentType = SimpleCron.TYPE_ACTION;
                }
            }
        }
        if (componentType != null) {
            CronInvoker invoker = ImplementUtil.getInstance(componentType, CronInvoker.class);
            if (invoker == null) {
                log.warn("No such component type:" + componentType);
                return;
            }
            Thread thread = new Thread(new CronRunnable(invoker, component, cron), "Crontab-" + cron.getName());
            thread.start();
        }
        else {
            log.warn("No component type specified:" + cron);
        }
    }

    private static class CronRunnable implements Runnable {

        private CronInvoker invoker;
        private Component component;
        private final Cron cron;

        private CronRunnable(CronInvoker invoker, Component component, final Cron cron) {
            this.invoker = invoker;
            this.component = component;
            this.cron = cron;
        }

        public void run() {
            if (log.isDebugEnabled()) {
                log.debug("Invoke cron:" + cron);
            }
            try {
                invoker.invoke(component, cron);
            }
            catch (Throwable t) {
                log.error("Invoke cron exception", t);
            }
        }
    }

    /**
     * The real logic
     *
     * @throws Exception Exception
     * @see #run()
     */
    @Override
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

        long sleep = getCheckInterval();

        synchronized (cronList) {
            try {
                cronList.wait(sleep);
            }
            catch (InterruptedException e) {
            }
        }
    }

    public long getCheckInterval() {
        return checkInterval;
    }

    public void setCheckInterval(long checkInterval) {
        this.checkInterval = checkInterval;
    }
}
