package org.femtoframework.coin.codec.yaml;

import org.femtoframework.coin.CoinModule;
import org.femtoframework.coin.codec.Decoder;
import org.femtoframework.coin.codec.Encoder;
import org.femtoframework.coin.spi.CoinModuleAware;
import org.yaml.snakeyaml.Yaml;

import java.io.*;

public class YamlCodec implements Encoder<Object, OutputStream>, Decoder<Object, String>, CoinModuleAware {
    @Override
    public Object decode(String input, Class<?> expectedClass) throws IOException {
        Yaml yaml = new Yaml();
        return yaml.loadAs(input, expectedClass);
    }

    @Override
    public void encode(Object obj, OutputStream out) throws IOException {
        Yaml yaml = new Yaml(new CoinRepresenter(coinModule));
        Writer writer = new BufferedWriter(new OutputStreamWriter(out, "utf8"));
        yaml.dump(obj, writer);
        writer.flush();
    }

    private CoinModule coinModule;

    @Override
    public void setCoinModule(CoinModule module) {
        this.coinModule = module;
    }
}
