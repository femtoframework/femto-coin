package org.femtoframework.coin.spec.element;

import org.femtoframework.coin.spec.*;
import org.femtoframework.util.DataUtil;

import java.util.Map;

import static org.femtoframework.coin.CoinConstants.NAME;

/**
 * Abstract Model Element,
 *
 * Model means object with version and name
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public abstract class ModelElement extends MapElement<Element> implements ModelSpec, SpecConstants {

    public ModelElement() {
    }

    public ModelElement(Map map) {
        super(map);
    }

    protected ModelElement(Kind kind) {
        super(kind);
    }


    /**
     * Version
     */
    public String getVersion() {
        return getString(API_VERSION, ModelSpec.super.getVersion());
    }

    /**
     * Name of the object
     *
     * @return Name of the object
     */
    @Override
    public String getName() {
        String name = getString(_NAME, null);
        if (name == null) {
            name = getString(NAME, null);
        }
        return name;
    }

    public void setName(String name) {
        put(_NAME, new PrimitiveElement<>(CoreKind.STRING, name));
    }

    public static String getVersion(Map map) {
        return getVersion(map, SpecConstants.VERSION_CORE_KIND);
    }

    public static String getVersion(Map map, String defaultVersion) {
        return DataUtil.getString(ModelElement.getValue(map, SpecConstants.API_VERSION), defaultVersion);
    }

    public static Object getValue(Map map, String key) {
        Element element = (Element)map.get(key);
        if (element != null) {
            return element.getValue(null, null);
        }
        return null;
    }

    public static String getString(Map map, String key, String defValue) {
        return DataUtil.getString(getValue(map, key), defValue);
    }

    protected Object getValue(String key) {
        return getValue(this, key);
    }

    protected String getString(String key, String defaultValue) {
        return DataUtil.getString(getValue(key), defaultValue);
    }

}
