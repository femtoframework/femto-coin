/**
 * Licensed to the FemtoFramework under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.femtoframework.coin;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

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
     * @return All "META-INF/spec/application.yaml" in classpaths
     * @throws IOException
     */
    URI getApplicationYaml(ClassLoader classLoader) throws IOException;

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
