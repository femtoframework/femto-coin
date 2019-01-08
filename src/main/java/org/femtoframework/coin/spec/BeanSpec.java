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

import org.femtoframework.coin.CoinConstants;

import java.util.Collections;
import java.util.List;

/**
 * Bean
 *
 * Bean means Model with type and other common stuffs
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public interface BeanSpec extends ModelSpec {

//    /**
//     * Return aliases
//     *
//     * @return aliases
//     */
//    default List<String> getAliases() {
//        return Collections.emptyList();
//    }

    /**
     * Return belongsTo
     *
     * belongsTo syntax
     *
     *
     * TODO
     * @return
     */
    default List<String> getBelongsTo() {
        return Collections.emptyList();
    }

    /**
     * Indicate the kind of this bean
     *
     * @return
     */
    String getType();

    /**
     * The real class of the kind
     *
     * @return kind class
     */
    Class<?> getTypeClass();

    /**
     * Bean Name
     *
     * @return Bean Name
     */
    String getName();


    /**
     * Is it singleton?
     * If it is true, then coin will try to create it in singleton pattern first.
     *
     * @return Singleton
     */
    default boolean isSingleton() {
        return false;
    }

    /**
     * Return namespace of current bean spec
     *
     * @return namespace
     */
    default String getNamespace() { //Default is "current" namespace
        return CoinConstants.DEFAULT_NAMESPACE;
    }

    /**
     * Whether it is default for the specific interface.
     * If multiple beans set default==true, the last one will be enabled as "default=true" in the factory
     *
     * @return is it the default implementation of the interface
     */
    default boolean isDefault() {
        return true;
    }

    /**
     * Whether the bean is enabled or not.
     *
     * TODO check that in component
     * @return
     */
    default boolean isEnabled() {
        return true;
    }

    /**
     * Start visiting this spec
     *
     * @param visitor Visitor
     */
    default void accept(ElementVisitor visitor) {
        visitor.visitBean(this);
    }
}
