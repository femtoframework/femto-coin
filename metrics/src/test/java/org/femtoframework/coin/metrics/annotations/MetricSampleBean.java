package org.femtoframework.coin.metrics.annotations;

import java.util.concurrent.atomic.AtomicInteger;

public class MetricSampleBean {

    private AtomicInteger counter = new AtomicInteger();

    private int myGauge = -1;

    public int incrementAndGet() {
        return counter.incrementAndGet();
    }

    @Metric(name="coin_metrics_sample_counter", description = "My description",
    tags = {@Tag(name="os", value="${tag_value}")})
    public AtomicInteger getCounter() {
        return counter;
    }

    public void setCounter(AtomicInteger counter) {
        this.counter = counter;
    }

    @Metric(name="coin_metrics_sample_gauge", description = "My description",
            tags = {@Tag(name="os", value="${tag_value}")})
    public int getMyGauge() {
        return myGauge;
    }

    public void setMyGauge(int myGauge) {
        this.myGauge = myGauge;
    }

    public String getTagValue() {
        return System.getProperty("os.name");
    }
}
