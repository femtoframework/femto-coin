package org.femtoframework.coin.spec.databind;

/**
 * Licensed to the FemtoFramework under one or more
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

//    private static YAMLFactory yamlFactory = new YAMLFactory();
//
//    private static ObjectMapper objectMapper = new ObjectMapper(yamlFactory);
//
//    static {
//        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//
//        SimpleModule moduleMap = new SimpleModule();
//        moduleMap.setDeserializerModifier(new CoinBeanDeserializerModifier());
//        objectMapper.registerModule(moduleMap);
//    }
//
//
//    @Test
//    public void testDes() throws Exception {
//        File file = NutletUtil.getResourceAsFile("examples.yaml");
//        YAMLParser parser = yamlFactory.createParser(file);
//        List<LinkedHashMap> list = objectMapper.readValues(parser, LinkedHashMap.class).readAll();
//        System.out.println(list);
//
//        KindSpec kindSpec = CoinUtil.getKindSpecFactory().getKindSpec();
//        BeanSpec spec = kindSpec.toSpec(list.get(1));
//
//        System.out.println(objectMapper.writeValueAsString(spec));
//
//        NamespaceSpec ns = kindSpec.toSpec(list.get(0));
//        System.out.println(objectMapper.writeValueAsString(ns));
//    }


}