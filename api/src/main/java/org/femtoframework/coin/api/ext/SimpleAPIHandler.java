package org.femtoframework.coin.api.ext;

import org.femtoframework.coin.*;
import org.femtoframework.coin.api.APIHandler;
import org.femtoframework.coin.api.APIRequest;
import org.femtoframework.coin.api.APIResponse;
import org.femtoframework.coin.codec.Encoder;
import org.femtoframework.coin.codec.json.JsonCodec;
import org.femtoframework.coin.codec.yaml.YamlCodec;
import org.femtoframework.coin.info.BeanInfo;
import org.femtoframework.coin.spec.SpecConstants;
import org.femtoframework.coin.spi.CoinModuleAware;
import org.femtoframework.parameters.Parameters;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.femtoframework.coin.CoinConstants.*;

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
        Encoder<Object, OutputStream> encoder = toEncoder(parameters.getString("output"));
        String resourceType = request.getType();
        if (resourceType == null) {
            throw new IOException("Resource type is null");
        }
        NamespaceFactory namespaceFactory = coinModule.getNamespaceFactory();
        switch (resourceType) {
            case NAMESPACE_NAMESPACE:
                if (request.isAll()) {
                    write(encoder, baos, offset, limit, namespaceFactory);
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
                boolean convert = RESOURCE_BEAN.equals(resourceType);
                if (namespace == null) {
                    if (request.isAll()) {
                        int index = 0;
                        List<Object> selected = new ArrayList<>(limit);
                        finish: for(Namespace n: namespaceFactory) {
                            Factory<?> factory = n.getFactory(resourceType);
                            for(String name: factory.getNames()) {
                                Object obj = factory.get(name);
                                if (index >= offset) {
                                    selected.add(convert ? toParameters(name, obj) : obj);
                                    if (selected.size() >= limit) {
                                        break finish;
                                    }
                                }
                                index ++;
                            }
                        }
                        write(encoder, baos, offset, limit, selected);
                    }
                    else {
                        response.setCode(404);
                        response.setMessage("Namespace " + ns + " not found");
                    }
                }
                else {
                    Factory factory = namespace.getFactory(resourceType);
                    if (request.isAll()) {
                        write(encoder, baos, offset, limit, factory);
                    } else {
                        String name = request.getName();
                        Object obj = factory.get(name);
                        if (obj == null) {
                            response.setCode(404);
                            response.setMessage("Resource " + ns + ":" + name + " not found");
                        } else {
                            encoder.encode(convert ? toParameters(name, obj) : obj, baos);
                        }
                    }
                }
                break;
        }

        if (response.getCode() == 200) {
            response.setContent(baos.toString("utf8"));
        }
        return response;
    }

    protected Parameters toParameters(String name, Object obj) {
        Class<?> clazz = obj.getClass();
        BeanInfo beanInfo = coinModule.getBeanInfoFactory().getBeanInfo(clazz);
        Parameters parameters = beanInfo.toParameters(obj);
        parameters.put(SpecConstants._NAME, name);
        parameters.put(SpecConstants._TYPE, clazz.getName());
        return parameters;
    }

    protected void write(Encoder<Object, OutputStream> encoder, ByteArrayOutputStream out,
                         int offset, int limit, Iterable<?> iterable) throws IOException {
        List objects = StreamSupport.stream(iterable.spliterator(), false)
                .skip(offset).limit(limit).collect(Collectors.toList());
        encoder.encode(objects, out);
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
    }
}
