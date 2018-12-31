/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
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
package org.femtoframework.coin.configurator;

import org.femtoframework.annotation.Resources;
import org.femtoframework.coin.*;
import org.femtoframework.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.inject.Named;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Auto Inject
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public class AutoConfigurator implements Configurator {

    private Logger log = LoggerFactory.getLogger(AutoConfigurator.class);

    /**
     * Configure the bean
     *
     * @param context BeanContext
     */
    @Override
    public void configure(BeanContext context) {
        Object obj = context.getBean();
        Class clazz = obj.getClass();
        configurate(obj, clazz, context);
    }

    public void configurate(Object obj, Class clazz, BeanContext context) {
        Method[] methods = clazz.getMethods();

        for (Method method : methods) {
            if (Modifier.isStatic(method.getModifiers()) || !Modifier.isPublic(method.getModifiers())) {
                continue;
            }
            if (method.getDeclaringClass() == Object.class) {
                continue;
            }

            if (method.getParameterTypes().length != 1) {
                continue;
            }

            Resource injection = method.getAnnotation(Resource.class);
            if (injection != null) {
                autoInject(obj, injection, context, method);
                continue;
            }

            Resources resources = method.getAnnotation(Resources.class);
            if (resources != null) {
                for (Resource resource : resources.value()) {
                    autoInject(obj, resource, context, method);
                }
                continue;
            }

//            Inject inject = method.getAnnotation(Inject.class);
//            if (inject != null) {
//                autoInject(obj, method.getAnnotation(Named.class), context, method);
//                continue;
//            }
            autoInject(obj, method.getAnnotation(Named.class), context, method);
        }
    }

    /**
     *
     * @param parent
     * @param named
     * @param context
     * @param method
     */
    private void autoInject(Object parent, Named named, BeanContext context, Method method) {
        String namespace = null;
        String[] names = splitName(named != null ? named.value() : null);
        if (names == null || StringUtil.isInvalid(names[0])) {
            namespace = context.getNamespace();
        }
        autoInject(parent, namespace, names[1], null, context, method);
    }


    private void autoInject(Object parent, String targetNamespace, String targetName, Class<?> clazz, BeanContext context,
                            Method method) {
        Object value = null;
        Namespace ns = context.getNamespaceByName(targetNamespace);

        if (targetName != null && !targetName.isEmpty()) {
            value = ns.getBeanFactory().get(targetName);
        }
        if (value == null) {
            Component component = ns.getComponentFactory().get(targetName);
            if (component == null) {
                Class<?> expectedType = method.getParameterTypes()[0];
                //If the bean does not exist, try to match by expectedType in the factory
                if (clazz == null || clazz == Object.class) {
                    if (expectedType.isInterface()) {
                        clazz = ns.getComponentFactory().getImplement(targetName, expectedType);
                        if (clazz == null) { //Ignore this
                            if (log.isInfoEnabled()) {
                                log.info("No such implementation for interface:" + expectedType);
                            }
                            return;
                        }

//                    Iterator<BeanSpec> it = ns.getBeanSpecFactory().iterator();
//                    BeanSpec found = null;
//                    while (it.hasNext()) {
//                        BeanSpec spec = it.next();
//                        Class<?> typeClass = spec.getKindClass();
//                        if (typeClass != null) {
//                            if (expectedType.isAssignableFrom(typeClass)) {
//                                if (found != null) {
//                                    log.warn("More than one object can be injected for the method=" + method.getName() +
//                                            " namespace=" + namespace + " expectedType:" + expectedType);
//                                    break;
//                                }
//                                else {
//                                    found = spec;
//                                }
//                            }
//                        }
//                    }
//
//                    if (found != null) {
//                        name = found.getName();
//                        value = ns.getBeanFactory().get(name, true);
//                    }
//                    else {
//                        ImplementedBy implementedBy = expectedType.getAnnotation(ImplementedBy.class);
//                        String qName = context.getNamespace() + ":" + context.getName();
//                        if (implementedBy != null) {
//                            String implClassName = implementedBy.value();
//                            Class<?> implClass;
//                            try {
//                                implClass = Reflection.getClass(implClassName, expectedType.getClassLoader());
//                            }
//                            catch (ClassNotFoundException e) {
//                                log.warn("No such object of the Injection:  objectName=" + qName + " function=" +
//                                        method.getName() + " name=" + name, e);
//                                return;
//                            }
//

//                        }
//                    }
                      component = ns.getComponentFactory().create(targetName, clazz, context.getTargetStage());

//TODO
//                    String newBeanName = name;
//                    String newBeanNamespace = namespace;
//                    String qName = context.getNamespace() + ":" + context.getName();
//
//                    ManagedBean mb = clazz.getAnnotation(ManagedBean.class);
//                    if (mb != null) {
//                        String[] names = splitName(mb.value());
//                        newBeanName = names[1];
//                        newBeanNamespace = names[0];
//                        if (!StringUtil.equals(newBeanNamespace, namespace) || !StringUtil.equals(newBeanName, name)) {
//                            log.warn("No such bean of the Injection:  beanName=" + qName + " function=" +
//                                    method.getName() + " name=" + name);
//                        }
//                    }
//                    else if (name == null || name.isEmpty()) {
//                        String packageName = expectedType.getPackage().getName();
//                        String parentPackage = parent.getClass().getPackage().getName();
//                        String implPackageName = clazz.getPackage().getName();
//                        if (parentPackage.equals(packageName) || parentPackage.equals(implPackageName)) {
//                            //Create bean automatically
//                            newBeanName = NamingFormat.format(expectedType.getSimpleName());
//                        }
//                        else {
//                            log.warn("No such bean of the Injection:  beanName=" + qName + " function=" +
//                                    method.getName() + " name=" + name);
//                            return;
//                        }
//                    }


//                    Namespace newBeanNs = context.getNamespaceByName(newBeanNamespace);
//                    ComponentFactory factory = newBeanNs.getComponentFactory();
//                    value = factory.create(newBeanName, clazz, context.getTargetStage()).getBean(expectedType);

                    }
                }
                else {
                    component = ns.getComponentFactory().create(targetName, clazz, context.getTargetStage());
                }
                if (component != null) {
                    value = component.getBean(expectedType);
                }
                else {
                    if (log.isWarnEnabled()) {
                        log.warn("Not able to create component:" + targetName + " class:" + clazz + " expected Type:" + expectedType);
                    }
                }
            }

            if (value == null) {
                return;
            }
        }
        invoke(value, parent, targetName, context, method);
    }


    protected static String[] splitName(String qName) {
        String[] names = new String[2];
        names[1] = qName;
        if (StringUtil.isValid(qName)) {
            int i = qName.indexOf(':');
            if (i > 0) {
                names[0] = qName.substring(0, i);
                names[1] = qName.substring(i + 1);
            }
        }
        return names;
    }

    private void autoInject(Object parent, Resource injection, BeanContext context, Method method) {
        Class<?> clazz = injection.type();

        String namespace = null;
        String[] names = splitName(injection.name());
        if (names == null || StringUtil.isInvalid(names[0])) {
            namespace = context.getNamespace();
        }
        autoInject(parent, namespace, names[1], clazz, context, method);
    }


    private void invoke(Object value, Object parent, String name, BeanContext context, Method method) {
        //如果方法是单个参数，那么直接调用，如果是两个参数，那么第一个参数必须是对象名称
        Class[] paramTypes = method.getParameterTypes();
        int argCount = paramTypes.length;
        try {
            if (argCount == 1) {
                method.invoke(parent, value);
            }
            else if (argCount == 2) {
                method.invoke(parent, name, value);
            }
        }
        catch (Exception ex) {
            String qName = context.getNamespace() + ":" + context.getName();
            log.warn("Invoke method exception of the Injection:  objectName=" + qName + " function=" +
                    method.getName() +
                    " name=" +
                    name, ex);
        }
    }

//    private Object createComponent(Class<?> clazz, String name, String namespace, BeanContext context,
//                                   Method method) {
//        try {
//            Namespace ns = context.getNamespaceByName(namespace);
//            ComponentFactory factory = ns.getComponentFactory();
//            Component comp = factory.create(name, clazz, BeanStage.CREATE);
//            Object bean = comp.getBean(null)
//            if (name != null && !name.isEmpty() && bean instanceof Nameable) {
//                ((Nameable)bean).setName(name);
//            }
//
//            //Run into INITIALIZE --> START
//            comp.setStage(BeanStage.START);
//
////            factory.deployObject(obj, ObjectStage.START);
//
////            if (StringUtil.isValid(namespace) && StringUtil.isValid(name)) { //只有名字空间和名称都存在的时候才自动绑定
////                factory.addObject(name, obj);
////            }
//            return obj;
//        }
//        catch (Exception ex) {
//            String qName = context.getNamespace() + ":" + context.getObjectName();
//            log.warn("Create object of the Injection exception:  objectName=" + qName + " function=" +
//                    method.getName() + " name=" + name + " class=" + clazz.getName(), ex);
//            return null;
//        }
//    }
}
