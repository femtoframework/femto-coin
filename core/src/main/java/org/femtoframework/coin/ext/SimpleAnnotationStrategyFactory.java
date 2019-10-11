package org.femtoframework.coin.ext;

import org.femtoframework.bean.BeanStage;
import org.femtoframework.coin.AnnotationStrategy;
import org.femtoframework.coin.AnnotationStrategyFactory;
import org.femtoframework.coin.CoinConstants;
import org.femtoframework.coin.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

/**
 * Simple Annotation Strategy Factory
 */
public class SimpleAnnotationStrategyFactory extends BaseResourceFactory<AnnotationStrategy> implements AnnotationStrategyFactory {

    protected SimpleAnnotationStrategyFactory() {
        super(null, CoinConstants.NAMESPACE_COIN);
    }

    @Override
    public void created(AnnotatedElement element, Component component) {
        handleAnnotation(BeanStage.CREATE, element, component);
    }

    protected void handleAnnotation(BeanStage stage, AnnotatedElement element, Component component) {
        Annotation[] annotations = element.getAnnotations();
        if (annotations.length > 0 && !map.isEmpty()) {
            //Go through annotations
            for(Annotation  annotation: annotations) {
                AnnotationStrategy strategy = get(annotation.getClass().getName());
                if (strategy != null) {
                    switch (stage) {
                        case CREATE:
                            strategy.created(annotation, element, component);
                            break;
                        case CONFIGURE:
                            strategy.configure(annotation, element, component);
                            break;
                        case INITIALIZE:
                            strategy.init(annotation, element, component);
                            break;
                        case START:
                            strategy.start(annotation, element, component);
                            break;
                        case STOP:
                            strategy.stop(annotation, element, component);
                            break;
                        case DESTROY:
                            strategy.destroy(annotation, element, component);
                            break;
                    }
                }
            }
        }
    }

    @Override
    public void configure(AnnotatedElement element, Component component) {
        handleAnnotation(BeanStage.CONFIGURE, element, component);
    }

    @Override
    public void init(AnnotatedElement element, Component component) {
        handleAnnotation(BeanStage.INITIALIZE, element, component);
    }

    @Override
    public void start(Annotation annotation, AnnotatedElement element, Component component) {
        handleAnnotation(BeanStage.START, element, component);
    }

    @Override
    public void stop(AnnotatedElement element, Component component) {
        handleAnnotation(BeanStage.STOP, element, component);
    }

    @Override
    public void destroy(AnnotatedElement element, Component component) {
        handleAnnotation(BeanStage.DESTROY, element, component);
    }
}


