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

import org.femtoframework.coin.naming.CoinName;

import javax.naming.Name;
import javax.naming.NamingException;

/**
 * Coin Lookup
 * <p>
 *
 * 1. Name follows this syntax 'namespace:name', it can point to a bean
 * 2. 'namespace:name.childName' can indicate a child of a bean if the bean has a child component with name 'child_name'
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public interface CoinLookup {

    /**
     * Resolve object by Name
     *
     * @param name Name
     * @return Object
     */
    Object lookup(Name name);


    /**
     * Resolve object by Name in string
     *
     * @param name Name
     * @return Object
     */
    default Object lookup(String name) throws NamingException {
        return lookup(new CoinName(name));
    }
}
