package org.femtoframework.coin.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Marker annotation that can be used to define a non-static, two-argument method (first argument name of property, second value to set),
 * to be used as a "fallback" handler for all otherwise unrecognized properties found from JSON content.
 * It is similar to XmlAnyElement in behavior; and can only be used to denote a single property per type.
 * If used, all otherwise unmapped key-value pairs from JSON Object values are added to the property (of type Map or bean).
 *
 * JsonAnySetter
 */
@Target(value={ANNOTATION_TYPE,METHOD})
@Retention(value=RUNTIME)
public @interface AnySetter {
}
