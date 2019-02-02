package org.femtoframework.coin.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(value={ANNOTATION_TYPE,METHOD})
@Retention(value=RUNTIME)
public @interface AnyGetter {
}
