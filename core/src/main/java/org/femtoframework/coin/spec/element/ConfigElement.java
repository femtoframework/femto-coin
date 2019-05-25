package org.femtoframework.coin.spec.element;

import jdk.nashorn.internal.ir.annotations.Ignore;
import org.femtoframework.coin.spec.*;
import org.femtoframework.parameters.Parameters;

import java.util.*;

import static org.femtoframework.coin.CoinConstants.NAMESPACE;

public class ConfigElement extends ModelElement implements ConfigSpec {

    public ConfigElement() {
        super(CoreKind.CONFIG);
    }

    public ConfigElement(Map map) {
        super(map);
        setKind(CoreKind.CONFIG);
    }

    public ConfigElement(String namespace) {
        super(CoreKind.CONFIG);
        setNamespace(namespace);
    }

    public void setNamespace(String namespace) {
        put(NAMESPACE, new PrimitiveElement<>(CoreKind.STRING, namespace));
    }

    /**
     * Return namespace of current config spec
     *
     * @return namespace
     */
    public String getNamespace() { //Default is "current" namespace
        return getString(NAMESPACE, ConfigSpec.super.getNamespace());
    }

    @Ignore
    private transient ParametersSpecAdapter parameters = null;

    /**
     * Return the parameter values only
     *
     * @return Parameters which only has the key-value pairs
     */
    @Override
    public <V> Parameters<V> getParameters() {
        if (parameters == null) {
            parameters = new ParametersSpecAdapter(getSpec());
        }
        return (Parameters<V>)parameters;
    }
}
