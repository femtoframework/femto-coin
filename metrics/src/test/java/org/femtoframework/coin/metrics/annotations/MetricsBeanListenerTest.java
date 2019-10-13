package org.femtoframework.coin.metrics.annotations;

import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import io.micrometer.core.instrument.search.RequiredSearch;
import org.femtoframework.coin.CoinController;
import org.femtoframework.coin.CoinModule;
import org.femtoframework.coin.CoinUtil;
import org.femtoframework.coin.metrics.MetricsService;
import org.femtoframework.util.nutlet.NutletUtil;

import java.net.URI;
import java.util.List;

import static org.junit.Assert.*;

public class MetricsBeanListenerTest {

    @org.junit.Test
    public void handleEvent() throws Exception {
        CoinModule coinModule = CoinUtil.newModule();
        CoinController controller = coinModule.getController();
        List<URI> componentYamls = controller.getComponentYamls(null);

        componentYamls.add(NutletUtil.getResourceAsFile("org/femtoframework/coin/metrics/annotations/component.yaml").toURI());

        controller.create(componentYamls.toArray(new URI[componentYamls.size()]));

        MetricSampleBean bean = (MetricSampleBean)coinModule.getLookup().lookupBean("coin-metrics-sample:sample-bean");
        assertNotNull(bean);

        MetricsService service = (MetricsService)coinModule.getLookup().lookupBean("coin-metrics:metrics-service");
        assertNotNull(service);
        CompositeMeterRegistry registry = service.getCompositeMeterRegistry();
        RequiredSearch search = registry.get("coin_metrics_sample_counter");
        assertEquals(bean.incrementAndGet(), (int)search.functionCounter().count());

        bean.incrementAndGet();
        int newCounter = bean.incrementAndGet();
        assertEquals(newCounter, (int)search.functionCounter().count());
    }
}