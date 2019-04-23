package org.femtoframework.coin.spec.element;

import org.femtoframework.coin.spec.Element;
import org.femtoframework.coin.spec.MapSpec;
import org.femtoframework.coin.spec.MetadataSpec;

public class MetadataElement<E extends Element> extends MapElement<E> implements MetadataSpec<E> {

    public MetadataElement() {
    }

    public MetadataElement(MapSpec<E> spec) {
        super(spec);
    }
}
