package org.femtoframework.coin.spec;

/**
 * Kind
 *
 * bean, it must have a "class" field which indicates the kind of this bean
 * map, if there is no "class" specified in a map structure, that means it is a map
 * list, multiple components
 * string, text value
 * int,  Integer
 * long, LONG
 * double, Any Float or Double
 * boolean, True of False
 * bytes BINARY
 *
 * in yaml
 *
 * bool, Boolean
 * float, Tags, Floating Point
 * int, Tags, Integer
 * map, Tags, Generic Mapping
 * null, Tags, Empty Nodes, Null
 * seq, Tags, Generic Sequence
 * str, Tags, Generic String
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public enum CoreKind implements Kind {
    /**
     * Null
     */
    NULL(true),

    /**
     * Text value, "str" in yaml
     */
    STRING(true),

    /**
     * Integer,  "int" in yaml
     */
    INT(true),

    /**
     * Long
     */
    LONG(true),

    /**
     * Float value, "float" in yaml
     */
    DOUBLE(true),

    /**
     * Boolean, "bool" in yaml
     */
    BOOLEAN(true),

    /**
     * Binary, byte[]
     */
    BYTES(true),

    /**
     * Timestamp, "java.util.Date"
     */
    TIMESTAMP(true),

    /**
     * Basic object in yaml
     */
    MAP(false),

    /**
     * multiple components, "seq" in yaml
     */
    LIST(false),

    /**
     * multiple components, "set" in yaml
     */
    SET(false),

    /**
     * An extension of MAP
     *
     * It must have a "class" which indicates the kind of this bean
     */
    BEAN(false),

    /**
     * VAR
     *
     * @param primitive
     */
    VAR(false),

    /**
     * NameSpace, K8S style Object
     */
    NAMESPACE(false),

    /**
     * Component, K8S style Object
     */
    COMPONENT(false),

    /**
     * Config, K8S style Object
     */
    CONFIG(false),

    /**
     * Component, K8S style Object
     */
    REMOTE_COMPONENT(false),

    /**
     * Remote, Reference to bean in remote JVM
     */
    REMOTE(false),

    /**
     * Cron
     */
    CRON(false);

    private boolean primitive;

    CoreKind(boolean primitive) {
        this.primitive = primitive;
    }

    public boolean isPrimitive() {
        return primitive;
    }
}
