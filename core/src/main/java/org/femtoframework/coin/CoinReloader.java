package org.femtoframework.coin;

import java.io.IOException;

/**
 * Reload coin spec with following strategies
 * 1. DEV  All resources could be reloaded, if the resource changed, it could support hot redeploy.
 * 2. QA or PROD, Only URI with "file" scheme can refresh if the resource is modified.
 */
public interface CoinReloader {

    /**
     * Reload specs
     */
    default void reload()       throws IOException {
        reload(false);
    }

    /**
     * Reload specs
     *
     * @param force Reload anyway if it is true, otherwise follow the strategies
     */
    void reload(boolean force) throws IOException;
}
