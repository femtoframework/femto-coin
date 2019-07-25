package org.femtoframework.coin.remote;

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
         * @param uri          Remote URI
         * @param interfaces   Interfaces the generated bean should have, could be null, if there is no interface
         * @return Generated Object
         */
        @Override
        public Object generate(String expectedType, String uri, String... interfaces) {
            return null;
        }
    };

    /**
     * Generate object
     *
     * @param expectedType Expected Type
     * @param uri          Remote URI
     * @param interfaces   Interfaces the generated bean should have, could be null, if there is no interface
     * @return Generated Object
     */
    Object generate(String expectedType, String uri, String... interfaces);

}
