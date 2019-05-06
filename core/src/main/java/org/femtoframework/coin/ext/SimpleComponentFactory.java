package org.femtoframework.coin.ext;

import org.femtoframework.bean.BeanPhase;
import org.femtoframework.bean.BeanStage;
import org.femtoframework.coin.*;
import org.femtoframework.bean.annotation.Coined;
import org.femtoframework.bean.annotation.Ignore;
import org.femtoframework.coin.exception.BeanCreationException;
import org.femtoframework.coin.exception.BeanNotExpectedException;
import org.femtoframework.coin.spec.BeanSpec;
import org.femtoframework.coin.spec.SpecFactory;
import org.femtoframework.coin.spec.element.BeanElement;
import org.femtoframework.coin.status.BeanStatus;
import org.femtoframework.coin.util.CoinNameUtil;
import org.femtoframework.implement.ImplementConfig;
import org.femtoframework.implement.ImplementUtil;
import org.femtoframework.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.ManagedBean;
import java.util.Iterator;
import java.util.Map;

/**
 * Simple Component ResourceFactory
 *
 * @author Sheldon Shao
 * @version 1.0
 */
@Coined
public class SimpleComponentFactory extends BaseResourceFactory<Component> implements ComponentFactory {

    @Ignore
    private SpecFactory<? extends BeanSpec> specFactory;

    @Ignore
    private Logger log = LoggerFactory.getLogger(SimpleComponentFactory.class);

    @Ignore
    private LifecycleStrategy strategy;

    @Ignore
    private CoinModule module;

//
//    @Ignore
//    private DefaultComponentFactory defaultComponentFactory;

    public SimpleComponentFactory(CoinModule module, SpecFactory<? extends BeanSpec> specFactory, LifecycleStrategy strategy) {
        super(module.getNamespaceFactory(), specFactory.getNamespace());
        this.module = module;
//        this.defaultComponentFactory = module.getDefaultComponentFactory();
        this.specFactory = specFactory;
        this.strategy = strategy;
    }

    /**
     * Create component on-demand
     *
     * @param name        Component name, the name could be null, if it is null, the created object won'be
     * @param implClass   Implement class
     * @param targetStage TargetStage, since the required implementation should have same stage with the parent bean
     * @return
     */
    @Override
    public Component create(String name, Class<?> implClass, BeanStage targetStage) {
        name = checkName(name, implClass);
        BeanElement spec = new BeanElement(getNamespace(), name, implClass);
        return doCreate(null, spec, targetStage);
    }

    protected String checkName(String name, Class<?> implClass) {
        String newBeanName = name;
        ManagedBean mb = implClass.getAnnotation(ManagedBean.class);
        if (mb != null) {
            String[] names = CoinNameUtil.splitName(mb.value());
            newBeanName = names[1];
            String newBeanNamespace = names[0];
            if (StringUtil.isValid(newBeanNamespace) && !StringUtil.equals(newBeanNamespace, getNamespace())) {
                throw new BeanNotExpectedException("The expected namespace is " + getNamespace()
                        + ", but declared namespace is " + newBeanNamespace);
            }
            else if (StringUtil.isValid(newBeanName)){
                if (!StringUtil.equals(newBeanName, name)) {
                    throw new BeanNotExpectedException(name, newBeanName);
                }
            }
        }
//        else if (StringUtil.isInvalid(name)) {
//            throw new BeanNotExpectedException(name, implClass.getSimpleName());
//        }
        return newBeanName;
    }

    /**
     * Create component by BeanSpec
     *
     * @param name        Bean Name
     * @param spec        Bean Spec
     * @param targetStage BeanStage
     */
    @Override
    public Component create(String name, BeanSpec spec, BeanStage targetStage) {
        checkName(name, spec.getTypeClass());
        return doCreate(null, spec, targetStage);
    }

    /**
     * Create component by existing bean
     *
     * @param name        Bean Name
     * @param bean        Bean
     * @param targetStage BeanStage
     */
    @Override
    public Component create(String name, Object bean, BeanStage targetStage) {
        checkName(name, bean.getClass());
        BeanSpec spec = new BeanElement(getNamespace(), name, bean.getClass());
        return doCreate(bean, spec, targetStage);
    }

