package org.femtoframework.coin.spec;

import org.femtoframework.coin.Factory;

/**
 * Kind Spec Factory
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public interface KindSpecFactory extends Factory<KindSpec> {

    /**
     * Return the core kind spec
     *
     * @return Core Kind Spec
     */
    KindSpec getCoreKindSpec();
}
