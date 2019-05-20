package org.femtoframework.coin.cron.invoker;

import org.femtoframework.bean.info.ActionInfo;
import org.femtoframework.bean.info.ArgumentInfo;
import org.femtoframework.bean.info.BeanInfo;
import org.femtoframework.coin.Component;
import org.femtoframework.coin.cron.Cron;
import org.femtoframework.coin.cron.CronInvoker;
import org.femtoframework.parameters.Parameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ActionInvoker implements CronInvoker
{
    private static Logger log = LoggerFactory.getLogger("coin/cron/action");

    /**
     * 执行Cron
     *
     * @param component Component
     * @param cron Cron
     */
    public void invoke(Component component, Cron cron)
    {
        Object resource = component.getBean();
        String actionName = cron.getAction();

        BeanInfo beanInfo = component.getBeanInfo();
        ActionInfo actionInfo = beanInfo.getAction(actionName);
        if (actionInfo == null) {
            log.warn("No such action '" + actionName + "' at resource:" + resource.getClass());
        }
        else { // Invoke action
            Parameters parameters = cron.getArguments();
            List<ArgumentInfo> argumentInfos = actionInfo.getArguments();
            try {
                if (argumentInfos == null || argumentInfos.isEmpty()) {
                    actionInfo.invoke(resource);
                } else {
                    int size = argumentInfos.size();
                    Object[] arguments = new Object[size];
                    for(int i = 0; i < size; i ++) {
                        ArgumentInfo argumentInfo = argumentInfos.get(i);
                        arguments[i] = argumentInfo.toValue(parameters.get(argumentInfo.getName()));
                    }
                    actionInfo.invoke(resource, arguments);
                }
            }
            catch (Exception ex) {
                log.error("Invoking action '" + actionName + "' exception at resource:"
                        + resource.getClass() + " " + ex.getMessage());
            }
        }
    }
}