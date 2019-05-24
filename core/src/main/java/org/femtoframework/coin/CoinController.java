package org.femtoframework.coin;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Coin Control
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public interface CoinController {

    /**
     * Get all component yaml files in given class loader
     *
     * @param classLoader Class Loader
     * @return All "META-INF/spec/component.yaml" in classpaths
     * @throws IOException
     */
    List<URI> getComponentYamls(ClassLoader classLoader) throws IOException;

    /**
     * Get application yaml files in given class loader
     *
     * @param classLoader Class Loader
     * @return "META-INF/spec/application.yaml" and "META-INF/spec/{ENV}/application.yaml" in classpaths {ENV} is the current environment
     * @throws IOException
     */
    List<URI> getApplicationYamls(ClassLoader classLoader) throws IOException;

    /**
     * All the URIs which passed through #create
     *
     * @return URIs
     */
    Set<URI> getUris();

    /**
     * Create beans by specs;
     *
     * @param uris Spec URI
     */
    void create(URI... uris) throws IOException;

    /**
     * Create beans by specs;
     *
     * @param files Spec File
     */
    default void create(File... files) throws IOException {
        create(Arrays.stream(files).map(f -> f.toURI()).toArray(URI[]::new));
    }

    /**
     * Apply changes on specs and beans
     *
     * @param uris Spec URI
     */
    void apply(URI... uris)  throws IOException;

    /**
     * Apply changes on specs and beans
     *
     * @param files Spec File
     */
    default void apply(File... files)  throws IOException {
        create(Arrays.stream(files).map(f -> f.toURI()).toArray(URI[]::new));
    }


    /**
     * Delete beans by specs
     *
     * @param uris Spec URI
     */
    void delete(URI... uris)  throws IOException;

    /**
     *  Delete beans by specs
     *
     * @param files Spec File
     */
    default void delete(File... files) throws IOException {
        create(Arrays.stream(files).map(f -> f.toURI()).toArray(URI[]::new));
    }
}
