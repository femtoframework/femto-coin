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
 * Bean Stage
 *
 * @author fengyun
 * @version 1.00 2005-9-2 21:08:35
 */
public enum BeanStage {

    /**
     * The bean is disabled
     */
    DISABLED,

    /**
     * Construction stage
     */
    CREATE,

    /**
     * Calling setter or getter
     */
    CONFIGURE,

    /**
     * Do some simple initialization logic which simple setters are not able to do that.
     */
    INITIALIZE,

    /**
     * Start to run, for example, start the thread to make the bean on working state
     */
    START,

    /**
     * Stop running
     */
    STOP,

    /**
     * Destroy the bean
     */
    DESTROY
}
