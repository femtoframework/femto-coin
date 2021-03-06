package org.femtoframework.coin.ext;

import org.femtoframework.bean.*;
import org.femtoframework.coin.*;
import org.femtoframework.coin.configurator.AutoConfigurator;
import org.femtoframework.coin.event.BeanEventListeners;
import org.femtoframework.coin.event.BeanEventSupport;
import org.femtoframework.coin.remote.RemoteGenerator;
import org.femtoframework.coin.spec.BeanSpec;
import org.femtoframework.coin.spec.ComponentSpec;
import org.femtoframework.coin.spec.MapSpec;
import org.femtoframework.coin.spec.RemoteSpec;
import org.femtoframework.implement.ImplementUtil;
import org.femtoframework.lang.reflect.NoSuchClassException;
import org.femtoframework.lang.reflect.ObjectCreationException;
import org.femtoframework.lang.reflect.Reflection;
import org.femtoframework.lang.reflect.ReflectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;

/**
 * Simple Lifecycle Strategy
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public class SimpleLifecycleStrategy implements LifecycleStrategy, InitializableMBean {

    private Logger log = LoggerFactory.getLogger(SimpleLifecycleStrategy.class);

    private Configurator autoConfigurator = new AutoConfigurator();

    private ConfiguratorFactory configuratorFactory;

    private BeanEventSupport eventSupport;

    private RemoteGenerator generator = RemoteGenerator.NONE;


    public SimpleLifecycleStrategy(CoinModule coinModule, ConfiguratorFactory configuratorFactory) {
        this.configuratorFactory = configuratorFactory;
        this.eventSupport = new BeanEventSupport();
        eventSupport.setCoinModule(coinModule);
        this.eventSupport.addListener(new SimpleStatusUpdater());
    }

    public BeanEventListeners getEventListeners() {
        return eventSupport;
    }

    /**
     * Create bean
     *
     * @param component Component
     * @return the created bean
     */
    @Override
    public Object create(Component component) {
        String beanName = component.getName();
        BeanSpec spec = component.getSpec();

        Object bean = null;
        if (spec instanceof RemoteSpec) {
            RemoteSpec remoteSpec = (RemoteSpec)spec;

            eventSupport.fireEvent(BeanPhase.ENABLED, component);
            eventSupport.fireEvent(BeanPhase.CREATING, component);
            bean = generator.generate(Object.class.getName(), remoteSpec.getUri(),
                    remoteSpec.getInterfaces());
            eventSupport.fireEvent(BeanPhase.CREATED, component);
        }
        else {
            String className = spec.getType();
            if (className == null) {
                throw new ObjectCreationException("No class found in bean, " + component.getNamespace() + ":" + beanName);
            }


            Class<?> clazz = null;
            try {
                clazz = Reflection.getClass(className);
            } catch (ClassNotFoundException cnfe) {
                throw new NoSuchClassException("Load class " + className + " error:", cnfe);
            } catch (NoClassDefFoundError ndfe) {
                throw new NoSuchClassException("Load class " + className + " error:", ndfe);
            }

            eventSupport.fireEvent(BeanPhase.ENABLED, component);

            if (beanName == null) {
                //Try to get name from Annotation
                ManagedBean mb = clazz.getAnnotation(ManagedBean.class);
                if (mb != null) {
                    int index;
                    String qName = mb.value();
                    if ((index = qName.indexOf(':')) > 0) {
                        beanName = qName.substring(index + 1);
                        if (component instanceof Nameable) {
                            ((Nameable) component).setName(beanName);
                        }
                    }
                }
            }

            eventSupport.fireEvent(BeanPhase.CREATING, component);

            boolean singleton = false;
            if (spec instanceof ComponentSpec) {
                singleton = MapSpec.getBoolean(((ComponentSpec)spec).getMetadata().getAnnotations(), "singleton", false);
            }
            if (singleton) {
                bean = newSingleton(clazz);
            }
            else {
                try {
                    bean = Reflection.newInstance(clazz);
                } catch (ReflectionException re) {
                    throw new ObjectCreationException("Create object exception:" + clazz.getName(), re);

                } catch (Throwable t) {
                    if (log.isErrorEnabled()) {
                        log.error("Create new instance error:", t);
                    }
                    throw new ObjectCreationException("Create object exception:" + clazz.getName(), t);
                }
            }
        }

        eventSupport.fireEvent(BeanPhase.CREATED, component);
        return bean;
    }

    public static <T> T newSingleton(Class<T> clazz) {
        if (Enum.class.isAssignableFrom(clazz)) {
            Class<? extends Enum> enumClass = (Class<? extends Enum>)clazz;
            return (T)Enum.valueOf(enumClass, "INSTANCE");
        } else {
            try {
                Method method = clazz.getDeclaredMethod("getInstance");
                if (Modifier.isStatic(method.getModifiers())) {
                    return (T)Reflection.invoke((Object)null, (Method)method);
                }
            } catch (NoSuchMethodException var2) {
            } catch (ReflectionException var3) {
            }

            return Reflection.newInstance(clazz);
        }
    }

    /**
     * Configure the bean
     *
     * @param component Component
     */
    @Override
    public void configure(Component component) {
        eventSupport.fireEvent(BeanPhase.CONFIGURING, component);

        autoConfigurator.configure(component);

        configuratorFactory.configure(component);

        eventSupport.fireEvent(BeanPhase.CONFIGURED, component);
    }

    /**
     * @param component
     */
    @Override
    public void init(Component component) {
        doInit(component, false);
    }

    protected void doInit(Component component, boolean isChild) {
        eventSupport.fireEvent(BeanPhase.INITIALIZING,component);

        Object bean = component.getBean();
        if (!isChild) {
            ensure(bean, BeanStage.INITIALIZE, false);
        }
        else {
            // The parent bean needs to control the lifecycle of the child,
            // So in some cases parent bean needs to invoke child's init method explicitly
            // But in some cases parent bean doesn't want to take care about the lifecycle of child
            // As the container, we can not use the single rule to fit both of the use cases.
            // Our strategy is the following
            // 1. If the child has PostConstruct, that means container takes care the initialization state
            // 2. If the child implements InitializableMBean which has the state, we can check whether it has been initialized or not, then takes action.
            // 3. If the child bean is a pure Initializable or other stuff, then we don't take any action
            Class<?> childClass = bean.getClass();
            if (!invokeByAnnotation(childClass, bean, PostConstruct.class)) {
                if (bean instanceof InitializableMBean) {
                    InitializableMBean mBean = (InitializableMBean)bean;
                    if (!mBean.isInitialized()) {
                        mBean.init();
                    }
                }
            }
        }

        //Initialize children
        Map<String, Component> children = component.getChildren();
        if (children != null) {
            //Keep the sequences of keys, since the map is linked hash map
            for(String key: children.keySet()) {
                Component child = children.get(key);
                doInit(child, true);
            }
        }

        eventSupport.fireEvent(BeanPhase.INITIALIZED,component);
    }

    /**
     * Start component
     *
     * @param component Component
     * @throws org.femtoframework.coin.exception.BeanLifecycleException
     */
    @Override
    public void start(Component component) {
        doStart(component, false);
    }

    protected void doStart(Component component, boolean isChild) {
        eventSupport.fireEvent(BeanPhase.STARTING,component);

        Object bean = component.getBean();
        if (!isChild) {
            ensure(bean, BeanStage.START, false);
        }
        else {
            // The parent bean needs to control the lifecycle of the child,
            // So in some cases parent bean needs to invoke child's init method explicitly
            // But in some cases parent bean doesn't want to take care about the lifecycle of child
            // As the container, we can not use the single rule to fit both of the use cases.
            // Our strategy is the following
            // 1. If the child implements LifecycleMBean which has the state, we can check whether it has been started or not, then takes action.
            // 2. If the child bean is a pure Startable or other stuff, then we don't take any action
            if (bean instanceof LifecycleMBean) {
                LifecycleMBean mBean = (LifecycleMBean) bean;
                if (mBean.getBeanPhase().ordinal() < BeanPhase.STARTING.ordinal()) {
                    mBean.start();
                }
            }
        }

        //Start children
        Map<String, Component> children = component.getChildren();
        if (children != null) {
            //Keep the sequences of keys, since the map is linked hash map
            for(String key: children.keySet()) {
                Component child = children.get(key);
                doStart(child, true);
            }
        }

        eventSupport.fireEvent(BeanPhase.STARTED,component);
    }

    /**
     * Stop component
     *
     * @param component Component
     * @throws org.femtoframework.coin.exception.BeanLifecycleException
     */
    @Override
    public void stop(Component component) {
        doStop(component, false);
    }

    protected void doStop(Component component, boolean isChild) {
        eventSupport.fireEvent(BeanPhase.STOPPING,component);

        Object bean = component.getBean();
        if (!isChild) {
            ensure(bean, BeanStage.STOP, false);
        }
        else {
            // The parent bean needs to control the lifecycle of the child,
            // So in some cases parent bean needs to invoke child's init method explicitly
            // But in some cases parent bean doesn't want to take care about the lifecycle of child
            // As the container, we can not use the single rule to fit both of the use cases.
            // Our strategy is the following
            // 1. If the child implements LifecycleMBean which has the state, we can check whether it has been started or not, then takes action.
            // 2. If the child bean is a pure Startable or other stuff, then we don't take any action
            if (bean instanceof LifecycleMBean) {
                LifecycleMBean mBean = (LifecycleMBean) bean;
                if (mBean.getBeanPhase().ordinal() < BeanPhase.STOPPING.ordinal()) {
                    mBean.stop();
                }
            }
        }

        //Stop children
        Map<String, Component> children = component.getChildren();
        if (children != null) {
            //Keep the sequences of keys, since the map is linked hash map
            for(String key: children.keySet()) {
                Component child = children.get(key);
                doStop(child, true);
            }
        }

        eventSupport.fireEvent(BeanPhase.STOPPED,component);
    }

    /**
     * Check whether the bean has such Annotation and invoke if has any
     *
     * @param clazz Class
     * @param bean Bean
     * @param annotationClass Annotation
     * @return whether the bean or its parent has any annotation
     */
    protected boolean invokeByAnnotation(Class<?> clazz, Object bean, Class<? extends Annotation> annotationClass) {
        if (clazz == Object.class) {
            return false;
        }
        Method[] methods = clazz.getDeclaredMethods();
        boolean hasAnnotation = false;
        for(Method method: methods) {
            if (Modifier.isStatic(method.getModifiers())) {
                continue;
            }
            if (method.isAnnotationPresent(annotationClass)) {
                if (method.getParameterCount() != 0) {
                    log.warn("The method " + clazz.getName() + "#" + method.getName()
                            + " has @" + annotationClass.getSimpleName() + ", but it has parameters");
                    continue;
                }
                method.setAccessible(true);
                Reflection.invoke(bean, method);
                hasAnnotation = true;
            }
        }
        //Invoke parent first and merge the result
        return invokeByAnnotation(clazz.getSuperclass(), bean, annotationClass) || hasAnnotation;
    }

    /**
     * Destroy component
     *
     * @param component Component
     * @throws org.femtoframework.coin.exception.BeanLifecycleException
     */
    @Override
    public void destroy(Component component) {
        doDestroy(component, false);
    }


    protected void ensure(Object bean, BeanStage stage, boolean forward) {
        if ((forward && BeanStage.INITIALIZE.isBeforeOrCurrent(stage)) || BeanStage.INITIALIZE == stage) {
            if (bean instanceof InitializableMBean) {
                InitializableMBean mBean = (InitializableMBean) bean;
                if (!mBean.isInitialized()) {
                    mBean.init();
                }
            } else if (bean instanceof Initializable) {
                ((Initializable) bean).init();
            } else {
                Class<?> clazz = bean.getClass();
                invokeByAnnotation(clazz, bean, PostConstruct.class);
            }
        }
        if ((forward && BeanStage.START.isBeforeOrCurrent(stage)) || BeanStage.START == stage) {
            if (bean instanceof LifecycleMBean) {
                LifecycleMBean mBean = (LifecycleMBean) bean;
                if (mBean.getBeanPhase().ordinal() < BeanPhase.STARTING.ordinal()) {
                    mBean.start();
                }
            } else if (bean instanceof Startable) {
                ((Startable) bean).start();
            }
        }
        if ((forward && BeanStage.STOP.isBeforeOrCurrent(stage)) || BeanStage.STOP == stage) {
            if (bean instanceof LifecycleMBean) {
                LifecycleMBean mBean = (LifecycleMBean) bean;
                if (mBean.getBeanPhase().ordinal() < BeanPhase.STOPPING.ordinal()) {
                    mBean.stop();
                }
            } else if (bean instanceof Stoppable) {
                ((Stoppable) bean).stop();
            }
        }
        if ((forward && BeanStage.DESTROY.isBeforeOrCurrent(stage)) || BeanStage.DESTROY == stage) {
            if (bean instanceof LifecycleMBean) {
                LifecycleMBean mBean = (LifecycleMBean) bean;
                if (mBean.getBeanPhase().ordinal() < BeanPhase.DESTROYING.ordinal()) {
                    mBean.destroy();
                }
            } else if (bean instanceof Destroyable) {
                ((Destroyable) bean).destroy();
            } else {
                Class<?> clazz = bean.getClass();
                invokeByAnnotation(clazz, bean, PreDestroy.class);
            }
        }
    }

    /**
     * Ensure the Bean in given stage
     *
     * @param bean  Bean
     * @param stage Target Stage
     */
    @Override
    public void ensure(Object bean, BeanStage stage) {
        ensure(bean, stage, true);
    }

    protected void doDestroy(Component component, boolean isChild) {
        eventSupport.fireEvent(BeanPhase.DESTROYING,component);

        Object bean = component.getBean();
        if (!isChild) {
            ensure(bean, BeanStage.DESTROY, false);
        }
        else {
            // The parent bean needs to control the lifecycle of the child,
            // So in some cases parent bean needs to invoke child's init method explicitly
            // But in some cases parent bean doesn't want to take care about the lifecycle of child
            // As the container, we can not use the single rule to fit both of the use cases.
            // Our strategy is the following
            // 1. If the child has PostConstruct, that means container takes care the initialization state
            // 2. If the child implements InitializableMBean which has the state, we can check whether it has been initialized or not, then takes action.
            // 3. If the child bean is a pure Initializable or other stuff, then we don't take any action
            Class<?> childClass = bean.getClass();
            if (!invokeByAnnotation(childClass, bean, PreDestroy.class)) {
                if (bean instanceof LifecycleMBean) {
                    LifecycleMBean mBean = (LifecycleMBean) bean;
                    if (mBean.getBeanPhase().ordinal() < BeanPhase.DESTROYING.ordinal()) {
                        mBean.destroy();
                    }
                }
            }
        }

        //Destroy children
        Map<String, Component> children = component.getChildren();
        if (children != null) {
            //Keep the sequences of keys, since the map is linked hash map
            for(String key: children.keySet()) {
                Component child = children.get(key);
                doDestroy(child, true);
            }
        }

        eventSupport.fireEvent(BeanPhase.DESTROYED,component);
    }

    private boolean initialized = false;

    /**
     * Return whether it is initialized
     *
     * @return whether it is initialized
     */
    @Override
    public boolean isInitialized() {
        return initialized;
    }

    /**
     * Initialized setter for internal
     *
     * @param initialized BeanPhase
     */
    @Override
    public void _doSetInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    /**
     * Initiliaze internally
     */
    @Override
    public void _doInit() {
        eventSupport.init();

        try {
            generator = ImplementUtil.getInstance(RemoteGenerator.class);
        }
        catch(Exception ex) {
            log.warn("No remote generator, using RemoteGenerator.NONE instead");
            generator = RemoteGenerator.NONE;
        }
    }
}
