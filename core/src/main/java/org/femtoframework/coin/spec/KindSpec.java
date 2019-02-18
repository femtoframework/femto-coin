package org.femtoframework.coin.spec;


import org.femtoframework.bean.NamedBean;

import java.util.Map;

/**
 * Kind Spec
 *
 * Kind can be used for extension, users can define their own spec structure, and plug them into COIN
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public interface KindSpec extends NamedBean {

    /**
     * Name of the object
     *
     * @return Name of the object
     */
    default String getName() {
        return getVersion();
    }

    /**
     * The version this ResourceFactory supports
     *
     * @return Version
     */
    String getVersion();

    /**
     * The Kind Class it uses
     *
     * @return The kind Class should be an Enum and Kind implementation
     */
    Class<? extends Kind> getKindClass();

    /**
     * Convert Kind to right spec
     *
     * @param map Map like data
     * @return right spec object
     */
    <S extends MapSpec> S toSpec(Map map);
}
