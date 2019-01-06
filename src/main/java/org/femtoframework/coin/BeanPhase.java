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
    DISABLED(false),

    /**
     * Bean is enabled, means it is ready for creating.
     */
    ENABLED(true),

    /**
     * Bean is creating
     */
    CREATING(true),

    /**
     * Bean is created
     */
    CREATED(false),

    /**
     * Bean is configuring
     */
    CONFIGURING(true),

    /**
     * Bean is configured
     */
    CONFIGURED(false),

    /**
     * Bean is initializing
     */
    INITIALIZING(true),

    /**
     * Bean is initialized
     */
    INITIALIZED(false),

    /**
     * Bean is starting
     */
    STARTING(true),

    /**
     * Bean is started
     */
    STARTED(false),

    /**
     * Bean is stopping
     */
    STOPPING(true),

    /**
     * Bean is stopped
     */
    STOPPED(false),

    /**
     * Bean is destroying
     */
    DESTROYING(true),

    /**
     * Bean is destroyed
     */
    DESTROYED(false);


    private boolean running;

    BeanPhase(boolean running) {
        this.running = running;
    }

    public boolean isRunning() {
        return running;
    }

    /**
     * Bean Stage
     *
     * @param stage Bean Stage
     * @return
     */
    public static BeanPhase expectedPhase(BeanStage stage) {
        switch (stage) {
            case DISABLED:
                return BeanPhase.DISABLED;
            case CREATE:
                return BeanPhase.CREATED;
            case CONFIGURE:
                return BeanPhase.CONFIGURED;
            case INITIALIZE:
                return BeanPhase.INITIALIZED;
            case START:
                return BeanPhase.STARTED;
            case STOP:
                return BeanPhase.STOPPED;
            case DESTROY:
                return BeanPhase.DESTROYED;
        }
        return BeanPhase.DISABLED;
    }
}
