/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
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
import java.net.URI;

/**
 * Coin Control
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public interface CoinControl {
    /**
     * Create beans by specs
     *
     * @param uri Spec URI
     */
    void create(URI uri);

    /**
     * Create beans by specs
     *
     * @param file Spec File
     */
    void create(File file);

    /**
     * Apply changes by specs
     *
     * @param uri Spec URI
     */
    void apply(URI uri);

    /**
     * Apply changes by specs
     *
     * @param file Spec File
     */
    void apply(File file);


    /**
     * Delete beans by specs
     *
     * @param uri Spec URI
     */
    void delete(URI uri);

    /**
     *  Delete beans by specs
     *
     * @param file Spec File
     */
    void delete(File file);
}
