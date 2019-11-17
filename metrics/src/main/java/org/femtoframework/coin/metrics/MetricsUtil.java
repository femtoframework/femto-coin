package org.femtoframework.coin.metrics;

import io.micrometer.core.instrument.FunctionCounter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import org.femtoframework.bean.info.BeanInfo;
import org.femtoframework.bean.info.PropertyInfo;
import org.femtoframework.coin.CoinUtil;
import org.femtoframework.coin.metrics.annotations.Metric;
import org.femtoframework.coin.metrics.annotations.Tag;
import org.femtoframework.util.DataUtil;

import javax.naming.NamingException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MetricsUtil {

    private static CompositeMeterRegistry defaultRegistry;

    public static CompositeMeterRegistry getDefaultRegistry() {
        if (defaultRegistry == null) {
            try {
                MetricsService service = (MetricsService) CoinUtil.getModule().getLookup().lookupBean("coin-metrics:metrics-service");
                defaultRegistry = service.getCompositeMeterRegistry();
            }
            catch(NamingException ne) {
                //Ignore
            }
        }
        return defaultRegistry;
    }

    /**
     * Registry Metrics by scanning the properties
     *
     * @param bean
     * @param beanInfo
     */
    public static List<Meter.Id> registryMetrics(Object bean, BeanInfo beanInfo) {
        return registryMetrics(getDefaultRegistry(), bean, beanInfo);
    }

    /**
     * Registry Metrics by scanming the properties
     *
     * @param registry
     * @param bean
     * @param beanInfo
     */
    public static List<Meter.Id> registryMetrics(CompositeMeterRegistry registry, Object bean, BeanInfo beanInfo) {
        List<Meter.Id> list = null;
        for(PropertyInfo propertyInfo : beanInfo.getProperties()) {
            if (propertyInfo.isReadable()) {
                Method method = propertyInfo.getGetterMethod();
                if (method != null) {
                    Metric metric = method.getAnnotation(Metric.class);
                    Meter meter = null;
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
                                meter = fb.register(registry);
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
                                meter = gb.register(registry);
                                break;
                        }

                        if (meter != null) {
                            if (list == null) {
                                list = new ArrayList<>(4);
                            }
                            list.add(meter.getId());
                        }
                    }
                }
            }
        }
        return list;
    }

    private static String getTagValue(Object bean, BeanInfo beanInfo, String valueExpression) {
        if (valueExpression == null) {
            return null;
        }
        if (valueExpression.startsWith("${") && valueExpression.endsWith("}")) {
            String tagProperty = valueExpression.substring(2, valueExpression.length()-1);
            if (tagProperty.indexOf('.') > 0) { //System.getProperties
                return System.getProperty(tagProperty);
            } else {
                if (tagProperty.toUpperCase().equals(tagProperty)) { // System.env
                    return System.getenv(tagProperty);
                }
                else {
                    PropertyInfo tagPropertyInfo = beanInfo.getProperty(tagProperty);
                    if (tagPropertyInfo == null) {
                        return valueExpression;
                    } else {
                        return String.valueOf((Object) tagPropertyInfo.invokeGetter(bean));
                    }
                }
            }
        }
        return valueExpression;
    }
}
