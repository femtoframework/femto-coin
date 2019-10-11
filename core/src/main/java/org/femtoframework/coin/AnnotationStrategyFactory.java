package org.femtoframework.coin;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

/**
 * Annotation Strategy Factory
 *
 */
public interface AnnotationStrategyFactory extends ResourceFactory<AnnotationStrategy> {

    /**
     * Create bean
     *
     * @param element AnnotatedElement
     * @param component Component
     * @return the created bean
     */
    void created(AnnotatedElement element, Component component);

    /**
     * Configure the bean
     *
     * @param element AnnotatedElement
     * @param component Component
     */
    void configure(AnnotatedElement element, Component component);

    /**
     * Initialize component
     *
     * @param element AnnotatedElement
     * @param component
     */
    void init(AnnotatedElement element, Component component);

    /**
     * Start component
     *
     * @param annotation The annotation class
     * @param element AnnotatedElement
     * @param component Component
     * @throws org.femtoframework.coin.exception.BeanLifecycleException
     */
    void start(Annotation annotation, AnnotatedElement element, Component component);

    /**
     * Stop component
     *
     * @param element AnnotatedElement
     * @param component Component
     * @throws org.femtoframework.coin.exception.BeanLifecycleException
     */
    void stop(AnnotatedElement element, Component component);

    /**
     * Destroy component
     *
     * @param element AnnotatedElement
     * @param component Component
     */
    void destroy(AnnotatedElement element, Component component);
}
