/**
 * Licensed to the FemtoFramework under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.femtoframework.coin.ext;

import org.femtoframework.bean.*;
import org.femtoframework.coin.*;
import org.femtoframework.coin.configurator.AutoConfigurator;
import org.femtoframework.coin.event.BeanEventListeners;
import org.femtoframework.coin.event.BeanEventSupport;
import org.femtoframework.coin.spec.BeanSpec;
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


    public SimpleLifecycleStrategy(ConfiguratorFactory configuratorFactory) {
        this.configuratorFactory = configuratorFactory;
        this.eventSupport = new BeanEventSupport();
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

        String className = spec.getType();
        if (className == null) {
            throw new ObjectCreationException("No _type found in bean, " + component.getNamespace() + ":" + beanName);
        }
        Class<?> clazz = null;
        try {
            clazz = Reflection.getClass(className);
        }
        catch (ClassNotFoundException cnfe) {
            throw new NoSuchClassException("Load class " + className + " error:", cnfe);
        }
        catch (NoClassDefFoundError ndfe) {
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
                        ((Nameable)component).setName(beanName);
                    }
                }
            }
        }

        eventSupport.fireEvent(BeanPhase.CREATING, component);

        Object bean;
        if (spec.isSingleton()) {
            bean = Reflection.newSingleton(clazz);
        }
        else {
            try {
                bean = Reflection.newInstance(clazz);
            }
            catch (ReflectionException re) {
                bean = Reflection.newSingleton(clazz);
            }
            catch (Throwable t) {
                if (log.isErrorEnabled()) {
                    log.error("Create new instance error:", t);
                }
                throw new ObjectCreationException("Create object exception:" + clazz.getName(), t);
            }
        }

        eventSupport.fireEvent(BeanPhase.CREATED, component);
        return bean;
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
    public void initialize(Component component) {
        eventSupport.fireEvent(BeanPhase.INITIALIZING,component);

        Object bean = component.getBean();
        if (bean instanceof Initializable) {
            ((Initializable)bean).initialize();
        }
        else {
            Class<?> clazz = bean.getClass();
            invokeByAnnotation(clazz, bean, PostConstruct.class);
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
        eventSupport.fireEvent(BeanPhase.STARTING,component);

        Object obj = component.getBean();
        if (obj instanceof Startable) {
            ((Startable)obj).start();
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
        eventSupport.fireEvent(BeanPhase.STOPPING,component);

        Object obj = component.getBean();
        if (obj instanceof Stoppable) {
            ((Stoppable)obj).stop();
        }
        eventSupport.fireEvent(BeanPhase.STOPPED, component);
    }

    protected void invokeByAnnotation(Class<?> clazz, Object bean, Class<? extends Annotation> annotationClass) {
        if (clazz == Object.class) {
            return;
        }
        Method[] methods = clazz.getDeclaredMethods();
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
            }
        }
        invokeByAnnotation(clazz.getSuperclass(), bean, annotationClass);
    }

    /**
     * Destroy component
     *
     * @param component Component
     * @throws org.femtoframework.coin.exception.BeanLifecycleException
     */
    @Override
    public void destroy(Component component) {
        eventSupport.fireEvent(BeanPhase.DESTROYING, component);

        Object bean = component.getBean();
        if (bean instanceof Destroyable) {
            ((Destroyable)bean).destroy();
        }
        else {
            Class clazz = bean.getClass();
            invokeByAnnotation(clazz, bean, PreDestroy.class);
        }
        eventSupport.fireEvent(BeanPhase.DESTROYED, component);
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
    public void _doInitialize() {
        eventSupport.initialize();
    }
}
