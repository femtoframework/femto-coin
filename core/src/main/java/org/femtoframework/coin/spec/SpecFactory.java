package org.femtoframework.coin.spec;

import org.femtoframework.coin.Factory;

/**
 * Bean Spec Factory
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public interface SpecFactory<S extends ModelSpec> extends Factory<S> {

    /**
     * Add new ModelSpec
     *
     * @param spec ModelSpec
     */
    void add(S spec);
}
