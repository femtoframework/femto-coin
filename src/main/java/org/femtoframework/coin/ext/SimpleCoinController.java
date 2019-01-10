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
import org.femtoframework.coin.*;
import org.femtoframework.coin.exception.NoSuchNamespaceException;
import org.femtoframework.coin.spec.*;
import org.femtoframework.coin.spec.element.ModelElement;
import org.femtoframework.implement.ImplementUtil;
import org.femtoframework.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * Coin Controller
 * 
 * @author Sheldon Shao
 * @version 1.0
 */
public class SimpleCoinController implements CoinController {

    //Object Mapper is thread safe
    private KindSpecFactory kindSpecFactory;

    private NamespaceFactory namespaceFactory;

    private SpecParser specParser;

    /**
     * Logger
     */
    private Logger log = LoggerFactory.getLogger(SimpleCoinController.class);

    /**
     * Create beans by specs
     *
     * @param uris Spec URI
     */
    @Override
    public void create(URI... uris) throws IOException {
        List<LinkedHashMap> maps = toMaps(uris);
        List<BeanSpec> beanSpecs = createSpecs(maps);
        createBeans(beanSpecs);
        keepStage(beanSpecs, BeanStage.CONFIGURE);
        keepStage(beanSpecs, BeanStage.INITIALIZE);
        keepStage(beanSpecs, BeanStage.START);
    }

    protected List<BeanSpec> createSpecs(List<LinkedHashMap> specs) throws IOException {
        int index = 1;
        List<BeanSpec> beanSpecs = new ArrayList<>();
        for(LinkedHashMap map: specs) {
            String version = ModelElement.getVersion(map);
            KindSpec kindSpec = kindSpecFactory.get(version);
            if (kindSpec == null) {
                throw new IOException("Version:" + version + " doesn't support, please make sure " +
                        "your KindSpec implementation has been put in your jar " +
                        "file /META-INF/spec/org.femtoframework.coin.spec.KindSpec.impl");
            }
            MapSpec spec = kindSpec.toSpec(map);
            Kind kind = spec.getKind();
            if (kind == CoreKind.NAMESPACE) {
                String name = ModelElement.getString(spec, SpecConstants._NAME, null);
                if (StringUtil.isInvalid(name)) {
                    throw new IOException("No name in kind 'namespace', spec index:" + index);
                }
                Namespace ns = namespaceFactory.get(name);
                if (ns != null) {
                    throw new IOException("Namespace existing already:" + name);
                }
                namespaceFactory.createNamespace(name);
            }
            else if (kind == CoreKind.BEAN) {
                BeanSpec beanSpec = (BeanSpec)spec;
                String namespace = beanSpec.getNamespace();
                Namespace ns = namespaceFactory.get(namespace);
                if (ns == null) {
                    throw new NoSuchNamespaceException("No such namespace:" + namespace + " in spec index:" + index);
                }
                String name = beanSpec.getName();
                BeanSpec oldSpec = ns.getBeanSpecFactory().get(name);
                if (log.isWarnEnabled()) { //Since application spec is allowing to override the spec within component spec
                    log.warn("A new BeanSpec with same namespace and name has been found, replacing with new one, "
                            + oldSpec.getQualifiedName());
                }
//                if (oldSpec != null) {
//                    throw new IOException("BeanSpec " + namespace + ":" + name + " existing already");
//                }
                ns.getBeanSpecFactory().add(beanSpec);
                beanSpecs.add(beanSpec);
            }
            else {
                //TODO CUSTOM Spec
                throw new IOException("Unsupported kind yet:" + kind);
            }
        }
        return beanSpecs;
    }

    protected void createBeans( List<BeanSpec> specs) {
        for(BeanSpec spec: specs) { //Create all components
            if (spec.isEnabled()) {
                String namespace = spec.getNamespace();
                Namespace ns = namespaceFactory.get(namespace);
                ns.getComponentFactory().create(spec.getName(), spec, BeanStage.CREATE);
            }
        }
    }

    protected void keepStage(List<BeanSpec> specs, BeanStage stage) {
        for(BeanSpec spec: specs) { //Configure all beans
            if (spec.isEnabled()) {
                String namespace = spec.getNamespace();
                Namespace ns = namespaceFactory.get(namespace);
                ComponentFactory cf = ns.getComponentFactory();
                Component component = cf.get(spec.getName());
                component.setStage(stage);
                cf.keep(component, stage);
            }
        }
    }


    protected List<LinkedHashMap> toMaps(URI... uris) throws IOException {
        List<LinkedHashMap> all = new ArrayList<>();
        for(URI u: uris) {
            all.addAll(getSpecParser().parseSpec(u));
        }
        return all;
    }

