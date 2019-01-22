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
package org.femtoframework.coin.ext;

import org.femtoframework.coin.*;
import org.femtoframework.coin.naming.CoinNamingParser;

import javax.naming.Name;
import javax.naming.NamingException;

/**
 * Simple Coin Lookup
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public class SimpleCoinLookup implements CoinLookup {


    private NamespaceFactory namespaceFactory;

    private CoinNamingParser namingParser = new CoinNamingParser();


    public SimpleCoinLookup(NamespaceFactory namespaceFactory) {
        this.namespaceFactory = namespaceFactory;
    }

    /**
     * Resolve object by Name in string
     *
     * @param name Name
     * @return Object
     */
    public Object lookup(String name) throws NamingException {
        int index = name.indexOf(CoinConstants.CHAR_DELIM);
        if (index == -1) {
            //Simple Name
            index = name.indexOf(CoinConstants.CHAR_COLON);
            if (index == -1) {
                return namespaceFactory.get(name);
            }
            String namespace = name.substring(0, index);
            Namespace ns = namespaceFactory.get(namespace);
            if (ns == null) {
                throw new NamingException("No such namespace:" + namespace + " in name:" + name);
            }
            String on = name.substring(index+1);
            Component component = ns.getComponentFactory().get(on);
            if (component == null) {
                throw new IllegalArgumentException("No such component:" + on + " in namespace:" + namespace);
            }
            return component.getBean();
        }
        else {
            return lookup(namingParser.parse(name));
        }
    }

    /**
     * Resolve object by Name
     *
     * @param name Name
     * @return Object
     */
    @Override
    public Object lookup(Name name) {
        int size = name.size();
        if (size == 0) {
            return null;
        }
        else {
            String n = name.get(0);
            Namespace ns = namespaceFactory.get(n);
            if (ns == null) {
                throw new IllegalArgumentException("No such namespace:" + n);
            }
            if (size == 1) {
                return ns;
            }
            else {
                String on = name.get(1);
                Component component = ns.getComponentFactory().get(on);
                if (component == null) {
                    throw new IllegalArgumentException("No such component:" + on + " in namespace:" + n);
                }
                int i = 2;
                Component next = component;
                while(size > i) {
                    String cn = name.get(i);
                    next = next.getChild(cn);
                    if (next == null) {
                        throw new IllegalArgumentException("No such component:" + cn + " in component:" + next);
                    }
                    i ++;
                }
                return next.getBean();
            }
        }
    }
}
