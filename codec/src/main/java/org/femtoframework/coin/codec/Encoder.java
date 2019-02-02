package org.femtoframework.coin.codec;

import java.io.IOException;

/**
 * Encoder
 *
 * Abstract interface for encoder/serializer
 */
public interface Encoder<T, O> {

    /**
     * Encode the object into given out
     *
     * @param obj Object to serialize
     * @param out Out could be OutputStream, DataOutput, Writer etc.
     * @throws IOException
     */
    void encode(T obj, O out) throws IOException;
}
