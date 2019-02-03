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

/**
 * Coin Constants
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public interface CoinConstants {

    String NAME_MODULE = "module";
    String NAME_CONTROLLER = "controller";
    String NAME_LOOKUP = "lookup";
    String NAME_VARIABLE_RESOLVER_FACTORY = "variable_resolver_factory";
    String NAME_CONFIGURATOR_FACTORY = "configurator_factory";
    String NAME_LIFECYCLE_STRATEGY = "lifecycle_strategy";
    String NAME_NAMESPACE_FACTORY = "namespace_factory";
    String NAME_KIND_SPEC_FACTORY = "kind_spec_factory";

    String NAMESPACE_COIN = "coin";

    String NAMESPACE_NAMESPACE = "namespace";

    String DEFAULT_NAMESPACE = "default";


    String COMPONENT_YAML = "META-INF/spec/component.yaml";
    String APPLICATION_YAML = "META-INF/spec/application.yaml";

    char CHAR_DELIM = '.';
    char CHAR_COLON = ':';

    String RESOURCE_COMPONENT = "component";
    String RESOURCE_BEAN = "bean";
    String RESOURCE_SPEC = "spec";
    String RESOURCE_CONFIG = "config";
}
