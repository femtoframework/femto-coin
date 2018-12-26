package org.femtoframework.coin.spec.databind;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLParser;
import org.femtoframework.coin.spec.BeanSpec;
import org.femtoframework.coin.spec.databind.deser.CoinBeanDeserializerModifier;
import org.femtoframework.coin.spec.element.BeanElement;
import org.femtoframework.util.DataBindUtil;
import org.femtoframework.util.nutlet.NutletUtil;
import org.junit.Test;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

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
public class MapSpecDeserializerTest {

    //Object Mapper is thread safe
    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        SimpleModule moduleMap = new SimpleModule();
        moduleMap.setDeserializerModifier(new CoinBeanDeserializerModifier());
        objectMapper.registerModule(moduleMap);
    }


    private static YAMLFactory yamlFactory = new YAMLFactory();

    @Test
    public void testDes() throws Exception {
        File file = NutletUtil.getResourceAsFile("examples.yaml");
        YAMLParser parser = yamlFactory.createParser(file);
        Map obj = objectMapper.readValue(parser, LinkedHashMap.class);
        System.out.println(obj);

        BeanSpec spec = new BeanElement(obj);
        System.out.println(DataBindUtil.writeValueAsString(spec));
    }
}