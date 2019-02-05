package org.femtoframework.coin.api.ext;

import org.femtoframework.bean.NamedBean;
import org.femtoframework.coin.*;
import org.femtoframework.coin.api.APIHandler;
import org.femtoframework.coin.api.APIRequest;
import org.femtoframework.coin.api.APIResponse;
import org.femtoframework.coin.codec.Encoder;
import org.femtoframework.coin.codec.json.JsonCodec;
import org.femtoframework.coin.codec.yaml.YamlCodec;
import org.femtoframework.coin.info.BeanInfo;
import org.femtoframework.coin.info.BeanInfoFactory;
import org.femtoframework.coin.naming.CoinName;
import org.femtoframework.coin.spec.ConfigSpec;
import org.femtoframework.coin.spec.MapSpec;
import org.femtoframework.coin.spec.SpecConstants;
import org.femtoframework.coin.spec.element.SpecParameters;
import org.femtoframework.coin.spi.CoinModuleAware;
import org.femtoframework.parameters.Parameters;
import org.femtoframework.parameters.ParametersMap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.femtoframework.coin.spec.SpecConstants.NAME;
import static org.femtoframework.coin.spec.SpecConstants._NAME;

/**
 * API Handler
 * 
 * @author Sheldon Shao
 * @version 1.0
 */
public class SimpleAPIHandler implements APIHandler, CoinModuleAware {

    private CoinModule coinModule;

    private CoinLookup coinLookup;

    /**
     * Handle API Request
     *
     * @param request API Request
     * @return
     */
    @Override
    public APIResponse handleRequest(APIRequest request) throws IOException {

        APIResponse response = new APIResponse();
        response.setCode(200);

        Parameters parameters = request.getParameters();
        int offset = parameters.getInt("offset", 0);
        int limit = parameters.getInt("limit", 100);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String output = parameters.getString("output");
        Encoder<Object, OutputStream> encoder = toEncoder(output);
        String contentType = "yaml".equalsIgnoreCase(output) || "yml".equalsIgnoreCase(output) ?
                "text/yaml; charset=UTF-8" : "application/json; charset=UTF-8";

        String method = request.getMethod();
        if (method == null) {
            method = "GET";
        }
        ResourceType type = request.getType();
        if (type == null) {
            throw new IOException("Resource type is null");
        }
        if (type == ResourceType.UNKNOWN) {
            throw new IOException("No such resource type:" + type);
        }

        NamespaceFactory namespaceFactory = coinModule.getNamespaceFactory();
        switch (method) {
            case "GET":
            case "get":
            case "Get":
                doGet(namespaceFactory, type, request, response, encoder, baos, offset, limit);
                break;
            case "PATCH":
            case "patch":
            case "Patch":
                doPatch(namespaceFactory, type, request, response, encoder, baos, offset, limit);
                break;
        }

        if (response.getCode() == 200) {
            response.setContentType(contentType);
            response.setContent(baos.toString("utf8"));
        }
        return response;
    }

