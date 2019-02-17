package org.femtoframework.coin.spi;

import org.femtoframework.coin.info.BeanInfoFactory;

public interface BeanInfoFactoryAware {

    /**
     * Set BeanInfoFactory
     *
     * @param factory BeanInfoFactory
     */
    void setBeanInfoFactory(BeanInfoFactory factory);
}
