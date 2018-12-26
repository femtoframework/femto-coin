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
package org.femtoframework.coin;

/**
 * Bean Lifecycle Strategy
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public interface LifecycleStrategy extends Configurator {

    /**
     * Create bean
     *
     * @param context BeanContext
     * @return the created bean
     */
    Object create(BeanContext context);

    /**
     *
     * @param context
     */
    void initialize(BeanContext context);

    /**
     * 根据将对象进行启动
     *
     * @param context 对象部署上下文
     * @throws org.femtoframework.coin.exception.BeanLifecycleException
     */
    void start(BeanContext context);


    /**
     * 根据将对象进行启动
     *
     * @param context 对象部署上下文
     * @throws org.femtoframework.coin.exception.BeanLifecycleException
     */
    default void stop(BeanContext context) {

    }


    default void destroy(BeanContext context) {

    }


}
