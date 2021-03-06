package org.femtoframework.coin.ext;

import org.femtoframework.bean.BeanStage;
import org.femtoframework.coin.*;
import org.femtoframework.bean.annotation.Ignore;
import org.femtoframework.coin.configurator.ConfigInjection;
import org.femtoframework.coin.exception.NoSuchNamespaceException;
import org.femtoframework.coin.spec.*;
import org.femtoframework.implement.ImplementUtil;
import org.femtoframework.parameters.Parameters;
import org.femtoframework.util.StringUtil;
import org.femtoframework.util.SystemUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

import static org.femtoframework.coin.CoinConstants.*;


/**
 * Coin Controller
 * 
 * @author Sheldon Shao
 * @version 1.0
 */
public class SimpleCoinController implements CoinController, CoinReloader {

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
     * URIs
     */
    private Set<URI> uris = new HashSet<>();

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
     * @return "META-INF/spec/application.yaml" and "META-INF/spec/{ENV}/application.yaml" in classpaths {ENV} is the current environment
     * @throws IOException
     */
    public List<URI> getApplicationYamls(ClassLoader classLoader) throws IOException {
        if (classLoader == null) {
            classLoader = Thread.currentThread().getContextClassLoader();
        }
        List<URI> uris = new ArrayList<>(2);
        try {
            URI uri = toURI(classLoader.getResource(APPLICATION_YAML));
            if (uri != null) {
                uris.add(uri);
            }

            String currentEnv = "META-INF/spec/"
                    + SystemUtil.getEnvironment().name().toLowerCase() + "/application.yaml";
            uri = toURI(classLoader.getResource(currentEnv));
            if (uri != null) {
                uris.add(uri);
            }
            return uris;
        } catch (URISyntaxException e) {
            throw new IOException("URI syntax error", e);
        }
    }

    /**
     * All the URIs which passed through #create
     *
     * @return URIs
     */
    @Override
    public Set<java.net.URI> getUris() {
        return uris;
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

        this.uris.addAll(Arrays.asList(uris));
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
                    namespaceFactory.apply(ns, ((NamespaceSpec)spec));
                }
                else {
                    namespaceFactory.createNamespace(name, ((NamespaceSpec) spec));
                }
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
        LinkedHashMap<ConfigSpec, Namespace> newConfigSpecs = new LinkedHashMap<>();

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

                namespaceFactory.apply(ns, namespaceSpec);
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

                Component component = ns.getComponentFactory().get(name);
                if (component != null) {
                    ns.getComponentFactory().apply(component, componentSpec);
                }
            }
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

                ns.getConfigSpecFactory().add(configSpec);
                newConfigSpecs.put(configSpec, ns);
            }
            else {
                throw new IOException("Unsupported kind yet:" + kind);
            }
        }

        if (!newConfigSpecs.isEmpty()) {
            //Apply this to all components in same namespace
            ConfigInjection injection = new ConfigInjection();
            for(Map.Entry<ConfigSpec, Namespace> entry: newConfigSpecs.entrySet()) {
                Parameters<Object> parameters = entry.getKey().getParameters();

                for(Component component : entry.getValue().getComponentFactory()) {
                    Parameters<Object> configForThisBean = parameters.getParameters(component.getName());
                    if (configForThisBean != null && !configForThisBean.isEmpty()) {
                        injection.inject(component, configForThisBean);
                    }
                }
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

    /**
     * Reload specs
     *
     * @param force Reload anyway if it is true, otherwise follow the strategies
     */
    @Override
    public void reload(boolean force) {

    }
}
