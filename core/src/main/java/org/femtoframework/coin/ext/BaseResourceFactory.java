package org.femtoframework.coin.ext;

import org.femtoframework.bean.BeanStage;
import org.femtoframework.coin.*;
import org.femtoframework.bean.annotation.Coined;
import org.femtoframework.bean.annotation.Ignore;
import org.femtoframework.pattern.ext.BaseFactory;

import static org.femtoframework.coin.CoinConstants.NAMESPACE_COIN;

/**
 * Base ResourceFactory
 *
 * @author Sheldon Shao
 * @version 1.0
 */
@Coined
public class BaseResourceFactory<B> extends BaseFactory<B> implements ResourceFactory<B> {

    private String namespace;

    @Ignore
    private NamespaceFactory namespaceFactory;

    protected BaseResourceFactory() {
    }

    protected BaseResourceFactory(NamespaceFactory namespaceFactory, String namespace) {
        setNamespace(namespace);
        setNamespaceFactory(namespaceFactory);
    }

    protected void setNamespaceFactory(NamespaceFactory namespaceFactory) {
        this.namespaceFactory = namespaceFactory;
    }

    /**
     * Namespace of this factory
     *
     * @return Namespace
     */
    @Override
    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    @Ignore
    public NamespaceFactory getNamespaceFactory() {
        return namespaceFactory;
    }

    /**
     * Apply stage to all its children
     *
     * @param namePrefix Prefix to put them in namespace, for example, configurator "auto" will be use name "configurator_auto" in namespace "coin"
     * @param stage Target Stage
     */
    protected void applyStageToChildren(String namePrefix, BeanStage stage) {
        NamespaceFactory namespaceFactory = getNamespaceFactory();
        Namespace namespace = namespaceFactory.get(NAMESPACE_COIN);
        ComponentFactory componentFactory = namespace.getComponentFactory();
        for(String name: getNames()) {
            B bean = get(name);
            componentFactory.create(namePrefix + name , bean, stage);
        }
    }

}
