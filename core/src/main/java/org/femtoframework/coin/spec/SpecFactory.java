package org.femtoframework.coin.spec;

import org.femtoframework.coin.ResourceFactory;

/**
 * Bean Spec ResourceFactory
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public interface SpecFactory<S extends ModelSpec> extends ResourceFactory<S> {

    /**
     * Add new ModelSpec
     *
     * @param spec ModelSpec
     */
    void add(S spec);
}
