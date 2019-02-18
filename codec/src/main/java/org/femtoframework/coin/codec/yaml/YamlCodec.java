package org.femtoframework.coin.codec.yaml;

import org.femtoframework.coin.codec.Decoder;
import org.femtoframework.coin.codec.Encoder;
import org.femtoframework.bean.info.BeanInfoFactory;
import org.femtoframework.coin.spi.BeanInfoFactoryAware;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class YamlCodec implements Encoder<Object, OutputStream>, Decoder<Object, String>, BeanInfoFactoryAware {
    @Override
    public Object decode(String input, Class<?> expectedClass) throws IOException {
        Yaml yaml = new Yaml();
        return yaml.loadAs(input, expectedClass);
    }

    @Override
    public void encode(Object obj, OutputStream out) throws IOException {
        Yaml yaml = new Yaml(new CoinRepresenter(beanInfoFactory));
        Writer writer = new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8));
        yaml.dump(obj, writer);
        writer.flush();
    }

    private BeanInfoFactory beanInfoFactory;

    /**
     * Set BeanInfoFactory
     *
     * @param factory BeanInfoFactory
     */
    @Override
    public void setBeanInfoFactory(BeanInfoFactory factory) {
        this.beanInfoFactory = factory;
    }
}
