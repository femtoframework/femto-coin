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
 * Bean phase of lifecycle
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public enum BeanPhase {

    /**
     * Bean is disabled, means it is not created or configured
     */
    DISABLED,

    /**
     * Bean is enabled, means it is ready for creating.
     */
    ENABLED,

    /**
     * Bean is creating
     */
    CREATING,

    /**
     * Bean is created
     */
    CREATED,

    /**
     * Bean is configuring
     */
    CONFIGURING,

    /**
     * Bean is configured
     */
    CONFIGURED,

    /**
     * Bean is initializing
     */
    INITIALIZING,

    /**
     * Bean is initialized
     */
    INITIALIZED,

    /**
     * Bean is starting
     */
    STARTING,

    /**
     * Bean is started
     */
    STARTED,

    /**
     * Bean is stopping
     */
    STOPPING,

    /**
     * Bean is stopped
     */
    STOPPED,

    /**
     * Bean is destroying
     */
    DESTROYING,

    /**
     * Bean is destroyed
     */
    DESTROYED;
}