    protected void doPatch(NamespaceFactory namespaceFactory, ResourceType resourceType,
                         APIRequest request, APIResponse response,
                         Encoder<Object, OutputStream> encoder, ByteArrayOutputStream baos, int offset, int limit)
            throws IOException {

        switch (resourceType) {
            case NAMESPACE:
            case COMPONENT:
            case INFO:
                response.setCode(405);
                response.setMessage("Method not allowed on this resource type " + resourceType);
                break;
            case BEAN:
            case SPEC:
            case CONFIG:
                String ns = request.getNamespace();
                Namespace namespace = namespaceFactory.get(ns);
                if (namespace == null) {
                    response.setCode(405);
                    response.setMessage("Namespace has to be specified for resource " + resourceType);
                }
                else {
                    Factory factory = namespace.getFactory(resourceType);
                    if (request.isAll()) {
                        response.setCode(405);
                        response.setMessage("Please specify resource name only for resource" + resourceType + ", when doing PATCH");
                    } else {
                        String name = request.getName();
                        Object obj = factory.get(name);
                        if (obj == null) {
                            response.setCode(404);
                            response.setMessage("Resource " + ns + ":" + name + " not found");
                        } else {
                            String[] paths = request.getPaths();
                            if (paths.length > 4) {
                                if (resourceType == ResourceType.CONFIG) {
                                    CoinName nextName = new CoinName(paths, 4);
                                    ConfigSpec configSpec = (ConfigSpec)obj;
                                    Parameters next = coinLookup.lookupConfig(configSpec, nextName);
                                    if (next == null) {
                                        response.setCode(404);
                                        response.setMessage("Resource " + ns + ":" + name + " not found");
                                    } else {
                                        //TODO
//                                        encoder.encode(next, baos);
                                    }
                                }
                                else {
                                    CoinName nextName = new CoinName(paths, 4);
                                    Component component = namespace.getComponentFactory().get(name);
                                    Component next = coinLookup.lookupComponent(component, nextName);
                                    if (next == null) {
                                        response.setCode(404);
                                        response.setMessage("Resource " + ns + ":" + name + " not found");
                                    } else {
//                                        encoder.encode(toParameters(paths[paths.length-1], next.getResource(resourceType)), baos);
                                    }
                                }
                            }
                            else {
//                                encoder.encode(toParameters(name, obj), baos);
                            }
                        }
                    }
                }
                break;
        }

    }

    protected void doGet(NamespaceFactory namespaceFactory, ResourceType resourceType,
                         APIRequest request, APIResponse response,
                         Encoder<Object, OutputStream> encoder, ByteArrayOutputStream baos, int offset, int limit)
        throws IOException {
        String ns = request.getNamespace();
        switch (resourceType) {
            case NAMESPACE:
                if (request.isAll()) {
                    write(encoder, baos, offset, limit, namespaceFactory, true);
                }
                else {
                    Namespace namespace = namespaceFactory.get(ns);
                    if (namespace == null) {
                        response.setCode(404);
                        response.setMessage("Namespace " + ns + " not found");
                    }
                    else {
                        encoder.encode(namespace, baos);
                    }
                }
                break;
            case INFO:
                if (ns == null) {
                    BeanInfoFactory beanInfoFactory = coinModule.getBeanInfoFactory();
                    if (request.isAll()) {
                        write(encoder, baos, offset, limit, beanInfoFactory, true);
                    }
                    else {
                        response.setCode(405);
                        response.setMessage("Unsupported");
                    }
                }
                else {
                    Namespace namespace = namespaceFactory.get(ns);
                    if (namespace == null) {
                        response.setCode(404);
                        response.setMessage("Namespace " + ns + " not found");
                    }
                    else {
                        ComponentFactory factory = namespace.getComponentFactory();
                        String name = request.getName();
                        Component component = factory.get(name);
                        if (component == null) {
                            response.setCode(404);
                            response.setMessage("Resource " + ns + ":" + name + " not found");
                        } else {
                            String[] paths = request.getPaths();
                            if (paths.length > 4) {
                                CoinName nextName = new CoinName(paths, 4);
                                Component next = coinLookup.lookupComponent(component, nextName);
                                if (next == null) {
                                    response.setCode(404);
                                    response.setMessage("Resource " + ns + ":" + name + " not found");
                                }
                                else {
                                    encoder.encode(toParameters(name, next.getBeanInfo()), baos);
                                }
                            }
                            else {
                                encoder.encode(toParameters(name, component.getBeanInfo()), baos);
                            }
                        }
                    }
                }
                break;
            case COMPONENT:
            case BEAN:
            case SPEC:
            case CONFIG:
                Namespace namespace = namespaceFactory.get(ns);
                if (namespace == null) {
                    if (request.isAll()) {
                        int index = 0;
                        List<Object> selected = new ArrayList<>(limit);
                        finish: for(Namespace n: namespaceFactory) {
                            Factory<?> factory = n.getFactory(resourceType);
                            for(String name: factory.getNames()) {
                                Object obj = factory.get(name);
                                if (index >= offset) {
                                    selected.add(toParameters(name, obj));
                                    if (selected.size() >= limit) {
                                        break finish;
                                    }
                                }
                                index ++;
                            }
                        }
                        write(encoder, baos, offset, limit, selected, false);
                    }
                    else {
                        response.setCode(404);
                        response.setMessage("Namespace " + ns + " not found");
                    }
                }
                else {
                    Factory factory = namespace.getFactory(resourceType);
                    if (request.isAll()) {
                        write(encoder, baos, offset, limit, factory, true);
                    } else {
                        String name = request.getName();
                        Object obj = factory.get(name);
                        if (obj == null) {
                            response.setCode(404);
                            response.setMessage("Resource " + ns + ":" + name + " not found");
                        } else {
                            String[] paths = request.getPaths();
                            if (paths.length > 4) {
                                if (resourceType == ResourceType.CONFIG) {
                                    CoinName nextName = new CoinName(paths, 4);
                                    ConfigSpec configSpec = (ConfigSpec)obj;
                                    Parameters next = coinLookup.lookupConfig(configSpec, nextName);
                                    if (next == null) {
                                        response.setCode(404);
                                        response.setMessage("Resource " + ns + ":" + name + " not found");
                                    } else {
                                        encoder.encode(next, baos);
                                    }
                                }
                                else {
                                    CoinName nextName = new CoinName(paths, 4);
                                    Component component = namespace.getComponentFactory().get(name);
                                    Component next = coinLookup.lookupComponent(component, nextName);
                                    if (next == null) {
                                        response.setCode(404);
                                        response.setMessage("Resource " + ns + ":" + name + " not found");
                                    } else {
                                        encoder.encode(toParameters(paths[paths.length-1], next.getResource(resourceType)), baos);
                                    }
                                }
                            }
                            else {
                                encoder.encode(toParameters(name, obj), baos);
                            }
                        }
                    }
                }
                break;
        }
    }

