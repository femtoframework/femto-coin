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
package org.femtoframework.coin.spec;

/**
 * Spec Constants
 *
 * All attribute names start with "_" are for bean spec only
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public interface SpecConstants {

    String VERSION_CORE_KIND = "v1";

    /**
     * Kind
     */
    String KIND = "kind";

    String METADATA = "metadata";

    String SPEC = "spec";

    String BEAN = "bean";

    String CONFIG = "config";


    /**
     * If it uses "_name" in the spec, the attribute won't be injected to bean
     */
    String _NAME = "_name";

    String _DEFAULT = "_default";


    String _NAMESPACE = "_namespace";

    String _ENABLED = "_enabled";

    String _TYPE = "_type";

    String API_VERSION = "apiVersion";

    String _ACCESS = "_access";

    String _SINGLETON = "_singleton";

    /**
     * Link the current bean to target beans
     */
    String _BELONGS_TO = "_belongsTo";
}
