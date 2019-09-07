package org.femtoframework.coin;

import org.femtoframework.coin.remote.RemoteGenerator;
import org.femtoframework.coin.spec.KindSpecFactory;
import org.femtoframework.implement.ImplementUtil;

/**
 * Coin Util
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public class CoinUtil {

    private static CoinModule module;

    static {
        module = ImplementUtil.getInstance(CoinModule.class);
        ImplementUtil.init(module);
    }

    public static CoinModule getModule() {
        return module;
    }

    /**
     * New Module for unit test
     *
     * @return Module
     */
    public static CoinModule newModule() {
        CoinModule module = ImplementUtil.getInstance(CoinModule.class, false);
        ImplementUtil.init(module);
        return module;
    }

    /**
     * Coin Control, maintain objects by Yaml or JSON
     *
     * @return Coin Control
     */
    public static CoinController getController() {
        return getModule().getController();
    }

    /**
     * Return KindSpecFactory
     *
     * @return KindSpecFactory
     */
    public static KindSpecFactory getKindSpecFactory() {
        return getModule().getKindSpecFactory();
    }


    /**
     * Generate object
     *
     * @param expectedType Expected Type
     * @param uri          Remote URI
     * @param interfaces   Interfaces the generated bean should have, could be null, if there is no interface
     * @return Generated Object
     */
    public static <T> T generate(Class<T> expectedType, String uri, Class... interfaces) {
        RemoteGenerator remoteGenerator = getModule().getRemoteGenerator();
        if (remoteGenerator != null) {
            String[] interfaceNames = interfaces != null ? new String[interfaces.length] : null;
            if (interfaceNames != null) {
                int i = 0;
                for(Class clazz : interfaces) {
                    interfaceNames[i++] = clazz.getName();
                }
                return (T)remoteGenerator.generate(expectedType.getName(), uri, interfaceNames);
            }
            else {
                return (T)remoteGenerator.generate(expectedType.getName(), uri);
            }
        }
        return null;
    }
}
