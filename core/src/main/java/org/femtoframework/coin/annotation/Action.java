package org.femtoframework.coin.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.femtoframework.coin.annotation.Constants.USE_DEFAULT_NAME;

/**
 * An annotation to indicate the method could be invoked from Remote, API or JMX.
 *
 * 0. Method has only non-map or non-object argument(s) (Primitive, String, Enum, Array of non-structure).
 * 1. Method must can be identified by its name, so method should not have overloading methods.
 * 2. Method whose annotated with @Action, is considered as action.
 * 3. If a method is considered as an Action, it won't be considered as Getter or Setter.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Action {
    /**
     * For renaming the action
     *
     * @return renaming of this action
     */
    String value() default USE_DEFAULT_NAME;

    enum Impact {

        /**
         * Unknown
         */
        UNKNOWN,

        /**
         * The method only returns information
         */
        INFO,

        /**
         * The method only takes action, has no return information
         */
        ACTION,

        /**
         * The method does take action and return information
         */
        ACTION_INFO
    }

    /**
     * What's the impact?
     *
     * @return Impact
     */
    Impact impact() default Impact.UNKNOWN;
}
