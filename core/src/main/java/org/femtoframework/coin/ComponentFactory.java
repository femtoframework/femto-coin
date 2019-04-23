package org.femtoframework.coin;

import org.femtoframework.bean.BeanPhase;
import org.femtoframework.bean.BeanStage;
import org.femtoframework.coin.spec.BeanSpec;

/**
 * Component ResourceFactory
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public interface ComponentFactory extends ResourceFactory<Component> {

    /**
     * Create component on-demand
     *
     * @param name Component name, the name could be null, if it is null, the created object won'be
     * @param implClass Implement class, if it is null,
     * @param targetStage TargetStage, since the required implementation should have same stage with the parent bean
     * @return
     */
    Component create(String name, Class<?> implClass, BeanStage targetStage);

    /**
     * Create component by BeanSpec
     *
     * @param name Bean Name
     * @param spec Bean Spec
     * @param targetStage BeanStage
     */
    Component create(String name, BeanSpec spec, BeanStage targetStage);

    /**
     * Create component by existing bean
     *
     * @param name Bean Name
     * @param bean Bean
     * @param targetStage BeanStage
     */
    Component create(String name, Object bean, BeanStage targetStage);


    /**
     * Keep component in the given stage
     *
     * @param component Component
     * @param targetStage Target Stage
     * @return Current BeanPhase of component
     */
    BeanPhase keep(Component component, BeanStage targetStage);

    /**
     * Get the default implementation by interface class
     * Priority:
     * 1. Check BeanSpec if there is a BeanSpec associate with given name, and it has indicated "_kind", picks the "_kind" as implementation
     * 2. Check @ImplementedBy, if the interface indicates this annotation, use the implementation.
     * 3. Check the /META-INF/spec/ files
     * 4. Match the interface class in all bean specs in the namespace, check whether there is bean implements this interface
     *
     * @param name Bean Name, it could be null
     * @param interfaceClass Interface Class
     * @return The implement class, return null, if it is not able to find a right implementation
     */
    Class<?> getImplement(String name, Class<?> interfaceClass);
}
