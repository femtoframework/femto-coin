package org.femtoframework.coin.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Coined
 *
 * What kind of object can be coined?
 * 1. POJO which is used to hold the information only.
 * 2. DTO an object that carries data between processes.
 * 3. Business Object which can be managed by COIN
 *
 * Why not "@Bean"?  "@Bean" is used too widely in spring
 *
 * If it specified order in alphabet, the property with index = -1 will be ordered by alphabet
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Coined {

    /**
     * Ignore unknown properties when decoding?
     *
     * @return IgnoreUnknown Properties
     */
    boolean ignoreUnknownProperties() default true;

    /**
     * Property that defines what to do regarding ordering of properties
     * not explicitly included in annotation instance. If set to true,
     * they will be alphabetically ordered(default settings); if false, order is
     * undefined
     */
    boolean alphabeticOrder() default true;

    /**
     * Specify the order or properties, it is used to generate the ProtoBuf encoder or other serialization which needs to keep order
     *
     * @return Property Order
     */
    String[] propertyOrder() default {};
}
