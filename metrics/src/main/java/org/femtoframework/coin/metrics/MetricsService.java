/**
* Copyright 2017 Pivotal Software, Inc.
* <p>
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
* <p>
* http://www.apache.org/licenses/LICENSE-2.0
* <p>
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.femtoframework.coin.metrics;

import org.femtoframework.bean.AbstractLifecycle;
import org.femtoframework.coin.BeanFactory;
import org.femtoframework.coin.metrics.export.prometheus.PrometheusEndpoint;
import org.femtoframework.coin.metrics.export.prometheus.PrometheusProperties;
import org.femtoframework.coin.metrics.export.prometheus.PrometheusPropertiesConfigAdapter;
import io.micrometer.core.instrument.Clock;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics;
import io.micrometer.core.instrument.binder.system.FileDescriptorMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import io.micrometer.core.instrument.binder.system.UptimeMetrics;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import io.micrometer.core.instrument.config.MeterFilter;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.prometheus.client.CollectorRegistry;
import org.femtoframework.coin.spi.BeanFactoryAware;

import java.util.List;

/**
 *
 * @author Jon Schneider
 */
public class MetricsService extends AbstractLifecycle implements BeanFactoryAware {

    private Clock micrometerClock = Clock.SYSTEM;
    private MeterRegistryPostProcessor meterRegistryPostProcessor;
    private MetricsProperties metricsProperties = new MetricsProperties();

//    @Bean
//    public Clock micrometerClock() {
//        return Clock.SYSTEM;
//    }
//
//    @Bean
//    public static MeterRegistryPostProcessor meterRegistryPostProcessor() {
//        return new MeterRegistryPostProcessor();
//    }
//
//    @Bean
//    public MetricsProperties metricsProperties() {
//        return new MetricsProperties();
//    }

    private PrometheusProperties prometheusProperties = new PrometheusProperties();

//    @Bean
//    public PrometheusProperties prometheusProperties() {
//        return getPrometheusProperties()
//    }


    private PropertiesMeterFilter propertiesMeterFilter;

    private PrometheusPropertiesConfigAdapter prometheusConfig;

    private PrometheusMeterRegistry prometheusMeterRegistry;

    private CollectorRegistry collectorRegistry = new CollectorRegistry(true);

//    @Bean
//    @Order(0)
//    public PropertiesMeterFilter propertiesMeterFilter(MetricsProperties properties) {
//        return new PropertiesMeterFilter(properties);
//    }
//
//
//    @Bean
//    public PrometheusConfig prometheusConfig() {
//        return new PrometheusPropertiesConfigAdapter(getPrometheusProperties());
//    }
//    @Bean
//    public PrometheusMeterRegistry prometheusMeterRegistry(PrometheusConfig config, CollectorRegistry collectorRegistry,
//                                                           Clock clock) {
//
//    }
//
//    @Bean
//    public CollectorRegistry collectorRegistry() {
//        return new CollectorRegistry(true);
//    }

    private MeterFilter metricsHttpServerUriTagFilter;

    public MeterFilter metricsHttpServerUriTagFilter(MetricsProperties properties) {
        String metricName = properties.getWeb().getServer().getRequestsMetricName();
        MeterFilter filter = new OnlyOnceLoggingDenyMeterFilter(() -> String
                .format("Reached the maximum number of URI tags for '%s'.", metricName));
        return MeterFilter.maximumAllowableTags(metricName, "uri",
                properties.getWeb().getServer().getMaxUriTags(), filter);
    }

    private CompositeMeterRegistry noOpMeterRegistry = null;

//    @Bean
    public CompositeMeterRegistry noOpMeterRegistry(Clock clock) {
        return new CompositeMeterRegistry(clock);
    }

//    @Bean
//    @Primary
    private CompositeMeterRegistry compositeMeterRegistry;

    public CompositeMeterRegistry compositeMeterRegistry(Clock clock, List<MeterRegistry> registries) {
        return new CompositeMeterRegistry(clock, registries);
    }

    private UptimeMetrics uptimeMetrics = new UptimeMetrics();

//    @Bean
//    public UptimeMetrics uptimeMetrics() {
//        return new UptimeMetrics();
//    }

    private ProcessorMetrics processorMetrics = new ProcessorMetrics();

//    @Bean
//    public ProcessorMetrics processorMetrics() {
//        return new ProcessorMetrics();
//    }

    private FileDescriptorMetrics fileDescriptorMetrics = new FileDescriptorMetrics();

//    @Bean
//    public FileDescriptorMetrics fileDescriptorMetrics() {
//        return new FileDescriptorMetrics();
//    }


    private JvmGcMetrics jvmGcMetrics = new JvmGcMetrics();
    //JVM
//    @Bean
//    public JvmGcMetrics jvmGcMetrics() {
//        return new JvmGcMetrics();
//    }

