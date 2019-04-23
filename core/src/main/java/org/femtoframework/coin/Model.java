package org.femtoframework.coin;

import org.femtoframework.coin.meta.ObjectMeta;
import org.femtoframework.coin.meta.TypeMeta;
import org.femtoframework.coin.spec.BeanSpec;
import org.femtoframework.coin.spec.ComponentSpec;

/**
 * COIN Object interface
 *
 * @param <SPEC> Spec
 * @param <STATUS> Status
 */
public interface Model<SPEC, STATUS> extends TypeMeta, ObjectMeta {

    /**
     * Absolute name in coin container, it doesn't include namespace
     *
     * 1. Level 1 components,
     * name
     * 2. Level 2 components,
     * name.childName
     *
     * @return GeneratedName
     */
    default String getGenerateName() {
        return getName();
    }


    /**
     * Return the SPEC of COIN Object
     *
     * @return Spec
     */
    SPEC getSpec();

    /**
     * Return the status of COIN Object
     *
     * @return Status
     */
    STATUS getStatus();
}
