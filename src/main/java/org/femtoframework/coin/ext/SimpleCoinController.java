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

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLParser;
import org.femtoframework.coin.*;
import org.femtoframework.coin.exception.NoSuchNamespaceException;
import org.femtoframework.coin.spec.*;
import org.femtoframework.coin.spec.databind.deser.CoinBeanDeserializerModifier;
import org.femtoframework.coin.spec.element.ModelElement;
import org.femtoframework.util.StringUtil;

import java.io.IOException;
import java.net.URI;
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

    private JsonFactory jsonFactory = new JsonFactory();
    private ObjectMapper jsonMapper = new ObjectMapper(jsonFactory);

    private YAMLFactory yamlFactory = new YAMLFactory();
    private ObjectMapper yamlMapper = new ObjectMapper(yamlFactory);

    {
        SimpleModule moduleMap = new SimpleModule();
        moduleMap.setDeserializerModifier(new CoinBeanDeserializerModifier());

        jsonMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        jsonMapper.registerModule(moduleMap);

        yamlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        yamlMapper.registerModule(moduleMap);
    }


    /**
     * Create beans by specs
     *
     * @param uri Spec URI
     */
    @Override
    public void create(URI uri) throws IOException {
        List<LinkedHashMap> maps = toMaps(uri);

        int index = 1;
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
                if (oldSpec != null) {
                    throw new IOException("BeanSpec " + namespace + ":" + name + " existing already");
                }
                ns.getBeanSpecFactory().add(beanSpec);
            }
            else {
                //TODO CUSTOM Spec
                throw new IOException("Unsupported kind yet:" + kind);
            }
        }
    }


    protected List<LinkedHashMap> toMaps(URI uri) throws IOException {
        String path = uri.getPath();
        String lowerCase = path.toLowerCase();
        if (lowerCase.endsWith(".yaml") || lowerCase.endsWith(".yml")) { //Yaml
            try (YAMLParser parser = yamlFactory.createParser(uri.toURL())) {
                return yamlMapper.readValues(parser, LinkedHashMap.class).readAll();
            }
        }
        else if (lowerCase.endsWith(".json")) {
            try (JsonParser parser = jsonFactory.createParser(uri.toURL())) {
                return jsonMapper.readValues(parser, LinkedHashMap.class).readAll();
            }
        }
        else {
            throw new IllegalArgumentException("Unrecognized file type:" + path);
        }
    }

    /**
     * Apply changes by specs
     *
     * @param uri Spec URI
     */
    @Override
    public void apply(URI uri) throws IOException {
        int index = 1;
        List<LinkedHashMap> maps = toMaps(uri);
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
     * @param uri Spec URI
     */
    @Override
    public void delete(URI uri) throws IOException {
        int index = 1;
        List<LinkedHashMap> maps = toMaps(uri);
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
}
