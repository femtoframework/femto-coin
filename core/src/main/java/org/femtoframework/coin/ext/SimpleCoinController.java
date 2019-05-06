package org.femtoframework.coin.ext;

import org.femtoframework.bean.BeanStage;
import org.femtoframework.coin.*;
import org.femtoframework.bean.annotation.Ignore;
import org.femtoframework.coin.exception.NoSuchNamespaceException;
import org.femtoframework.coin.spec.*;
import org.femtoframework.implement.ImplementUtil;
import org.femtoframework.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;

import static org.femtoframework.coin.CoinConstants.*;


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
    @Ignore
    private Logger log = LoggerFactory.getLogger(SimpleCoinController.class);

    /**
     * Get all component yaml files in given class loader
     *
     * @param classLoader Class Loader
     * @return All "META-INF/spec/component.yaml" in classpaths
     * @throws IOException
     */
    @Override
    public List<URI> getComponentYamls(ClassLoader classLoader) throws IOException {
        if (classLoader == null) {
            classLoader = Thread.currentThread().getContextClassLoader();
        }
        List<URI> uris = new ArrayList<>();
        Enumeration<URL> en = classLoader.getResources(COMPONENT_YAML);
        while(en.hasMoreElements()) {
            try {
                uris.add(toURI(en.nextElement()));
            } catch (URISyntaxException e) {
                throw new IOException("URI syntax error", e);
            }
        }
        return uris;
    }

    /**
     * Get application yaml files in given class loader
     *
     * @param classLoader Class Loader
     * @return All "META-INF/spec/application.yaml" in classpaths
     * @throws IOException
     */
    @Override
    public URI getApplicationYaml(ClassLoader classLoader) throws IOException {
        if (classLoader == null) {
            classLoader = Thread.currentThread().getContextClassLoader();
        }
        try {
            return toURI(classLoader.getResource(APPLICATION_YAML));
        } catch (URISyntaxException e) {
            throw new IOException("URI syntax error", e);
        }
    }

    protected URI toURI(URL url) throws URISyntaxException {
        if (url == null) {
            return null;
        }
        else {
            return url.toURI();
        }
    }

    /**
     * Create beans by specs
     *
     * @param uris Spec URI
     */
    @Override
    public void create(URI... uris) throws IOException {
        List<LinkedHashMap> maps = toMaps(uris);
        List<ComponentSpec> beanSpecs = createSpecs(maps);
        createBeans(beanSpecs);
        keepStage(beanSpecs, BeanStage.CONFIGURE);
        keepStage(beanSpecs, BeanStage.INITIALIZE);
        keepStage(beanSpecs, BeanStage.START);
    }

    protected List<ComponentSpec> createSpecs(List<LinkedHashMap> specs) throws IOException {
        int index = 1;
        List<ComponentSpec> compSpecs = new ArrayList<>();
        for(LinkedHashMap map: specs) {
            String version = MapSpec.getApiVersion(map);
            KindSpec kindSpec = kindSpecFactory.get(version);
            if (kindSpec == null) {
                throw new IOException("Version:" + version + " doesn't support, please make sure " +
                        "your KindSpec implementation has been put in your jar " +
                        "file /META-INF/spec/implements.properties");
            }
            MapSpec spec = kindSpec.toSpec(map);
            Kind kind = spec.getKind();
            if (kind == CoreKind.NAMESPACE) {
                NamespaceSpec namespaceSpec = (NamespaceSpec)spec;
                String name = namespaceSpec.getName();
                if (StringUtil.isInvalid(name)) {
                    throw new IOException("No name in kind 'namespace', spec index:" + index);
                }
                Namespace ns = namespaceFactory.getNamespace(name, false);
                if (ns != null) {
                    throw new IOException("Namespace existing already:" + name);
                }
                namespaceFactory.createNamespace(name, ((NamespaceSpec)spec));
            }
            else if (kind == CoreKind.COMPONENT) {
                ComponentSpec compSpec = (ComponentSpec)spec;
                String namespace = compSpec.getNamespace();
                Namespace ns = namespaceFactory.getNamespace(namespace, true);
                String name = compSpec.getName();
                SpecFactory<ComponentSpec> specFactory = ns.getSpecFactory(ComponentSpec.class);
                ComponentSpec oldSpec = specFactory.get(name);
                if (oldSpec != null && log.isWarnEnabled()) { //Since application spec is allowing to override the spec within component spec
                    log.warn("A new BeanSpec with same namespace and name has been found, replacing with new one, "
                            + oldSpec.getNamespace() + ":" + oldSpec.getName());
                }
                specFactory.add(compSpec);
                compSpecs.add(compSpec);
            }
            else if (kind == CoreKind.CONFIG) {
                ConfigSpec configSpec = (ConfigSpec)spec;
                String namespace = configSpec.getNamespace();
                Namespace ns = namespaceFactory.getNamespace(namespace, true);
                String name = configSpec.getName();
                SpecFactory<ConfigSpec> specFactory = ns.getSpecFactory(ConfigSpec.class);
                if (name == null) {
                    name = "config_" + specFactory.getNames().size();
                    configSpec.setName(name);
                }
                ModelSpec oldSpec = specFactory.get(name);
                if (oldSpec != null && log.isWarnEnabled()) { //Since application spec is allowing to override the spec within component spec
                    log.warn("A new ConfigSpec with same namespace and name has been found, replacing with new one, "
                            + oldSpec.getName());
                }
                specFactory.add(configSpec);
            }
            else {
                throw new IOException("Unsupported kind yet:" + kind);
            }
        }
        return compSpecs;
    }

    protected void createBeans(List<ComponentSpec> specs) {
        for(ComponentSpec spec: specs) { //Create all components
            if (spec.isEnabled()) {
                String namespace = spec.getNamespace();
                Namespace ns = namespaceFactory.get(namespace);
                ns.getComponentFactory().create(spec.getName(), spec, BeanStage.CREATE);
            }
        }
    }

    protected void keepStage(List<ComponentSpec> specs, BeanStage stage) {
        for(ComponentSpec spec: specs) { //Configure all beans
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
            String version = MapSpec.getApiVersion(map);
            KindSpec kindSpec = kindSpecFactory.get(version);
            if (kindSpec == null) {
                throw new IOException("Version:" + version + " doesn't support, please make sure " +
                        "your KindSpec implementation has been put in your jar " +
                        "file /META-INF/spec/implements.properties");
            }
            MapSpec spec = kindSpec.toSpec(map);
            Kind kind = spec.getKind();
            if (kind == CoreKind.NAMESPACE) {
                NamespaceSpec namespaceSpec = (NamespaceSpec)spec;
                String name = namespaceSpec.getName();
                if (StringUtil.isInvalid(name)) {
                    throw new IOException("No name in kind 'namespace', spec index:" + index);
                }
                Namespace ns = namespaceFactory.get(name);
                if (ns == null) { //No such namespace already
                    throw new NoSuchNamespaceException("No such namespace:" + name + " in spec index:" + index);
                }
                //TODO another attribute to update?
            }
            else if (kind == CoreKind.COMPONENT) {
                ComponentSpec componentSpec = (ComponentSpec)spec;
                String namespace = componentSpec.getNamespace();
                Namespace ns = namespaceFactory.getNamespace(namespace, true);
                String name = componentSpec.getName();
                SpecFactory<ComponentSpec> specFactory = ns.getSpecFactory(ComponentSpec.class);
                ModelSpec oldSpec = specFactory.get(name);
                if (oldSpec == null) {
                    throw new IOException("Spec " + namespace + ":" + name + " doesn't exist.");
                }

                //TODO apply new beanSpec
                Component component = ns.getComponentFactory().get(name);
                if (component != null) {

                }
            }
//            else if (kind == CoreKind.BEAN) {
//                BeanSpec beanSpec = (BeanSpec)spec;
//                String namespace = beanSpec.getNamespace();
//                Namespace ns = namespaceFactory.getNamespace(namespace, true);
//                String name = beanSpec.getName();
//                SpecFactory<ComponentSpec> specFactory = ns.getSpecFactory(ComponentSpec.class);
//                ModelSpec oldSpec = specFactory.get(name);
//                if (oldSpec == null) {
//                    throw new IOException("Spec " + namespace + ":" + name + " doesn't exist.");
//                }
//
//                //TODO apply new beanSpec
//                Component component = ns.getComponentFactory().get(name);
//                if (component != null) {
//
//                }
//            }
            else if (kind == CoreKind.CONFIG) {
                ConfigSpec configSpec = (ConfigSpec)spec;
                String namespace = configSpec.getNamespace();
                Namespace ns = namespaceFactory.getNamespace(namespace, true);
                String name = configSpec.getName();
                SpecFactory<ConfigSpec> specFactory = ns.getSpecFactory(ConfigSpec.class);
                ModelSpec oldSpec = specFactory.get(name);
                if (oldSpec == null) {
                    throw new IOException("Spec " + namespace + ":" + name + " doesn't exist.");
                }

                //TODO apply new configSpec
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
            String version = MapSpec.getApiVersion(map);
            KindSpec kindSpec = kindSpecFactory.get(version);
            if (kindSpec == null) {
                throw new IOException("Version:" + version + " doesn't support, please make sure " +
                        "your KindSpec implementation has been put in your jar " +
                        "file /META-INF/spec/implements.properties");
            }
            MapSpec spec = kindSpec.toSpec(map);
            Kind kind = spec.getKind();
            if (kind == CoreKind.NAMESPACE) {
                NamespaceSpec namespaceSpec = (NamespaceSpec)spec;
                String name = namespaceSpec.getName();
                if (StringUtil.isInvalid(name)) {
                    throw new IOException("No name in kind 'namespace', spec index:" + index);
                }
                Namespace ns = namespaceFactory.get(name);
                if (ns == null) { //No such namespace already
                    return;
                }
                namespaceFactory.delete(name);
            }
            else if (kind == CoreKind.COMPONENT) {
                ComponentSpec compSpec = (ComponentSpec)spec;
                String namespace = compSpec.getNamespace();
                Namespace ns = namespaceFactory.get(namespace);
                if (ns == null) {
                    throw new NoSuchNamespaceException("No such namespace:" + namespace + " in spec index:" + index);
                }
                String name = compSpec.getName();

                ns.getBeanFactory().delete(name);
                ns.getSpecFactory(ComponentSpec.class).delete(name);
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
