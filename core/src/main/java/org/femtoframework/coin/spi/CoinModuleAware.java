package org.femtoframework.coin.spi;

import org.femtoframework.coin.CoinModule;

/**
 * CoinModule Aware
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public interface CoinModuleAware {

    void setCoinModule(CoinModule module);
}
