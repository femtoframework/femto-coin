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

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;
import io.micrometer.core.instrument.config.MeterFilter;
import org.femtoframework.bean.AbstractLifecycle;
import org.femtoframework.coin.BeanFactory;
import org.femtoframework.coin.spi.BeanFactoryAware;

import java.util.*;

/**
 * {@link MeterRegistryConfigurer} to post-process {@link MeterRegistry} beans.
 *
 * @author Jon Schneider
 * @author Phillip Webb
 * @author Andy Wilkinson
 */
class MeterRegistryPostProcessor extends AbstractLifecycle implements BeanFactoryAware {

    private BeanFactory beanFactory;

    private MetricsProperties properties;

    private List<MeterRegistry> registries;

    private volatile MeterRegistryConfigurer configurer;

    MeterRegistryPostProcessor(MetricsProperties properties) {
        this.properties = properties;
    }

    @Override
    public void setBeanFactory(BeanFactory factory) {
        this.beanFactory = factory;
    }

    public void _doInit() {
        List<MeterRegistryCustomizer<?>> meterRegistryCustomizers = new ArrayList<>();

        List<MeterBinder> meterBinders = new ArrayList<>();
        List<MeterFilter> meterFilters = new ArrayList<>();
        List<MeterRegistry> registries = new ArrayList<>();

        for(Object bean: beanFactory) {
            if (bean instanceof MeterBinder) {
                meterBinders.add((MeterBinder)bean);
            }
            if (bean instanceof MeterFilter) {
                meterFilters.add((MeterFilter)bean);
            }
            if (bean instanceof MeterRegistryCustomizer) {
                meterRegistryCustomizers.add((MeterRegistryCustomizer)bean);
            }

            if (bean instanceof MeterRegistry) {
                registries.add((MeterRegistry)bean);
            }
        }
        this.registries = registries;
        configurer = new MeterRegistryConfigurer(
                meterBinders,
                meterFilters,
                meterRegistryCustomizers, properties.isUseGlobalRegistry());
    }

    /**
     * Start internally
     */
    public void _doStart() {
        for(MeterRegistry registry: getRegistries()) {
            configurer.configure(registry);
        }
    }

    public List<MeterRegistry> getRegistries() {
        return registries;
    }
}
