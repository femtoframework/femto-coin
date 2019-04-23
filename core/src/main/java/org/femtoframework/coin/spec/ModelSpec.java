package org.femtoframework.coin.spec;

import org.femtoframework.bean.NamedBean;
import org.femtoframework.coin.meta.TypeMeta;

import static org.femtoframework.coin.CoinConstants.*;

/**
 * Spec which has Version and Model structure (K8S style Object)
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public interface ModelSpec<SPEC extends MapSpec<Element>> extends MapSpec<Element>, NamedBean, TypeMeta {
    /**
     * Version
     */
    default String getApiVersion() {
        return getString(API_VERSION, VERSION_COIN_V1);
    }

    default String getName() {
        return getMetadata().getName();
    }

    /**
     * Return generated name
     *
     * @return generated name
     */
    default String getGenerateName() {
        return getMetadata().getGenerateName();
    }

    /**
     * Namespace of this component
     *
     * @return Namespace
     */
    default String getNamespace() {
        return getMetadata().getNamespace();
    }

    /**
     * Return Metadata
     *
     * @return Metadata
     */
    MetadataSpec<Element> getMetadata();

    /**
     * Return Spec
     *
     * @return Spec
     */
    SPEC getSpec();
}
