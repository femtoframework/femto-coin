package org.femtoframework.coin.configurator;

import org.femtoframework.annotation.Resources;
import org.femtoframework.coin.*;
import org.femtoframework.bean.annotation.Ignore;
import org.femtoframework.bean.annotation.Property;
import org.femtoframework.coin.spec.*;
import org.femtoframework.coin.spec.element.BeanElement;
import org.femtoframework.coin.util.CoinNameUtil;
import org.femtoframework.text.NamingConvention;
import org.femtoframework.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.inject.Named;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

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
     * @param component Component
     */
    public void configure(Component component) {
        Object obj = component.getBean();
        Class clazz = obj.getClass();
        configure(obj, clazz, component);
    }

    public void configure(Object obj, Class clazz, Component component) {
        Method[] methods = clazz.getMethods();

        Map<String, Injection> injections = new LinkedHashMap<>();
        for (Method method : methods) {
            if (Modifier.isStatic(method.getModifiers()) || !Modifier.isPublic(method.getModifiers())) {
                continue;
            }
            if (method.getDeclaringClass() == Object.class) {
                continue;
            }

            if (method.isAnnotationPresent(Ignore.class)) { //Ignore
                continue;
            }

            if (method.getParameterTypes().length != 1) {
                continue;
            }

            Resource injection = method.getAnnotation(Resource.class);
            if (injection != null) {
                findInjection(obj, injection, component, method, injections);
                continue;
            }

            Resources resources = method.getAnnotation(Resources.class);
            if (resources != null) {
                for (Resource resource : resources.value()) {
                    findInjection(obj, resource, component, method, injections);
                }
                continue;
            }

            Inject inject = method.getAnnotation(Inject.class);
            String methodName = method.getName();
            //Only the method declared on an interface will be injected if there is no any annotation associated
            if (inject != null || methodName.startsWith("set")) { // || methodName.startsWith("add")
                findInjection(obj, method.getAnnotation(Named.class), component, method, injections);
            }
        }
        
        //Inject by element's sequence
        BeanSpec spec = component.getSpec();
        for(String key: spec.keySet()) {
            Injection injection = injections.remove(key);
            if (injection != null) {
                injection.inject(obj, component);
            }
        }

        //Inject rest
        for(String key: injections.keySet()) {
            Injection injection = injections.get(key);
            injection.inject(obj, component);
        }
    }


    private class Injection {
        Element element;
        String targetName;
        Method method;
        Class<?> expectedType;
        Component childComponent;

        public Injection(Element element, String name, Method method, Class<?> expectedType, Component childComponent) {
            this.element = element;
            this.targetName = name;
            this.method = method;
            this.expectedType = expectedType;
            this.childComponent = childComponent;
        }

        public void inject(Object parent, Component parentComponent) {
            Object value = childComponent != null ? childComponent.getBean(expectedType) :
                    element.getValue(expectedType, parentComponent);
            AutoConfigurator.this.invoke(value, parent, targetName, parentComponent, method);
        }
    }

    /**
     *
     * @param parent
     * @param named
     * @param component
     * @param method
     */
    private void findInjection(Object parent, Named named, Component component, Method method, Map<String, Injection> injections) {
        String namespace = null;
        String[] names = CoinNameUtil.splitName(named != null ? named.value() : null);
        if (names == null || StringUtil.isInvalid(names[0])) {
            namespace = component.getNamespace();
        }
        findInjection(namespace, names[1], null, component, method, injections);
    }


    private void findInjection(String targetNamespace, String targetName, Class<?> clazz, Component component,
                               Method method, Map<String, Injection> injections) {
        Object value = null;
        Namespace ns = component.getNamespaceByName(targetNamespace);


        if (StringUtil.isValid(targetName)) {
            value = ns.getBeanFactory().get(targetName);
        }

        String propertyName = null;
        if (value == null) {
            Class<?> expectedType = method.getParameterTypes()[0];
            BeanSpec spec = component.getSpec();
            if (spec instanceof ComponentSpec) {
                spec = ((ComponentSpec)spec).getSpec();
            }

            Property property = method.getAnnotation(Property.class);
            if (property != null) {
                propertyName = property.value();
            }
            else {
                propertyName = NamingConvention.parse(method.getName().substring(3), false); //PropertyName
            }
            Element element = spec.get(propertyName);
            if (element == null && property == null) {
                String formatted = NamingConvention.format(propertyName);
                element = spec.get(formatted);
            }

            //If the bean does not exist, try to match by expectedType in the factory
            boolean beanInjection = ((element != null && element.getKind() == CoreKind.BEAN)
                     || (element != null && expectedType.isInterface()));

            Component childComponent = null;
            Injection injection = null;
            if (beanInjection) {
                BeanElement childSpec = null;
                if (element instanceof BeanElement) {
                    childSpec = (BeanElement)element;
                }
                else if (element instanceof SetSpec) {
                    if (Set.class.isAssignableFrom(expectedType)) {
                        injection = new Injection(element, targetName, method, expectedType, null);
                    }
                }
                else if (element instanceof MapSpec) {
                    if (Map.class.isAssignableFrom(expectedType)) {
                        injection = new Injection(element, targetName, method, expectedType, null);
                    }
                    else if (Set.class.isAssignableFrom(expectedType)) {
                        injection = new Injection(element, targetName, method, expectedType, null);
                    }
                    else {
                        childSpec = new BeanElement((MapSpec) element);
                        childSpec.setNamespace(targetNamespace);
                        if (targetName != null) {
                            childSpec.setName(targetName);
                        }
                    }
                }
                else if (element instanceof ListSpec) {
                    if (List.class.isAssignableFrom(expectedType)) {
                        injection = new Injection(element, targetName, method, expectedType, null);
                    }
                }
                else if (element instanceof VariableSpec) {
                    injection = new Injection(element, targetName, method, expectedType, null);
                }

                Class<?> expectedClass = childSpec != null ? childSpec.getTypeClass() : null;
                if (clazz != null && clazz != Object.class) {
                    expectedClass = clazz;
                }
                else if ((childSpec == null || expectedClass == null) && expectedType.isInterface()) {
                    clazz = ns.getComponentFactory().getImplement(targetName, expectedType);
                    if (clazz != null) { //Ignore this
                        expectedClass = clazz;
                    }
                }

                if (childSpec != null && expectedClass != null) {
                    childSpec.setTypeClass(expectedClass);
                }

                if (StringUtil.isValid(targetName)) {
                    childComponent = ns.getComponentFactory().get(targetName);
                }

                if (childComponent == null && childSpec != null) {
                    childComponent = ns.getComponentFactory().create(targetName, childSpec, component.getStage());
                    if (targetName == null) {
                        targetName = propertyName;
                        childSpec.setName(propertyName);
                        component.addChild(propertyName, childComponent);
                    }
                }
            }
            else if (element != null) { //Primitive value
                injection = new Injection(element, targetName, method, expectedType, null);
            }

            if (childComponent != null) {
                injection = new Injection(element, targetName, method, expectedType, childComponent);
            }
            else {
                if (log.isTraceEnabled()) {
                    log.trace("Not able to create component:" + targetName + " class:" + clazz + " expected Type:" + expectedType);
                }
            }

            if (injection != null) {
                injections.put(targetName != null? targetName : propertyName, injection);
            }
        }
    }



    private void findInjection(Object parent, Resource injection, Component component, Method method, Map<String, Injection> injections) {
        Class<?> clazz = injection.type();

        String namespace = null;
        String[] names = CoinNameUtil.splitName(injection.name());
        if (names == null || StringUtil.isInvalid(names[0])) {
            namespace = component.getNamespace();
        }
        findInjection(namespace, names[1], clazz, component, method, injections);
    }


    private void invoke(Object value, Object parent, String name, Component component, Method method) {
        try {
            method.invoke(parent, value);
        }
        catch (Exception ex) {
            String qName = component.getNamespace() + ":" + component.getName();
            log.warn("Invoke method exception of the Injection:  objectName=" + qName + " function=" +
                    method.getName() +
                    " name=" +
                    name, ex);
        }
    }
}
