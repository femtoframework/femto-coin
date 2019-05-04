package org.femtoframework.coin.remote;

import java.util.List;

/**
 * Remote Generator
 *
 * Interface for integrating with remote service call
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public interface RemoteGenerator {

    RemoteGenerator NONE = new RemoteGenerator() {

        /**
         * Generate object
         *
         * @param expectedType Expected Type
         * @param interfaces   Interfaces the generated bean should have, could be null, if there is no interface
         * @param uri          Remote URI
         * @return Generated Object
         */
        @Override
        public Object generate(String expectedType, List<String> interfaces, String uri) {
            return null;
        }
    };

    /**
     * Generate object
     *
     * @param expectedType Expected Type
     * @param interfaces Interfaces the generated bean should have, could be null, if there is no interface
     * @param uri Remote URI
     * @return Generated Object
     */
    Object generate(String expectedType, List<String> interfaces, String uri);
}
