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

import io.micrometer.core.instrument.binder.MeterBinder;
import org.femtoframework.bean.AbstractLifecycle;
import org.femtoframework.coin.metrics.export.prometheus.PrometheusEndpoint;
import org.femtoframework.coin.metrics.export.prometheus.PrometheusProperties;
import org.femtoframework.coin.metrics.export.prometheus.PrometheusPropertiesConfigAdapter;
import io.micrometer.core.instrument.Clock;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import io.micrometer.core.instrument.config.MeterFilter;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.prometheus.client.CollectorRegistry;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jon Schneider
 */
public class MetricsService extends AbstractLifecycle {

    private Clock micrometerClock = Clock.SYSTEM;
    private MetricsProperties metricsProperties = new MetricsProperties();

    private List<MeterBinder> meterBinders = new ArrayList<>();

    private List<MeterRegistry> registries = new ArrayList<>();

    private List<MeterFilter> meterFilters = new ArrayList<>();

    private PrometheusProperties prometheusProperties = new PrometheusProperties();

    private PropertiesMeterFilter propertiesMeterFilter;

    private PrometheusPropertiesConfigAdapter prometheusConfig;

    private PrometheusMeterRegistry prometheusMeterRegistry;

    private List<MeterRegistryCustomizer<?>> customizers = new ArrayList<>();

    private CollectorRegistry collectorRegistry = new CollectorRegistry(true);

    private MeterFilter metricsHttpServerUriTagFilter;

    public MeterFilter metricsHttpServerUriTagFilter(MetricsProperties properties) {
        String metricName = properties.getWeb().getServer().getRequestsMetricName();
        MeterFilter filter = new OnlyOnceLoggingDenyMeterFilter(() -> String
                .format("Reached the maximum number of URI tags for '%s'.", metricName));
        return MeterFilter.maximumAllowableTags(metricName, "uri",
                properties.getWeb().getServer().getMaxUriTags(), filter);
    }

    private CompositeMeterRegistry noOpMeterRegistry = null;

    public CompositeMeterRegistry noOpMeterRegistry(Clock clock) {
        return new CompositeMeterRegistry(clock);
    }

    private CompositeMeterRegistry compositeMeterRegistry;

    public CompositeMeterRegistry compositeMeterRegistry(Clock clock, List<MeterRegistry> registries) {
        return new CompositeMeterRegistry(clock, registries);
    }

    public Clock getMicrometerClock() {
        return micrometerClock;
    }

    public MetricsProperties getMetricsProperties() {
        return metricsProperties;
    }

    public PrometheusProperties getPrometheusProperties() {
        return prometheusProperties;
    }

    private PrometheusEndpoint prometheusEndpoint;

    private volatile MeterRegistryConfigurer configurer;

    @Override
    public void _doInit() {
        propertiesMeterFilter = new PropertiesMeterFilter(metricsProperties);
        addMeterFilter(propertiesMeterFilter);
        prometheusConfig = new PrometheusPropertiesConfigAdapter(getPrometheusProperties());
        prometheusMeterRegistry = new PrometheusMeterRegistry(getPrometheusConfig(), getCollectorRegistry(), getMicrometerClock());
        getRegistries().add(prometheusMeterRegistry);

        metricsHttpServerUriTagFilter = metricsHttpServerUriTagFilter(metricsProperties);
        addMeterFilter(metricsHttpServerUriTagFilter);
        noOpMeterRegistry = noOpMeterRegistry(getMicrometerClock());
        getRegistries().add(noOpMeterRegistry);

        configurer = new MeterRegistryConfigurer(
                meterBinders,
                meterFilters,
                customizers, metricsProperties.isUseGlobalRegistry());

        compositeMeterRegistry = compositeMeterRegistry(getMicrometerClock(), getRegistries());

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
        for(MeterRegistry registry: getRegistries()) {
            configurer.configure(registry);
        }
        if (prometheusEndpoint != null) {
            prometheusEndpoint.start();
        }
    }


    @Override
    public void _doStop() {
        if (prometheusEndpoint != null) {
            prometheusEndpoint.stop();
        }
    }

    @Override
    public void _doDestroy() {
        if (prometheusEndpoint != null) {
            prometheusEndpoint.destroy();
            prometheusEndpoint = null;
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

    public PrometheusEndpoint getPrometheusEndpoint() {
        return prometheusEndpoint;
    }

    public void setPrometheusEndpoint(PrometheusEndpoint prometheusEndpoint) {
        this.prometheusEndpoint = prometheusEndpoint;
    }

    public void addMeterBinder(MeterBinder binder) {
        meterBinders.add(binder);
    }

    public List<MeterBinder> getMeterBinders() {
        return meterBinders;
    }

    public void setMeterBinders(List<MeterBinder> meterBinders) {
        this.meterBinders = meterBinders;
    }

    public void addMeterFilter(MeterFilter filter) {
        meterFilters.add(filter);
    }

    public List<MeterFilter> getMeterFilters() {
        return meterFilters;
    }

    public void setMeterFilters(List<MeterFilter> meterFilters) {
        this.meterFilters = meterFilters;
    }

    public void addCustomizer(MeterRegistryCustomizer customizer) {
        customizers.add(customizer);
    }

    public List<MeterRegistryCustomizer<?>> getCustomizers() {
        return customizers;
    }

    public void setCustomizers(List<MeterRegistryCustomizer<?>> customizers) {
        this.customizers = customizers;
    }

    public void addRegistry(MeterRegistry registry) {
        registries.add(registry);
    }

    public List<MeterRegistry> getRegistries() {
        return registries;
    }

    public void setRegistries(List<MeterRegistry> registries) {
        this.registries = registries;
    }
}
