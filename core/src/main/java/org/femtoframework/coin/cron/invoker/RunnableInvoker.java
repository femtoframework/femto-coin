package org.femtoframework.coin.cron.invoker;

import org.femtoframework.coin.Component;
import org.femtoframework.coin.cron.Cron;
import org.femtoframework.coin.cron.CronInvoker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Runnable Invoker
 *
 * @author fengyun
 * @version 1.00 2010-10-16 14:42:44
 */
public class RunnableInvoker implements CronInvoker
{
    private static Logger log = LoggerFactory.getLogger("coin/cron/runnable");

    /**
     * 执行Cron
     *
     * @param component Component
     * @param cron Cron
     */
    public void invoke(Component component, Cron cron)
    {
        String componentName = cron.getComponentName();
        Object obj = component.getBean();
        if (obj instanceof Runnable) {
            ((Runnable) obj).run();
        }
        else {
            log.error("Invalid bean:" + obj + " component name:" + componentName);
        }
    }
}
