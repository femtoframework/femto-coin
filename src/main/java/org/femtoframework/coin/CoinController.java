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

/**
 * Coin Control
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public interface CoinController {
    /**
     * Create beans by specs
     *
     * @param uri Spec URI
     */
    void create(URI uri) throws IOException;

    /**
     * Create beans by specs
     *
     * @param file Spec File
     */
    default void create(File file) throws IOException {
        create(file.toURI());
    }

    /**
     * Apply changes by specs
     *
     * @param uri Spec URI
     */
    void apply(URI uri)  throws IOException;

    /**
     * Apply changes by specs
     *
     * @param file Spec File
     */
    default void apply(File file)  throws IOException {
        apply(file.toURI());
    }


    /**
     * Delete beans by specs
     *
     * @param uri Spec URI
     */
    void delete(URI uri)  throws IOException;

    /**
     *  Delete beans by specs
     *
     * @param file Spec File
     */
    default void delete(File file) throws IOException {
        delete(file.toURI());
    }
}
