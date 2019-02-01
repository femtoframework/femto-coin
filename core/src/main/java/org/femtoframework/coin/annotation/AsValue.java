package org.femtoframework.coin.annotation;

import java.lang.annotation.*;

/**
 * Consider the property value as the value of bean
 *
 * It is similar as @JsonValue in jackson
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface AsValue {
}
