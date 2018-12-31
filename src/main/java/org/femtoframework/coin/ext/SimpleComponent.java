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
import org.femtoframework.coin.spec.BeanSpec;
import org.femtoframework.coin.spec.element.BeanElement;
import org.femtoframework.coin.status.BeanStatus;

/**
 * Simple Component
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public class SimpleComponent implements Component {

    private BeanSpec spec;
    private BeanStatus status = new SimpleBeanStatus();
    private BeanStage stage = BeanStage.CREATE;


    public SimpleComponent(String name, Class<?> implClass) {
        spec = new BeanElement(name, implClass);
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
        //TODO
        return null;
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
}
