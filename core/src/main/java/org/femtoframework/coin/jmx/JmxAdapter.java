package org.femtoframework.coin.jmx;

import org.femtoframework.bean.BeanPhase;
import org.femtoframework.coin.Component;
import org.femtoframework.coin.event.BeanEvent;
import org.femtoframework.coin.event.BeanEventListener;
import org.femtoframework.bean.info.BeanInfo;
import org.femtoframework.bean.info.BeanInfoFactory;
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

    private boolean enabled = false;

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

                //TODO convert beanInfo to MBeanInfo and generate MBean
                String absoluteName = component.getGenerateName();
                String namespace = component.getNamespace();
                String objectName = namespace + ":name=" + absoluteName;
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
