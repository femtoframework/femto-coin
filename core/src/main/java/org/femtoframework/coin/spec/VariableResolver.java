package org.femtoframework.coin.spec;

import org.femtoframework.coin.Component;

/**
 * Variable Resolver
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public interface VariableResolver {

    /**
     * Resolve variable by spec
     *
     * @param <T> Convert it to type
     * @param var VariableSpec
     * @param expectedType
     * @param component
     * @return
     */
    <T> T resolve(VariableSpec var, Class<T> expectedType, Component component);
}