    /**
     * Keep component in the given stage
     *
     * @param component   Component
     * @param targetStage Target Stage
     * @return Current stage of component
     */
    @Override
    public BeanPhase keep(Component component, BeanStage targetStage) {
        BeanPhase expectedPhase = BeanPhase.expectedPhase(targetStage);
        BeanStatus status = component.getStatus();

        int expectedPhaseId = expectedPhase.ordinal();
        if (expectedPhaseId > status.getPhase().ordinal()) {
            if (!status.getPhase().isRunning()) { //Some other is running
                int currentPhaseId = status.getPhase().ordinal();
                if (currentPhaseId < expectedPhaseId  && currentPhaseId < BeanPhase.CONFIGURED.ordinal()) {
                    strategy.configure(component);
                }
                currentPhaseId = status.getPhase().ordinal();
                if (currentPhaseId < expectedPhaseId && currentPhaseId < BeanPhase.INITIALIZED.ordinal()) {
                    strategy.init(component);
                }
                currentPhaseId = status.getPhase().ordinal();
                if (currentPhaseId < expectedPhaseId && currentPhaseId < BeanPhase.STARTED.ordinal()) {
                    strategy.start(component);
                }
                currentPhaseId = status.getPhase().ordinal();
                if (currentPhaseId < expectedPhaseId && currentPhaseId < BeanPhase.STOPPED.ordinal()) {
                    strategy.stop(component);
                }
                currentPhaseId = status.getPhase().ordinal();
                if (currentPhaseId < expectedPhaseId && currentPhaseId < BeanPhase.DESTROYED.ordinal()) {
                    strategy.destroy(component);
                }
            }
        }
        return status.getPhase();
    }


    /**
     * Delete the object by given name
     *
     * @param name Name
     * @return Deleted object
     */
    @Override
    public Component delete(String name) {
        Component component = super.delete(name);
        if (component != null) {
            component.setStage(BeanStage.DESTROY);
            keep(component, BeanStage.DESTROY);
        }
        return component;
    }


    protected Component doCreate(Object bean, BeanSpec spec, BeanStage targetStage) {
        SimpleComponent component = new SimpleComponent(module, getNamespace(), spec);
        component.setStage(targetStage);
        component.setBean(bean);
        bean = createBean(component);
        if (StringUtil.isValid(component.getName())) {
            //Only named component will be added in this factory, otherwise it will be linked to parent component
            add(component);
            component.getCurrentNamespace().getBeanFactory().add(component.getName(), bean);
        }

//        if (spec instanceof ComponentSpec) {
//            List<String> defaultFor = ((ComponentSpec)spec).getDefaultFor();
//            if (defaultFor != null) {
//                for(String clazz : defaultFor) {
//                    Class<?> defaultForClass = null;
//                    try {
//                        defaultForClass = Reflection.loadClass(clazz);
//                        defaultComponentFactory.add(defaultForClass, component);
//                    } catch (ClassNotFoundException cnfe) {
//                        log.error("The 'defaultFor' interface is not found:" + clazz, cnfe);
//                    }
//                }
//            }
//        }
        return component;
    }

    /**
     * Create Object in Component
     *
     * @param component Component
     */
    protected Object createBean(SimpleComponent component) {
        Object bean = component.getBean();
        if (bean == null) {
            bean = strategy.create(component);
        }
        if (bean == null) {
            throw new BeanCreationException("Can not create the bean, namespace:" + getNamespace() + " bean name:" + component.getName());
        }
        component.setBean(bean);
        keep(component, component.getStage());
        return bean;
    }

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
    @Override
    public Class<?> getImplement(String name, Class<?> interfaceClass) {
        Class<?> implClass = null;
        if (StringUtil.isValid(name)) {
            BeanSpec spec = specFactory.get(name);
            if (spec != null) {
                implClass = spec.getTypeClass();
                if (implClass != null) {
                    return implClass;
                }
            }
        }



        implClass = ImplementUtil.getImplement(interfaceClass);
        if (implClass == null && StringUtil.isValid(name)) {
            Map<String, ImplementConfig<?>> map = ImplementUtil.getImplementManager().getMultipleImplements(interfaceClass);
            ImplementConfig config = map.get(name);
            return config != null ? config.getImplementationClass() : null;
        }

        Iterator<? extends BeanSpec> it = specFactory.iterator();
        BeanSpec found = null;
        while (it.hasNext()) {
            BeanSpec spec = it.next();
            Class<?> typeClass = spec.getTypeClass();
            if (typeClass != null) {
                if (interfaceClass.isAssignableFrom(typeClass)) {
                    if (found != null) {
                        log.warn("More than one implementation can be injected for interface=" + interfaceClass);
                        break;
                    }
                    else {
                        found = spec;
                        implClass = typeClass;
                    }
                }
            }
        }
        return implClass;
    }


    public SpecFactory getSpecFactory() {
        return specFactory;
    }
}
