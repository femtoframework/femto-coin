package org.femtoframework.coin;


import org.femtoframework.util.nutlet.NutletUtil;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;

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
public class FirstBeanTest {

    @Test
    public void getSecond() throws Exception {
        long start = System.currentTimeMillis();
        File file = NutletUtil.getResourceAsFile("examples.yaml");
//        ParametersMap parameters = JsonIterator.readValueFromYaml(file, ParametersMap.class);
        System.out.println("TimeElapsed:" + (System.currentTimeMillis() - start));

//        assertNotNull(parameters.get("second"));
//        assertEquals("First", parameters.get("_name"));
    }

    @Test
    public void getNamespace() throws Exception {
//        long start = System.currentTimeMillis();
        File file = NutletUtil.getResourceAsFile("examples.yaml");
//        ParametersMap parameters = DataBindUtil.readValueFromYaml(file, ParametersMap.class);

        long start = System.currentTimeMillis();
        Yaml yaml = new Yaml();
        InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
        Iterable<Object> objs = yaml.loadAll(inputStream);
        Iterator<Object> it = objs.iterator();
        System.out.println(it.next());
        System.out.println(it.next());
        System.out.println("TimeElapsed:" + (System.currentTimeMillis() - start));
    }
}