    /**
     * Apply changes by specs
     *
     * @param uris Spec URI
     */
    @Override
    public void apply(URI... uris) throws IOException {
        int index = 1;
        List<LinkedHashMap> maps = toMaps(uris);
        for(LinkedHashMap map: maps) {
            String version = ModelElement.getVersion(map);
            KindSpec kindSpec = kindSpecFactory.get(version);
            if (kindSpec == null) {
                throw new IOException("Version:" + version + " doesn't support, please make sure " +
                        "your KindSpec implementation has been put in your jar " +
                        "file /META-INF/spec/org.femtoframework.coin.spec.KindSpec.impl");
            }
            MapSpec spec = kindSpec.toSpec(map);
            Kind kind = spec.getKind();
            if (kind == CoreKind.NAMESPACE) {
                String name = ModelElement.getString(spec, SpecConstants._NAME, null);
                if (StringUtil.isInvalid(name)) {
                    throw new IOException("No name in kind 'namespace', spec index:" + index);
                }
                Namespace ns = namespaceFactory.get(name);
                if (ns == null) { //No such namespace already
                    throw new NoSuchNamespaceException("No such namespace:" + name + " in spec index:" + index);
                }
                //TODO another attribute to update?
            }
            else if (kind == CoreKind.BEAN) {
                BeanSpec beanSpec = (BeanSpec)spec;
                String namespace = beanSpec.getNamespace();
                Namespace ns = namespaceFactory.get(namespace);
                if (ns == null) {
                    throw new NoSuchNamespaceException("No such namespace:" + namespace + " in spec index:" + index);
                }
                String name = beanSpec.getName();
                BeanSpec oldSpec = ns.getBeanSpecFactory().get(name);
                if (oldSpec == null) {
                    throw new IOException("BeanSpec " + namespace + ":" + name + " doesn't exist.");
                }

                //TODO apply new beanSpec
                Component component = ns.getComponentFactory().get(name);
                if (component != null) {

                }
            }
            else {
                //TODO CUSTOM Spec
                throw new IOException("Unsupported kind yet:" + kind);
            }
        }
    }

    /**
     * Delete beans by specs
     *
     * @param uris Spec URI
     */
    @Override
    public void delete(URI... uris) throws IOException {
        int index = 1;
        List<LinkedHashMap> maps = toMaps(uris);
        for(LinkedHashMap map: maps) {
            String version = ModelElement.getVersion(map);
            KindSpec kindSpec = kindSpecFactory.get(version);
            if (kindSpec == null) {
                throw new IOException("Version:" + version + " doesn't support, please make sure " +
                        "your KindSpec implementation has been put in your jar " +
                        "file /META-INF/spec/org.femtoframework.coin.spec.KindSpec.impl");
            }
            MapSpec spec = kindSpec.toSpec(map);
            Kind kind = spec.getKind();
            if (kind == CoreKind.NAMESPACE) {
                String name = ModelElement.getString(spec, SpecConstants._NAME, null);
                if (StringUtil.isInvalid(name)) {
                    throw new IOException("No name in kind 'namespace', spec index:" + index);
                }
                Namespace ns = namespaceFactory.get(name);
                if (ns == null) { //No such namespace already
                    return;
                }
                namespaceFactory.delete(name);
            }
            else if (kind == CoreKind.BEAN) {
                BeanSpec beanSpec = (BeanSpec)spec;
                String namespace = beanSpec.getNamespace();
                Namespace ns = namespaceFactory.get(namespace);
                if (ns == null) {
                    throw new NoSuchNamespaceException("No such namespace:" + namespace + " in spec index:" + index);
                }
                String name = beanSpec.getName();

                ns.getBeanFactory().delete(name);
                ns.getBeanSpecFactory().delete(name);
                Component component = ns.getComponentFactory().get(name);
                if (component != null) {
                    ns.getComponentFactory().delete(name);
                }
            }
            else {
                //TODO CUSTOM Spec
                throw new IOException("Unsupported kind yet:" + kind);
            }
        }
    }

    public KindSpecFactory getKindSpecFactory() {
        return kindSpecFactory;
    }

    public void setKindSpecFactory(KindSpecFactory kindSpecFactory) {
        this.kindSpecFactory = kindSpecFactory;
    }

    public NamespaceFactory getNamespaceFactory() {
        return namespaceFactory;
    }

    public void setNamespaceFactory(NamespaceFactory namespaceFactory) {
        this.namespaceFactory = namespaceFactory;
    }

    public SpecParser getSpecParser() {
        if (specParser == null) {
            specParser = ImplementUtil.getInstance(SpecParser.class);
        }
        return specParser;
    }

    public void setSpecParser(SpecParser specParser) {
        this.specParser = specParser;
    }
}
