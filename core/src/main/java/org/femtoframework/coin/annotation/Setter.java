package org.femtoframework.coin.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.femtoframework.coin.annotation.Constants.USE_DEFAULT_NAME;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Setter
{
    /**
     * Optional default argument that defines logical property this
     * method is used to modify ("set")
     */
    String value() default USE_DEFAULT_NAME;
}