    protected Parameters toParameters(String name, Object obj) {
        if (obj instanceof MapSpec) {
            return new SpecParameters((MapSpec)obj);
        }
        else if (obj instanceof Parameters) {
            return ((Parameters)obj);
        }
        else if (obj instanceof Map) {
            return new ParametersMap((Map)obj);
        }
        else {
            Class<?> clazz = obj.getClass();
            BeanInfo beanInfo = coinModule.getBeanInfoFactory().getBeanInfo(clazz);
            Parameters parameters = beanInfo.toParameters(obj);
            if (name != null && !(parameters.containsKey(NAME) || parameters.containsKey(_NAME))) {
                parameters.put(_NAME, name);
            }
            parameters.put(SpecConstants._TYPE, clazz.getName());
            return parameters;
        }
    }

    protected void write(Encoder<Object, OutputStream> encoder, ByteArrayOutputStream out,
                         int offset, int limit, Iterable<?> iterable, boolean parameterize) throws IOException {
        List objects = StreamSupport.stream(iterable.spliterator(), false)
                .skip(offset).limit(limit).collect(Collectors.toList());

        if (parameterize) {
            List list = new ArrayList(objects.size());
            for(Object object: objects) {
                String name = object instanceof NamedBean ? ((NamedBean)object).getName() : null;
                list.add(toParameters(name, object));
            }
            encoder.encode(list, out);
        }
        else {
            encoder.encode(objects, out);
        }
    }

    private JsonCodec jsonCodec = new JsonCodec();

    private YamlCodec yamlCodec = new YamlCodec();

    protected Encoder<Object, OutputStream> toEncoder(String format) {
        if ("yaml".equalsIgnoreCase(format) || "yml".equalsIgnoreCase(format)) {
            return yamlCodec;
        }
        return jsonCodec;
    }


    @Override
    public void setCoinModule(CoinModule module) {
        this.coinModule = module;
        this.coinLookup = module.getLookup();
        yamlCodec.setCoinModule(module);
    }
}
