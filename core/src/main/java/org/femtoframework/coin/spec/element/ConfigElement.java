package org.femtoframework.coin.spec.element;

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

    protected static class ParametersAdapter implements Parameters<Object> {

        MapSpec<Element> element;

        public ParametersAdapter(MapSpec<Element> element) {
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
            Element value = element.get(key);
            return toValue(value);
        }

        protected Object toValue(Element value) {
            if (value instanceof MapSpec) {
                return new ParametersAdapter((MapSpec)value);
            }
            else if (value instanceof ListSpec) {
                List list = new ArrayList();
                for(Element e : ((ListSpec<Element>)value)) {
                    list.add(toValue(e));
                }
                return list;
            }
            else if (value instanceof SetSpec) {
                Set set = new HashSet();
                for(Element e : ((SetSpec<Element>)value)) {
                    set.add(toValue(e));
                }
                return set;
            }
            else if (value instanceof PrimitiveSpec) {
                return ((PrimitiveSpec)value).getPrimitiveValue();
            }
            else {
                return value;
            }
        }

        @Override
        public Object put(String key, Object value) {
            Element oldElement = element.get(key);
            if (value instanceof Map) {
                element.put(key, new MapElement<>((Map)value));
            }
            else if (value instanceof List) {
                element.put(key, new ListElement<>((List)value));
            }
            else if (value instanceof Set) {
                element.put(key, new SetElement<>((Set)value));
            }
            else if (value instanceof String) {
                String v = (String)value;
                if (v.startsWith("${") && v.endsWith("}")) {
                    element.put(key, new VariableElement(CoreKind.VAR, v.substring(2, v.length()-1), v));
                }
                else {
                    element.put(key, new PrimitiveElement<>(CoreKind.STRING, v));
                }
            }
            else if (value instanceof Integer) {
                element.put(key, new PrimitiveElement<>(CoreKind.INT, (Integer)value));
            }
            else if (value instanceof Long) {
                element.put(key, new PrimitiveElement<>(CoreKind.LONG, (Long)value));
            }
            else if (value instanceof Boolean) {
                element.put(key, new PrimitiveElement<>(CoreKind.INT, (Boolean)value));
            }
            else {
                element.put(key, new PrimitiveElement<>(CoreKind.STRING, String.valueOf(value)));
            }
            return toValue(oldElement);
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
