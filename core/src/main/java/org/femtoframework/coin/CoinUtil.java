package org.femtoframework.coin;

import org.femtoframework.coin.spec.KindSpecFactory;
import org.femtoframework.implement.ImplementUtil;

/**
 * Coin Util
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public class CoinUtil {

    private static CoinModule module;

    static {
        module = ImplementUtil.getInstance(CoinModule.class);
        ImplementUtil.init(module);
    }

    public static CoinModule getModule() {
        return module;
    }

    /**
     * New Module for unit test
     *
     * @return Module
     */
    public static CoinModule newModule() {
        CoinModule module = ImplementUtil.getInstance(CoinModule.class, false);
        ImplementUtil.init(module);
        return module;
    }

    /**
     * Coin Control, maintain objects by Yaml or JSON
     *
     * @return Coin Control
     */
    public static CoinController getController() {
        return getModule().getController();
    }

    /**
     * Return KindSpecFactory
     *
     * @return KindSpecFactory
     */
    public static KindSpecFactory getKindSpecFactory() {
        return getModule().getKindSpecFactory();
    }
}
