package org.femtoframework.coin.spec;

import org.femtoframework.coin.spec.element.MapElement;

import java.util.Map;

import static org.femtoframework.coin.CoinConstants.*;

/**
 * Metadata Spec
 */
public interface MetadataSpec<E extends Element> extends MapSpec<E> {

    default String getName() {
        return getString(NAME, null);
    }

    default String getNamespace() {
        return getString(NAMESPACE, NAMESPACE_DEFAULT);
    }

    /**
     * Return generated name
     *
     * @return generated name
     */
    default String getGenerateName() {
        return getName();
    }

    default MapSpec<E> getLabels() {
        Map map = (Map)get(LABELS);
        if (map == null) {
            MapSpec<E> spec = new MapElement<E>();
            put(LABELS, (E)spec);
            return spec;
        }
        else if (map instanceof MapSpec) {
            return (MapSpec<E>)map;
        }
        else {
            return new MapElement<>(map);
        }
    }

    default MapSpec<E> getAnnotations() {
        Map map = (Map)get(ANNOTATIONS);
        if (map == null) {
            MapSpec<E> spec = new MapElement<E>();
            put(ANNOTATIONS, (E)spec);
            return spec;
        }
        else if (map instanceof MapSpec) {
            return (MapSpec<E>)map;
        }
        else {
            return new MapElement<>(map);
        }
    }
}
