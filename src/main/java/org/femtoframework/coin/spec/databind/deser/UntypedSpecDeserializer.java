/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.femtoframework.coin.spec.databind.deser;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonTokenId;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.UntypedObjectDeserializer;
import org.femtoframework.coin.spec.ElementType;
import org.femtoframework.coin.spec.types.ListElement;
import org.femtoframework.coin.spec.types.MapElement;
import org.femtoframework.coin.spec.types.PrimitiveElement;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Untyped Spec Deserializer
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public class UntypedSpecDeserializer extends UntypedObjectDeserializer {

    public UntypedSpecDeserializer(UntypedObjectDeserializer base)
    {
        super(base, null, null, null, null);
    }

    public UntypedSpecDeserializer(UntypedObjectDeserializer base,
                                     JsonDeserializer<?> mapDeser, JsonDeserializer<?> listDeser,
                                     JsonDeserializer<?> stringDeser, JsonDeserializer<?> numberDeser)
    {
        super(base, mapDeser, listDeser, stringDeser, numberDeser);
    }

    protected JsonDeserializer<?> _withResolved(JsonDeserializer<?> mapDeser,
                                                JsonDeserializer<?> listDeser,
                                                JsonDeserializer<?> stringDeser, JsonDeserializer<?> numberDeser) {
        return new UntypedSpecDeserializer(this,
                mapDeser, listDeser, stringDeser, numberDeser);
    }


    @Override
    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException
    {
        Object value = super.deserialize(p, ctxt);
        int tokenId = p.getCurrentTokenId();
        switch (tokenId) {
            case JsonTokenId.ID_END_OBJECT:
                return new MapElement<>((Map)value);
            case JsonTokenId.ID_END_ARRAY:
                return new ListElement<>((List)value);
            case JsonTokenId.ID_EMBEDDED_OBJECT:
                return p.getEmbeddedObject();
            case JsonTokenId.ID_STRING:
                return new PrimitiveElement(ElementType.STRING, value);
            case JsonTokenId.ID_NUMBER_INT:
                if (value instanceof Long){
                    return new PrimitiveElement(ElementType.LONG, value);
                }
                else  {
                    return new PrimitiveElement(ElementType.INT, value);
                }
            case JsonTokenId.ID_NUMBER_FLOAT:
                return new PrimitiveElement(ElementType.DOUBLE, value);
            case JsonTokenId.ID_TRUE:
            case JsonTokenId.ID_FALSE:
                return new PrimitiveElement(ElementType.BOOLEAN, value);
            case JsonTokenId.ID_NULL: // should not get this but...
                return new PrimitiveElement(ElementType.NULL, value);
        }
        return value;
    }
}
