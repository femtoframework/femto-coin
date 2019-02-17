package org.femtoframework.coin.spi;

import org.femtoframework.coin.BeanFactory;

/**
 * Set BeanFactory
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public interface BeanFactoryAware {
    /**
     * Set BeanFactory
     *
     * @param factory BeanFactory
     */
    void setBeanFactory(BeanFactory factory);
}
