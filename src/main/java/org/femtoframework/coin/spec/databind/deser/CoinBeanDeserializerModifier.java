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

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.BeanDeserializer;
import com.fasterxml.jackson.databind.deser.BeanDeserializerFactory;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.deser.std.UntypedObjectDeserializer;

/**
 * CoinBeanDeserializerModifier
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public class CoinBeanDeserializerModifier extends BeanDeserializerModifier {

    /**
     * Method called by {@link BeanDeserializerFactory} after constructing default
     * bean deserializer instance with properties collected and ordered earlier.
     * Implementations can modify or replace given deserializer and return deserializer
     * to use. Note that although initial deserializer being passed is of type
     * {@link BeanDeserializer}, modifiers may return deserializers of other element;
     * and this is why implementations must check for type before casting.
     */
    public JsonDeserializer<?> modifyDeserializer(DeserializationConfig config,
                                                  BeanDescription beanDesc, JsonDeserializer<?> deserializer) {
        if (deserializer instanceof UntypedObjectDeserializer) {
            return new UntypedSpecDeserializer((UntypedObjectDeserializer)deserializer);
        }
        return deserializer;
    }
}
