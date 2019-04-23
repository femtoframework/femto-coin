package org.femtoframework.coin.spec.element;

import org.femtoframework.coin.spec.*;

import java.util.Map;

import static org.femtoframework.coin.CoinConstants.*;

/**
 * Abstract Model Element,
 *
 * Model means object with version and name
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public abstract class ModelElement<SPEC extends MapSpec<Element>> extends MapElement<Element> implements ModelSpec<SPEC> {

    public ModelElement() {
    }

    public ModelElement(Map map) {
        super(map);
    }

    protected ModelElement(Kind kind) {
        super(kind);
    }

    /**
     * Name of the object
     *
     * @return Name of the object
     */
    @Override
    public String getName() {
        return getMetadata().getString(NAME, null);
    }

    public void setName(String name) {
        getMetadata().put(NAME, new PrimitiveElement<>(CoreKind.STRING, name));
    }


    /**
     * Return Metadata
     *
     * @return Metadata
     */
    public MetadataSpec<Element> getMetadata() {
        MapSpec<Element> metadata = (MapSpec<Element>)get(METADATA);
        if (metadata == null) {
            metadata = new MetadataElement<>();
            put(METADATA, metadata);
        }
        else if (metadata instanceof MetadataElement) {
            return (MetadataElement)metadata;
        }
        else {
            metadata = new MetadataElement<>(metadata);
            put(METADATA, metadata);
        }
        return (MetadataElement)metadata;
    }

    /**
     * Return Spec
     *
     * @return Spec
     */
    public SPEC getSpec() {
        return (SPEC)get(SPEC);
    }

}
