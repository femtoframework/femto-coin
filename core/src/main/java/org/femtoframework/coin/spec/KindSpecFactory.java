package org.femtoframework.coin.spec;

import org.femtoframework.coin.ResourceFactory;

/**
 * Kind Spec ResourceFactory
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public interface KindSpecFactory extends ResourceFactory<KindSpec> {

    /**
     * Return the core kind spec
     *
     * @return Core Kind Spec
     */
    KindSpec getCoreKindSpec();
}
