package org.femtoframework.coin.codec;

import java.io.IOException;

/**
 * Abstract Decoder
 *
 * @param <T> Object
 * @param <I> Input
 */
public interface Decoder<T, I> {
    /**
     * Decode object from given input
     *
     * @param input Input, it could be InputStream, DataInput, Reader etc.
     * @return Decoded Object
     * @throws IOException
     */
    default T decode(I input) throws IOException {
        return decode(input, null);
    }

    /**
     * Decode object from given input
     *
     * @param input Input, it could be InputStream, DataInput, Reader etc.
     * @param expectedClass Expected Class
     * @return Decoded Object
     * @throws IOException
     */
    T decode(I input, Class<?> expectedClass) throws IOException;
}
