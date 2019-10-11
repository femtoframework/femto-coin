package org.femtoframework.coin;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

/**
 * Annotation Strategy
 *
 * It is doing something on existing component in any statbe
 */
public interface AnnotationStrategy {

    /**
     * Create bean
     *
     * @param annotation The annotation class
     * @param element AnnotatedElement
     * @param component Component
     * @return the created bean
     */
    default void created(Annotation annotation, AnnotatedElement element, Component component) {

    }

    /**
     * Configure the bean
     *
     * @param annotation The annotation class
     * @param element AnnotatedElement
     * @param component Component
     */
    default void configure(Annotation annotation, AnnotatedElement element, Component component) {

    }

    /**
     * Initialize component
     *
     * @param annotation The annotation class
     * @param element AnnotatedElement
     * @param component
     */
    default void init(Annotation annotation, AnnotatedElement element, Component component) {

    }

    /**
     * Start component
     *
     * @param annotation The annotation class
     * @param element AnnotatedElement
     * @param component Component
     * @throws org.femtoframework.coin.exception.BeanLifecycleException
     */
    default void start(Annotation annotation, AnnotatedElement element, Component component) {

    }


    /**
     * Stop component
     *
     * @param annotation The annotation class
     * @param element AnnotatedElement
     * @param component Component
     * @throws org.femtoframework.coin.exception.BeanLifecycleException
     */
    default void stop(Annotation annotation, AnnotatedElement element, Component component) {

    }

    /**
     * Destroy component
     *
     * @param annotation The annotation class
     * @param element AnnotatedElement
     * @param component Component
     */
    default void destroy(Annotation annotation, AnnotatedElement element, Component component) {

    }
}
