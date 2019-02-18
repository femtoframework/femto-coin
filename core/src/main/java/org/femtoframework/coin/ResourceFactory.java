package org.femtoframework.coin;

import org.femtoframework.pattern.Factory;

/**
 * ResourceFactory interface
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public interface ResourceFactory<B> extends Factory<B> {

    /**
     * Namespace of this factory
     *
     * @return Namespace
     */
    String getNamespace();
}
