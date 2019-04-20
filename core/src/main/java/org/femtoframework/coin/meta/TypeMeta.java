package org.femtoframework.coin.meta;

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
    String getKind();

    /**
     * API Version
     *
     * @return API Version
     */
    String getApiVersion();
}
