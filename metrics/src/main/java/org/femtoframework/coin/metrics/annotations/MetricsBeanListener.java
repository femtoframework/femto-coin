package org.femtoframework.coin.metrics.annotations;

import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import org.femtoframework.bean.BeanPhase;
import org.femtoframework.bean.info.BeanInfo;
import org.femtoframework.coin.CoinModule;
import org.femtoframework.coin.Component;
import org.femtoframework.coin.event.BeanEvent;
import org.femtoframework.coin.event.BeanEventListener;
import org.femtoframework.coin.metrics.MetricsService;
import org.femtoframework.coin.metrics.MetricsUtil;
import org.femtoframework.coin.spi.CoinModuleAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.NamingException;

/**
 * A bean listener to handle metrics related annotations
 *
 * TODO remove the metrics from registry
 */
public class MetricsBeanListener implements BeanEventListener, CoinModuleAware
{

    private CompositeMeterRegistry registry;

    private CoinModule coinModule;

    private static Logger logger = LoggerFactory.getLogger(MetricsBeanListener.class);

    @Override
    public void handleEvent(BeanEvent event) {
        BeanPhase phase = event.getPhase();
        Object bean = event.getComponent().getBean();
        if (phase == BeanPhase.STARTING) {
            Component component = event.getComponent();
            BeanInfo beanInfo = component.getBeanInfo();
            MetricsUtil.registryMetrics(getRegistry(), bean, beanInfo);
        }
    }

    private CompositeMeterRegistry getRegistry() {
        if (registry == null) {
            try {
                MetricsService service = (MetricsService)coinModule.getLookup().lookupBean("coin-metrics:metrics-service");
                registry = service.getCompositeMeterRegistry();
            }
            catch(IllegalArgumentException iae) {
                logger.warn("The namespace 'coin-metrics' is not initialized yet.");
            }
            catch(NamingException ne) {
                logger.warn("The metrics service 'metrics-service' is not initialized yet.");
            }
        }
        return registry;
    }

    @Override
    public void setCoinModule(CoinModule module) {
        this.coinModule = module;
    }
}