    private JvmMemoryMetrics jvmMemoryMetrics = new JvmMemoryMetrics();

//    @Bean
//    public JvmMemoryMetrics jvmMemoryMetrics() {
//        return new JvmMemoryMetrics();
//    }

    private JvmThreadMetrics jvmThreadMetrics = new JvmThreadMetrics();

//    @Bean
//    public JvmThreadMetrics jvmThreadMetrics() {
//        return new JvmThreadMetrics();
//    }

    private ClassLoaderMetrics classLoaderMetrics = new ClassLoaderMetrics();

//    @Bean
//    public ClassLoaderMetrics classLoaderMetrics() {
//        return new ClassLoaderMetrics();
//    }

    public Clock getMicrometerClock() {
        return micrometerClock;
    }

    public MetricsProperties getMetricsProperties() {
        return metricsProperties;
    }

    public MeterRegistryPostProcessor getMeterRegistryPostProcessor() {
        return meterRegistryPostProcessor;
    }

    public PrometheusProperties getPrometheusProperties() {
        return prometheusProperties;
    }

    private BeanFactory beanFactory;

    private PrometheusEndpoint prometheusEndpoint;

    @Override
    public void _doInit() {
        propertiesMeterFilter = new PropertiesMeterFilter(metricsProperties);
        prometheusConfig = new PrometheusPropertiesConfigAdapter(getPrometheusProperties());
        prometheusMeterRegistry = new PrometheusMeterRegistry(getPrometheusConfig(), getCollectorRegistry(), getMicrometerClock());

        metricsHttpServerUriTagFilter = metricsHttpServerUriTagFilter(metricsProperties);
        noOpMeterRegistry = noOpMeterRegistry(getMicrometerClock());

        meterRegistryPostProcessor = new MeterRegistryPostProcessor(metricsProperties);
        meterRegistryPostProcessor.setBeanFactory(beanFactory);
        meterRegistryPostProcessor.init();

        compositeMeterRegistry = compositeMeterRegistry(getMicrometerClock(), meterRegistryPostProcessor.getRegistries());

        if (prometheusEndpoint == null) {
            prometheusEndpoint = new PrometheusEndpoint();
        }
        prometheusEndpoint.setCollectorRegistry(prometheusMeterRegistry.getPrometheusRegistry());
        prometheusEndpoint.init();
    }

    /**
     * Start internally
     */
    @Override
    public void _doStart() {
        meterRegistryPostProcessor.start();
        if (prometheusEndpoint != null) {
            prometheusEndpoint.start();
        }
    }


    @Override
    public void _doStop() {
        if (prometheusEndpoint != null) {
            prometheusEndpoint.stop();
        }
        meterRegistryPostProcessor.stop();
    }

    @Override
    public void _doDestroy() {
        if (prometheusEndpoint != null) {
            prometheusEndpoint.destroy();
            prometheusEndpoint = null;
        }
        if (meterRegistryPostProcessor != null) {
            meterRegistryPostProcessor.destroy();
            meterRegistryPostProcessor = null;
        }
    }

    public PropertiesMeterFilter getPropertiesMeterFilter() {
        return propertiesMeterFilter;
    }

    public PrometheusPropertiesConfigAdapter getPrometheusConfig() {
        return prometheusConfig;
    }

    public PrometheusMeterRegistry getPrometheusMeterRegistry() {
        return prometheusMeterRegistry;
    }

    public CollectorRegistry getCollectorRegistry() {
        return collectorRegistry;
    }

    public MeterFilter getMetricsHttpServerUriTagFilter() {
        return metricsHttpServerUriTagFilter;
    }

    public CompositeMeterRegistry getNoOpMeterRegistry() {
        return noOpMeterRegistry;
    }

    public CompositeMeterRegistry getCompositeMeterRegistry() {
        return compositeMeterRegistry;
    }

    public UptimeMetrics getUptimeMetrics() {
        return uptimeMetrics;
    }

    public ProcessorMetrics getProcessorMetrics() {
        return processorMetrics;
    }

    public FileDescriptorMetrics getFileDescriptorMetrics() {
        return fileDescriptorMetrics;
    }

    public JvmGcMetrics getJvmGcMetrics() {
        return jvmGcMetrics;
    }

    public JvmMemoryMetrics getJvmMemoryMetrics() {
        return jvmMemoryMetrics;
    }

    public JvmThreadMetrics getJvmThreadMetrics() {
        return jvmThreadMetrics;
    }

    public ClassLoaderMetrics getClassLoaderMetrics() {
        return classLoaderMetrics;
    }

    @Override
    public void setBeanFactory(BeanFactory factory) {
        this.beanFactory = factory;
    }

    public PrometheusEndpoint getPrometheusEndpoint() {
        return prometheusEndpoint;
    }

    public void setPrometheusEndpoint(PrometheusEndpoint prometheusEndpoint) {
        this.prometheusEndpoint = prometheusEndpoint;
    }
}
