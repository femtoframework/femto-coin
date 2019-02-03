package org.femtoframework.coin.spec;

import org.femtoframework.coin.CoinConstants;
import org.femtoframework.parameters.Parameters;

/**
 * Config Spec
 *
 * Config allows anonymous, coin generates name for it, if the name did not provide.
 *
 * Config doesn't have the structure information, it has the properties only
 */
public interface ConfigSpec extends ModelSpec {

    /**
     * Return namespace of current bean spec
     *
     * @return namespace
     */
    default String getNamespace() { //Default is "current" namespace
        return CoinConstants.DEFAULT_NAMESPACE;
    }

    /**
     * Return the parameter values only
     *
     * @return Parameters which only has the key-value pairs
     */
    <V> Parameters<V> getParameters();
}
