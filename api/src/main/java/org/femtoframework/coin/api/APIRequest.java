package org.femtoframework.coin.api;

import org.femtoframework.parameters.Parameters;
import org.femtoframework.parameters.ParametersMap;

import java.util.Map;

/**
 * API Resource Path
 *
 * 1. All namespaces:
 * <code>
 * type: namespace
 * all: true
 * namespace: null
 * </code>
 *
 * 2. Namespace with 'My namespace'
 * <code>
 * type: namespace
 * all: false
 * namespace: 'My namespace'
 * </code>
 *
 * 3. All components in all namespaces:
 * <code>
 * type: component
 * all: true
 * namespace: null
 * </code>
 *
 * 4. All Components in given namespace:
 * <code>
 * type: component
 * all: true
 * namespace: 'My namespace'
 * </code>
 *
 * 4. Component with given name in given namespace:
 * <code>
 * type: component
 * all: false
 * namespace: 'My namespace'
 * name: 'My component'
 * </code>
 *
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public class APIRequest {

    /**
     * All for this kind of resource?
     */
    private boolean all = false;

    /**
     * Resource Type
     */
    private String type;


    /**
     * Namespace
     */
    private String namespace;

    /**
     * Name
     */
    private String name;

    /**
     * Parameters
     */
    private Parameters parameters;

    /**
     * Path
     */
    private String path;


    private String method;

    /**
     * Resource Types
     *
     * namespace
     * component
     * bean
     * spec
     */
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * If namespace is null means all namespaces.
     */
    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    /**
     * Resource Name
     *
     * if name is null means all resources
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAll() {
        return all;
    }

    public void setAll(boolean all) {
        this.all = all;
    }

    /**
     * Query String
     */
    public Parameters getParameters() {
        return parameters;
    }

    public void setParameters(Parameters parameters) {
        this.parameters = parameters;
    }

    public void setQueryParams(Map<String, String> queryParams) {
        setParameters(new ParametersMap<>(queryParams));
    }

    /**
     * Original path
     */
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Method
     */
    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
