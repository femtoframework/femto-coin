package org.femtoframework.coin.spi;

import org.femtoframework.bean.info.BeanInfoFactory;

public interface BeanInfoFactoryAware {

    /**
     * Set BeanInfoFactory
     *
     * @param factory BeanInfoFactory
     */
    void setBeanInfoFactory(BeanInfoFactory factory);
}
