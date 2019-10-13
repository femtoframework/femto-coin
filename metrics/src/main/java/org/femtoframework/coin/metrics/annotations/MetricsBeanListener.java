package org.femtoframework.coin.metrics.annotations;

import io.micrometer.core.instrument.FunctionCounter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import org.femtoframework.bean.BeanPhase;
import org.femtoframework.bean.info.BeanInfo;
import org.femtoframework.bean.info.PropertyInfo;
import org.femtoframework.coin.CoinModule;
import org.femtoframework.coin.Component;
import org.femtoframework.coin.event.BeanEvent;
import org.femtoframework.coin.event.BeanEventListener;
import org.femtoframework.coin.metrics.MetricsService;
import org.femtoframework.coin.spi.CoinModuleAware;
import org.femtoframework.util.DataUtil;

import javax.naming.NamingException;
import java.lang.reflect.Method;

/**
 * A bean listener to handle metrics related annotations
 *
 */
public class MetricsBeanListener implements BeanEventListener, CoinModuleAware
{

    private CompositeMeterRegistry registry;

    private CoinModule coinModule;

    @Override
    public void handleEvent(BeanEvent event) {
        BeanPhase phase = event.getPhase();
        Object bean = event.getComponent().getBean();
        if (phase == BeanPhase.STARTING) {
            Component component = event.getComponent();
            BeanInfo beanInfo = component.getBeanInfo();
            for(PropertyInfo propertyInfo : beanInfo.getProperties()) {
                if (propertyInfo.isReadable()) {
                    Method method = propertyInfo.getGetterMethod();
                    if (method != null) {
                        Metric metric = method.getAnnotation(Metric.class);
                        if (metric != null) {
                            switch (metric.type()) {
                                case COUNTER:
                                    FunctionCounter.Builder fb = FunctionCounter.builder(metric.name(), bean, v -> DataUtil.getDouble(propertyInfo.invokeGetter(bean), 0))
                                            .description(metric.description());
                                    for(Tag tag : metric.tags()) {
                                        String value = getTagValue(bean, beanInfo, tag.value());
                                        if (value != null) {
                                            fb.tag(tag.name(), value);
                                        }
                                    }
                                    fb.register(getRegistry());
                                    break;
                                case GAUGE:
                                    Gauge.Builder gb = Gauge.builder(metric.name(), bean, v -> DataUtil.getDouble(propertyInfo.invokeGetter(bean), 0))
                                            .description(metric.description());
                                    for(Tag tag : metric.tags()) {
                                        String value = getTagValue(bean, beanInfo, tag.value());
                                        if (value != null) {
                                            gb.tag(tag.name(), value);
                                        }
                                    }
                                    gb.register(getRegistry());
                                    break;
                            }

                        }
                    }
                }
            }
        }
    }

    private String getTagValue(Object bean, BeanInfo beanInfo, String valueExpression) {
        if (valueExpression == null) {
            return null;
        }
        if (valueExpression.startsWith("${") && valueExpression.endsWith("}")) {
            String tagProperty = valueExpression.substring(2, valueExpression.length()-3);
            PropertyInfo tagPropertyInfo = beanInfo.getProperty(tagProperty);
            if (tagPropertyInfo == null) {
                return valueExpression;
            }
            else {
                return String.valueOf(tagPropertyInfo.invokeGetter(bean));
            }
        }
        return valueExpression;
    }

    private CompositeMeterRegistry getRegistry() {
        if (registry == null) {
            try {
                MetricsService service = (MetricsService)coinModule.getLookup().lookupBean("coin-metrics:metrics-service");
                registry = service.getCompositeMeterRegistry();
            }
            catch(NamingException ne) {
                //Ignore
            }
        }
        return registry;
    }

    @Override
    public void setCoinModule(CoinModule module) {
        this.coinModule = module;
    }
}
