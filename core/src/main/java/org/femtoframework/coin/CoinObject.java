package org.femtoframework.coin;

import org.femtoframework.coin.meta.ObjectMeta;
import org.femtoframework.coin.meta.TypeMeta;

/**
 * COIN Object interface
 *
 * @param <SPEC> Spec
 * @param <STATUS> Status
 */
public interface CoinObject<SPEC, STATUS> extends TypeMeta, ObjectMeta {

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
