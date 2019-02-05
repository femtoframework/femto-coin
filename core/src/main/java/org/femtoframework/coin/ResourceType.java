package org.femtoframework.coin;

/**
 * Resource Type
 *
 */
public enum ResourceType {
    UNKNOWN("unknown", "unknowns"),

    NAMESPACE("namespace", "namespaces"),

    COMPONENT("component", "components"),

    SPEC("spec", "specs"),

    CONFIG("config", "configs"),

    INFO("info", "infos"),

    BEAN("bean", "beans");


    private String typeName;
    private String pluralName;

    ResourceType(String typeName, String pluralName) {
        this.typeName = typeName;
        this.pluralName = pluralName;
    }

    public String getName() {
        return typeName;
    }

    public String getPluralName() {
        return pluralName;
    }

    /**
     * Convert any name to ResourceType
     *
     * @param name smallName, name in capital, or pluralName
     * @return Reosurce Type
     */
    public static ResourceType toType(String name) {
        if (name == null || name.isEmpty()) {
            return UNKNOWN;
        }
        switch (name) {
            case "namespace":
            case "namespaces":
            case "NAMESPACE":
                return NAMESPACE;
            case "component":
            case "COMPONENT":
            case "components":
                return COMPONENT;
            case "spec":
            case "SPEC":
            case "specs":
                return SPEC;
            case "bean":
            case "BEAN":
            case "beans":
                return BEAN;
            case "config":
            case "CONFIG":
            case "configs":
                return CONFIG;
            case "info":
            case "INFO":
            case "infos":
                return INFO;
            default:
                return UNKNOWN;
        }
    }
}
