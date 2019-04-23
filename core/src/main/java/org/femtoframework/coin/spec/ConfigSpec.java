package org.femtoframework.coin.spec;

import org.femtoframework.bean.Nameable;
import org.femtoframework.parameters.Parameters;

/**
 * Config Spec
 *
 * Config allows anonymous, coin generates name for it, if the name did not provide.
 *
 * Config doesn't have the structure information, it has the properties only
 */
public interface ConfigSpec extends ModelSpec, Nameable {

    /**
     * Return the parameter values only
     *
     * @return Parameters which only has the key-value pairs
     */
    <V> Parameters<V> getParameters();
}
