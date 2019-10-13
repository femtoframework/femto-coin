package org.femtoframework.coin.examples.metrics;

import org.femtoframework.bean.annotation.Property;
import org.femtoframework.coin.examples.service.CounterSample;
import org.femtoframework.coin.metrics.annotations.Metric;
import org.femtoframework.coin.metrics.annotations.Tag;

import java.util.concurrent.atomic.AtomicInteger;

public class MetricSampleBean implements CounterSample {

    private AtomicInteger counter = new AtomicInteger();

    private int myGauge = -1;

    public int incrementAndGet() {
        return counter.incrementAndGet();
    }

    @Metric(name="coin_metrics_sample_counter", description = "My description",
            tags = {@Tag(name="os", value="${tag_value}")})
    public int getCounter() {
        return counter.get();
    }

    @Metric(name="coin_metrics_sample_gauge", description = "My description",
            tags = {@Tag(name="os", value="${tag_value}")})
    public int getMyGauge() {
        return myGauge;
    }

    public void setMyGauge(int myGauge) {
        this.myGauge = myGauge;
    }

    @Property
    public String getTagValue() {
        return System.getProperty("os.name");
    }
}
