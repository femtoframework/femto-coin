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

import org.femtoframework.bean.BeanStage;
import org.femtoframework.bean.NamedBean;
import org.femtoframework.coin.*;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Base Factory
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public class BaseFactory<B> implements Factory<B> {


    private String namespace;
    protected Map<String, B> map = new LinkedHashMap<>();

    protected void add(String name, B object) {
        map.put(name, object);
    }

    protected void add(NamedBean bean) {
        add(bean.getName(), (B)bean);
    }

    private NamespaceFactory namespaceFactory;

    protected BaseFactory(NamespaceFactory namespaceFactory, String namespace) {
        setNamespace(namespace);
        setNamespaceFactory(namespaceFactory);
    }

    protected void setNamespaceFactory(NamespaceFactory namespaceFactory) {
        this.namespaceFactory = namespaceFactory;
    }

    /**
     * Namespace of this factory
     *
     * @return Namespace
     */
    @Override
    public String getNamespace() {
        return namespace;
    }

    /**
     * Return all names
     *
     * @return all names
     */
    @Override
    public Set<String> getNames() {
        return map.keySet();
    }

    /**
     * Return object by given name
     *
     * @param name Name
     * @return object in this factory
     */
    @Override
    public B get(String name) {
        return map.get(name);
    }

    /**
     * Delete the object by given name
     *
     * @param name Name
     * @return Deleted object
     */
    @Override
    public B delete(String name) {
        return map.remove(name);
    }

    /**
     * Returns an iterator over elements of kind {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<B> iterator() {
        return map.values().iterator();
    }


    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public NamespaceFactory getNamespaceFactory() {
        return namespaceFactory;
    }


    /**
     * Apply stage to all its children
     *
     * @param namePrefix Prefix to put them in namespace, for example, configurator "auto" will be use name "configurator_auto" in namespace "coin"
     * @param stage Target Stage
     */
    protected void applyStageToChildren(String namePrefix, BeanStage stage) {
        NamespaceFactory namespaceFactory = getNamespaceFactory();
        Namespace namespace = namespaceFactory.get(CoinConstants.NAMESPACE_COIN);
        ComponentFactory componentFactory = namespace.getComponentFactory();
        for(String name: getNames()) {
            B bean = get(name);
            componentFactory.create(namePrefix + name , bean, stage);
        }
    }

}
