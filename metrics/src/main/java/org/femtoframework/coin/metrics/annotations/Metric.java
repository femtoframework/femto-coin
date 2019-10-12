package org.femtoframework.coin.metrics.annotations;

import java.lang.annotation.*;

/**
 * Metric
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface Metric {

    /**
     * Metric Type
     *
     * @return Metric Type
     */
    MetricType type() default MetricType.COUNTER;

    /**
     * Metric Name
     *
     * @return Metric Name
     */
    String name();

    /**
     * Metric Tags
     *
     * @return
     */
    Tag[] tags() default {};

    /**
     * Description
     *
     * @return Description
     */
    String description() default "";
}
