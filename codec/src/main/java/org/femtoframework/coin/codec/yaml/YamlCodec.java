package org.femtoframework.coin.codec.yaml;

import org.femtoframework.coin.codec.Decoder;
import org.femtoframework.coin.codec.Encoder;

import java.io.IOException;
import java.io.OutputStream;

//TODO
public class YamlCodec implements Encoder<Object, OutputStream>, Decoder<Object, String> {
    @Override
    public Object decode(String input, Class<?> expectedClass) throws IOException {
        return null;
    }

    @Override
    public void encode(Object obj, OutputStream out) throws IOException {

    }
}
