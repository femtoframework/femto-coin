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
package org.femtoframework.coin.jmx;

import org.femtoframework.bean.BeanPhase;
import org.femtoframework.coin.Component;
import org.femtoframework.coin.event.BeanEvent;
import org.femtoframework.coin.event.BeanEventListener;
import org.femtoframework.coin.info.BeanInfo;
import org.femtoframework.coin.info.BeanInfoFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.*;
import java.lang.management.ManagementFactory;
import javax.management.MXBean;

/**
 * Jmx Adapter
 *
 * @author Sheldon Shao
 * @version 1.0
 */
@MXBean
public class JmxAdapter implements BeanEventListener {

    private boolean enabled = true;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    private MBeanServer mBeanServer;

    /**
     * Logger
     */
    private Logger log = LoggerFactory.getLogger(JmxAdapter.class);

    /**
     * Handle bean event
     *
     * @param event Bean Event
     */
    @Override
    public void handleEvent(BeanEvent event) {
        if (enabled) {
            if (event.getPhase().isAfterOrCurrent(BeanPhase.INITIALIZED)) {
                if (mBeanServer == null) {
                    mBeanServer = ManagementFactory.getPlatformMBeanServer();
                }

                Class<?> typeClass = event.getComponent().getSpec().getTypeClass();

                Component component = event.getComponent();
                BeanInfoFactory beanInfoFactory = component.getModule().getBeanInfoFactory();
                BeanInfo beanInfo = beanInfoFactory.getBeanInfo(typeClass);

                String aboluteName = component.getAbsoluteName();
                String namespace = component.getNamespace();
                String objectName = namespace + ":name=" + aboluteName;
                try {
                    mBeanServer.registerMBean(event.getComponent().getBean(), new ObjectName(objectName));
                } catch (InstanceAlreadyExistsException e) {
                    log.warn("Instance already exists, " +  component.getQualifiedName(), e);
                } catch (MBeanRegistrationException e) {
                    log.warn("MBean registering exception, " + component.getQualifiedName(), e);
                } catch (NotCompliantMBeanException e) {
                    log.warn("NotCompliantMBeanException, " + component.getQualifiedName(), e);
                } catch (MalformedObjectNameException e) {
                    log.warn("Malformed ObjectName Exception, " + objectName, e);
                }
            }
        }
    }
}
