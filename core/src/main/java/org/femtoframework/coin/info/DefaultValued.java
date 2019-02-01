package org.femtoframework.coin.info;

/**
 * Default Valued interface to indicate the info which has default value defined in String
 *
 * @see PropertyInfo
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public interface DefaultValued {

    /**
     * Default value defined in String
     *
     * @see org.femtoframework.coin.annotation.Property
     */
    String getDefaultValue();


    /**
     * Get default value as expected type
     *
     * @param <T> Expected Type
     * @return default value as expected type
     */
    <T> T getExpectedDefaultValue();
}
