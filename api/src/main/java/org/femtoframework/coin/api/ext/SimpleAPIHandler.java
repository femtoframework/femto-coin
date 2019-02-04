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

import static org.femtoframework.coin.CoinConstants.*;
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
                "text/yaml; charset=UTF-8" :"application/json; charset=UTF-8";

        String resourceType = request.getType();
        if (resourceType == null) {
            throw new IOException("Resource type is null");
        }
        NamespaceFactory namespaceFactory = coinModule.getNamespaceFactory();
        switch (resourceType) {
            case NAMESPACE_NAMESPACE:
                if (request.isAll()) {
                    write(encoder, baos, offset, limit, namespaceFactory, true);
                }
                else {
                    String ns = request.getNamespace();
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
            case RESOURCE_COMPONENT:
            case RESOURCE_BEAN:
            case RESOURCE_SPEC:
            case RESOURCE_CONFIG:
                String ns = request.getNamespace();
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
                            encoder.encode(toParameters(name, obj), baos);
                        }
                    }
                }
                break;
        }

        if (response.getCode() == 200) {
            response.setContentType(contentType);
            response.setContent(baos.toString("utf8"));
        }
        return response;
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
        yamlCodec.setCoinModule(module);
    }
}
