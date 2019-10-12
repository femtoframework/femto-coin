package org.femtoframework.coin.metrics.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE})
@Documented
public @interface Tag {
    /**
     * Tag Name
     *
     * @return Metric Name
     */
    String name();

    /**
     * It should support template,
     * 1. Plain text:  "abc", "123456"
     * 2. Refer to a getter in CURRENT object:  "${tag_value}"  ${tag_value} means the return value of method "this.getTagValue()".
     * 3. You can wrap any logic in the getter to have any different kinds of tag value data sources.
     *
     * @return Tag Value
     */
    String value();
}
