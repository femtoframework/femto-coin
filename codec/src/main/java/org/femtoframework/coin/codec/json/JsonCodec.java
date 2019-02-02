package org.femtoframework.coin.codec.json;

import com.jsoniter.JsonIterator;
import com.jsoniter.output.JsonStream;
import org.femtoframework.coin.codec.Decoder;
import org.femtoframework.coin.codec.Encoder;
import org.femtoframework.parameters.ParametersMap;

import java.io.*;

/**
 * Json Codec
 *
 * @author Sheldon Shao
 * @version 1.0.0
 */
public class JsonCodec implements Encoder<Object, OutputStream>, Decoder<Object, String> {

    @Override
    public void encode(Object obj, OutputStream out) throws IOException {
        JsonStream.serialize(CoinCompatibilityMode.DEFAULT, obj, out);
    }

    /**
     * Decode object from given input
     *
     * @param input Input, it could be InputStream, DataInput, Reader etc.
     * @param expectedClass Expected Class
     * @return Decoded Object
     * @throws IOException
     */
    @Override
    public Object decode(String input, Class<?> expectedClass) throws IOException {
        if (expectedClass == null) {
            expectedClass = ParametersMap.class;
        }
        return JsonIterator.deserialize(CoinCompatibilityMode.DEFAULT, input, expectedClass);
    }
}
