package org.femtoframework.coin.ext;

import org.femtoframework.bean.NamedBean;
import org.femtoframework.coin.NamespaceFactory;
import org.femtoframework.coin.spec.ModelSpec;
import org.femtoframework.coin.spec.SpecFactory;

/**
 * Simple Spec Factory
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public class SimpleSpecFactory<S extends ModelSpec> extends BaseFactory<S> implements SpecFactory<S> {

    public SimpleSpecFactory(NamespaceFactory namespaceFactory, String namespace) {
        super(namespaceFactory, namespace);
    }

    /**
     * Add new ModelSpec
     *
     * @param spec ModelSpec
     */
    @Override
    public void add(S spec) {
        add((NamedBean)spec);
    }
}
