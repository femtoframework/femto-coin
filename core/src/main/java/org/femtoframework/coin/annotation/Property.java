package org.femtoframework.coin.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Property {
    /**
     * Use default property name
     */
    String USE_DEFAULT_NAME = Constants.USE_DEFAULT_NAME;

    /**
     * Marker value used to indicate that no index has been specified.
     * Used as the default value as annotations do not allow "missing"
     * values.
     */
    int INDEX_UNKNOWN = Constants.INDEX_UNKNOWN;

    /**
     * The property name, if it is "" that means use the default property name
     *
     * @return Property Name
     */
    String value() default USE_DEFAULT_NAME;

    /**
     * Is the property required when decoding from JSON or YAML?
     *
     * @return used in decoding only, if required is true, the field must present in the JSON, regardless null or not
     */
    boolean required() default false;

    /**
     * Property that indicates numerical index of this property (relative
     * to other properties specified for the Object). This index
     * is typically used by binary formats, but may also be useful
     * for schema languages, ProtoBuff or other tools.
     */
    int index() default INDEX_UNKNOWN;

    /**
     * The default property value in String
     *
     * @return default value
     */
    String defaultValue() default "";

    /**
     * Is the property readable
     *
     * @return property readable
     */
    boolean readable() default true;

    /**
     * Is the property writable
     *
     * @return property writable
     */
    boolean writable() default true;
}
