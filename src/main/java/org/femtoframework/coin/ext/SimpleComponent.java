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

import org.femtoframework.bean.Nameable;
import org.femtoframework.coin.BeanStage;
import org.femtoframework.coin.Component;
import org.femtoframework.coin.Namespace;
import org.femtoframework.coin.NamespaceFactory;
import org.femtoframework.coin.spec.BeanSpec;
import org.femtoframework.coin.spec.CoreKind;
import org.femtoframework.coin.spec.element.PrimitiveElement;
import org.femtoframework.coin.status.BeanStatus;
import org.femtoframework.util.convert.ConverterUtil;

import java.util.HashMap;
import java.util.Map;

import static org.femtoframework.coin.spec.SpecConstants.NAME;

/**
 * Simple Component
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public class SimpleComponent implements Component, Nameable {

    private BeanSpec spec;
    private BeanStatus status = new SimpleBeanStatus();
    private BeanStage stage = BeanStage.CREATE;
    private Object bean;

    private transient NamespaceFactory namespaceFactory;
    private transient Namespace currentNamespace;

    public SimpleComponent(NamespaceFactory namespaceFactory, BeanSpec spec) {
        this.namespaceFactory = namespaceFactory;
        this.spec = spec;
        this.currentNamespace = namespaceFactory.get(spec.getNamespace());
    }

    /**
     * Bean Spec
     *
     * @return BeanSpec
     */
    @Override
    public BeanSpec getSpec() {
        return spec;
    }

    /**
     * Return current namespace
     *
     * @return Current Namespace
     */
    @Override
    public Namespace getCurrentNamespace() {
        return currentNamespace;
    }

    /**
     * Bean Status
     *
     * @return BeanStatus
     */
    @Override
    public BeanStatus getStatus() {
        return status;
    }

    /**
     * Set the target bean stage, that means the component will be executed to given stage
     *
     * @param stage
     */
    @Override
    public void setStage(BeanStage stage) {
        this.stage = stage;
    }

    /**
     * Return the right bean with expectedType
     *
     * @param expectedType ExpectedType, if the is null, that means returning the bean implementation object anyway
     * @return Bean
     */
    @Override
    public <T> T getBean(Class<T> expectedType) {
        if (expectedType == null) {
            return (T)getBean();
        }
        else {
            return ConverterUtil.convertToType(getBean(), expectedType);
        }
    }

    /**
     * Return namespace factory
     *
     * @return namespace factory
     */
    @Override
    public NamespaceFactory getNamespaceFactory() {
        return namespaceFactory;
    }

    /**
     * Return expected Stage
     *
     * @return expected Stage
     */
    @Override
    public BeanStage getStage() {
        return stage;
    }

    public Object getBean() {
        return bean;
    }

    public void setBean(Object bean) {
        this.bean = bean;
    }

    /**
     * Set name of the object
     *
     * @param name Name
     */
    @Override
    public void setName(String name) {
        if (spec != null) {
            spec.put(NAME, new PrimitiveElement<>(CoreKind.STRING, name));
        }
    }

    private Map<String, Component> children = null;

    /**
     * Add Child component, if the child bean is an anonymous Component, it will be put into the parent instead of adding it to ComponentFactory
     *
     * @param propertyName Property Name
     * @param component Child Component
     */
    public void addChild(String propertyName, Component component) {
        if (children == null) {
            children = new HashMap<>(4);
        }
        children.put(propertyName, component);
    }

    /**
     * Return Component by Formatted property name
     *
     * @param name Formatted Property Name,
     * @return
     */
    public Component getChild(String name) {
        return children.get(name);
    }
}
