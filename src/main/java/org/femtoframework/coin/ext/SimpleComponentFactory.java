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
package org.femtoframework.coin.ext;

import org.femtoframework.coin.BeanStage;
import org.femtoframework.coin.Component;
import org.femtoframework.coin.ComponentFactory;
import org.femtoframework.coin.exception.BeanNotExpectedException;
import org.femtoframework.coin.spec.BeanSpec;
import org.femtoframework.coin.spec.BeanSpecFactory;
import org.femtoframework.coin.util.CoinNameUtil;
import org.femtoframework.implement.ImplementUtil;
import org.femtoframework.lang.reflect.NoSuchClassException;
import org.femtoframework.lang.reflect.Reflection;
import org.femtoframework.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.ManagedBean;
import java.util.Iterator;
import java.util.Properties;

/**
 * Simple Component Factory
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public class SimpleComponentFactory extends BaseFactory<Component> implements ComponentFactory {

    private BeanSpecFactory specFactory;

    private Logger log = LoggerFactory.getLogger(SimpleComponentFactory.class);


    public SimpleComponentFactory(BeanSpecFactory specFactory) {
        this.specFactory = specFactory;
        setNamespace(specFactory.getNamespace());
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
            else if (StringUtil.isValid(name)){
                if (!StringUtil.equals(newBeanName, name)) {
                    throw new BeanNotExpectedException(name, newBeanName);
                }
            }
        }
        else if (StringUtil.isInvalid(name)) {
            throw new BeanNotExpectedException(name, implClass.getSimpleName());
        }

        SimpleComponent component = new SimpleComponent(newBeanName, implClass);
        component.setStage(targetStage);
        //TODO go to target stage?
        add(component);
        return component;
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
            Properties properties = ImplementUtil.getImplementProperties(interfaceClass);
            String className = properties.getProperty(name);
            if (className != null) {
                try {
                    return Reflection.getClass(className, interfaceClass.getClassLoader());
                }
                catch (ClassNotFoundException e) {
                    if (log.isWarnEnabled()) {
                        log.warn("The implementation " + className + " of interface " +
                                interfaceClass.getName() + " is not found. " + name, e);
                    }
                    throw new NoSuchClassException("The implementation " + className + " of interface " +
                            interfaceClass.getName() + " is not found. " + name, e);
                }
            }
            //TODO create BeanSpec automatically to make it faster for next time?
        }

        Iterator<BeanSpec> it = specFactory.iterator();
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

    public BeanSpecFactory getSpecFactory() {
        return specFactory;
    }
}
