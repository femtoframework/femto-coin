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

import org.femtoframework.coin.spec.BeanSpec;

/**
 * Bean factory
 * 
 * @author fengyun
 * @version 1.00 2005-1-27 14:12:22
 */
public interface BeanFactory extends Factory<Object> {
    /**
     * Return object by given name
     *
     * @param name Name
     * @param create Create object automatically
     * @return object in this factory
     */
    Object get(String name, boolean create);


    /**
     * Create Object by BeanSpec
     *
     * @param name Bean Name
     * @param spec Bean Spec
     * @param stage BeanStage
     */
    Object create(String name, BeanSpec spec, BeanStage stage);
}
