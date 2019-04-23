package org.femtoframework.coin.spec;

import org.femtoframework.coin.spec.element.ModelElement;
import org.femtoframework.util.DataUtil;

import java.util.Map;

import static org.femtoframework.coin.CoinConstants.API_VERSION;
import static org.femtoframework.coin.CoinConstants.VERSION_COIN_V1;

/**
 * Element Map
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public interface MapSpec<E extends Element> extends Element, Map<String, E> {
    /**
     * Start visiting this spec
     *
     * @param visitor Visitor
     */
    default void accept(ElementVisitor visitor) {
        visitor.visitMap(this);
    }


    static String getApiVersion(Map map) {
        return getApiVersion(map, VERSION_COIN_V1);
    }

    static String getApiVersion(Map map, String defaultVersion) {
        return DataUtil.getString(MapSpec.getValue(map, API_VERSION), defaultVersion);
    }

    static Object getValue(Map map, String key) {
        return getValue(map, key, null);
    }

    static Object getValue(Map map, String key, Object defValue) {
        Element element = (Element)map.get(key);
        if (element != null) {
            Object value = element.getValue((Class<?>)null, null);
            return value != null ? value : defValue;
        }
        return defValue;
    }

    static String getString(Map map, String key, String defValue) {
        return DataUtil.getString(getValue(map, key), defValue);
    }

    Object getValue(String key);

    Object getValue(String key, Object defValue);

    String getString(String key, String defValue);
}
