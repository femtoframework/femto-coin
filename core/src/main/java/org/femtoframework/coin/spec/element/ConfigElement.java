package org.femtoframework.coin.spec.element;

import org.femtoframework.coin.spec.ConfigSpec;
import org.femtoframework.coin.spec.CoreKind;
import org.femtoframework.coin.spec.SpecConstants;
import org.femtoframework.parameters.Parameters;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class ConfigElement extends ModelElement implements ConfigSpec, SpecConstants {

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
        put(_NAMESPACE, new PrimitiveElement<>(CoreKind.STRING, namespace));
    }

    /**
     * Return namespace of current config spec
     *
     * @return namespace
     */
    public String getNamespace() { //Default is "current" namespace
        return getString(_NAMESPACE, ConfigSpec.super.getNamespace());
    }

    protected static class ParametersAdapter implements Parameters<Object> {

        ConfigElement element;

        public ParametersAdapter(ConfigElement element) {
            this.element = element;
        }

        @Override
        public int size() {
            return element.size();
        }

        @Override
        public boolean isEmpty() {
            return element.isEmpty();
        }

        @Override
        public boolean containsKey(Object key) {
            return element.containsKey(key);
        }

        @Override
        public boolean containsValue(Object value) {
            throw new UnsupportedOperationException("Unsupported");
        }

        public Object get(Object key) {
            return ModelElement.getValue(element, String.valueOf(key));
        }

        @Override
        public Object put(String key, Object value) {
            throw new UnsupportedOperationException("Unsupported");
        }

        @Override
        public Object remove(Object key) {
            throw new UnsupportedOperationException("Unsupported");
        }

        @Override
        public void putAll(Map<? extends String, ?> m) {
            throw new UnsupportedOperationException("Unsupported");
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException("Unsupported");
        }

        @Override
        public Set<String> keySet() {
            return element.keySet();
        }

        @Override
        public Collection<Object> values() {
            throw new UnsupportedOperationException("Unsupported");
        }

        @Override
        public Set<Entry<String, Object>> entrySet() {
            throw new UnsupportedOperationException("Unsupported");
        }
    }

    private transient ParametersAdapter adapter = null;

    /**
     * Return the parameter values only
     *
     * @return Parameters which only has the key-value pairs
     */
    @Override
    public <V> Parameters<V> getParameters() {
        if (adapter == null) {
            adapter = new ParametersAdapter(this);
        }
        return (Parameters<V>)adapter;
    }
}
