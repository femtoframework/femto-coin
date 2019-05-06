package org.femtoframework.coin.spec;

import org.femtoframework.util.DataUtil;

import java.util.List;

import static org.femtoframework.coin.CoinConstants.*;

/**
 * Component Spec is
 */
public interface ComponentSpec extends BeanSpec, ModelSpec<BeanSpec> {

    /**
     * Return name
     *
     * @return name
     */
    default String getName() {
        return ModelSpec.super.getGenerateName();
    }

    /**
     * Return generated name
     *
     * @return generated name
     */
    default String getGenerateName() {
        return ModelSpec.super.getGenerateName();
    }

    /**
     * Return default namespace
     *
     * @return Namespace
     */
    default String getNamespace() {
        return ModelSpec.super.getNamespace();
    }

    /**
     * Whether the bean is enabled or not.
     *
     * @return Is enabled or not
     */
    default boolean isEnabled() {
        MapSpec<Element> labels = getMetadata().getLabels();
        if (labels != null) {
            return DataUtil.getBoolean(labels.getValue(LABEL_ENABLED, true), true);
        }
        return true;
    }

    /**
     * Check whether the component is default for specific Interface
     *
     * @return Class of the interface
     */
    default List<String> getDefaultFor() {
        MapSpec<Element> annotations = getMetadata().getAnnotations();
        if (annotations != null) {
            return DataUtil.getStringList(annotations.getValue(ANNOTATIONS_DEFAULT_FOR, null));
        }
        return null;
    }
}
