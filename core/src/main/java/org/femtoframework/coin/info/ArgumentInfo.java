package org.femtoframework.coin.info;

/**
 * Argument Info
 *
 * It reuses the existing Annotation @java.inject.Named
 */
public interface ArgumentInfo extends FeatureInfo {

    /**
     * Type of this argument
     *
     * @return type of this argument
     */
    String getType();
}
