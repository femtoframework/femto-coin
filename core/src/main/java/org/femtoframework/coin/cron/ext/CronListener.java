package org.femtoframework.coin.cron.ext;

import org.femtoframework.bean.BeanPhase;
import org.femtoframework.bean.BeanStage;
import org.femtoframework.coin.CoinModule;
import org.femtoframework.coin.Component;
import org.femtoframework.coin.cron.Cron;
import org.femtoframework.coin.cron.CronController;
import org.femtoframework.coin.event.BeanEvent;
import org.femtoframework.coin.event.BeanEventListener;
import org.femtoframework.coin.naming.CoinName;
import org.femtoframework.coin.spi.CoinModuleAware;

import javax.naming.Name;

public class CronListener implements BeanEventListener, CoinModuleAware {

    private CoinModule coinModule;

    private Name name = new CoinName("coin:cron_controller");

    /**
     * Handle bean event
     *
     * @param event Bean Event
     */
    @Override
    public void handleEvent(BeanEvent event) {
        if (event.getPhase().isAfterOrCurrent(BeanPhase.STARTED)) {
            Object bean = event.getTarget();
            if (bean instanceof Cron) {
                CronController cronController = (CronController)coinModule.getLookup().lookupBean(name);
                if (cronController != null) {
                    cronController.addCron((Cron) bean);
                }
            }
        }
        else if (event.getPhase().isAfterOrCurrent(BeanPhase.STOPPING)) {
            Object bean = event.getTarget();
            if (bean instanceof Cron) {
                CronController cronController = (CronController)coinModule.getLookup().lookupBean(name);
                if (cronController != null) {
                    cronController.removeCron((Cron) bean);
                }
            }
        }
        else if (event.getPhase() == BeanPhase.CONFIGURED) {
            Component component = event.getComponent();
            Object bean = event.getTarget();
            if (bean instanceof Cron) {
                if (component.getStage().isAfterOrCurrent(BeanStage.START)) {
                    //Apply cron again
                    CronController cronController = (CronController)coinModule.getLookup().lookupBean(name);
                    if (cronController != null) {
                        cronController.applyCron((Cron) bean);
                    }
                }
            }
        }
    }

    @Override
    public void setCoinModule(CoinModule module) {
        this.coinModule = module;
    }
}
