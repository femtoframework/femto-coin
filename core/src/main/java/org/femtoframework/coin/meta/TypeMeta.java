package org.femtoframework.coin.meta;

import org.femtoframework.coin.spec.Kind;

/**
 * Type Meta
 *
 * @link https://github.com/kubernetes/apimachinery/blob/master/pkg/apis/meta/v1/meta.go
 */
public interface TypeMeta {

    /**
     * Return the Kind of the Type
     *
     * @return Kind
     */
    Kind getKind();

    /**
     * API Version
     *
     * @return API Version
     */
    String getApiVersion();
}
