package org.femtoframework.coin.ext;

import org.femtoframework.coin.*;
import org.femtoframework.coin.spec.BeanSpec;
import org.femtoframework.parameters.Parameters;
import org.femtoframework.util.nutlet.NutletUtil;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

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
public class SimpleCoinControllerTest {

    @Test
    public void create() throws Exception {

        File file = NutletUtil.getResourceAsFile("examples.yaml");


        long start = System.currentTimeMillis();


        CoinModule coinModule = CoinUtil.newModule();
        CoinController controller = coinModule.getController();

        controller.create(file);

        Namespace ns = coinModule.getNamespaceFactory().get("test");
        assertNotNull(ns);

        BeanSpec spec = ns.getSpecFactory(BeanSpec.class).get("first");
        assertNotNull(spec.get("second"));

        Component component = ns.getComponentFactory().get(spec.getName());
        assertNotNull(component);
        assertNotNull(((FirstInterface)component.getBean()).getSecond());


        FirstInterface firstInterface = component.getBean(FirstInterface.class);
        Parameters parameters = firstInterface.getParameters();
        assertNotNull(parameters);

        //Test varirable
        assertEquals(firstInterface, parameters.get("var"));
        assertEquals(firstInterface.getSecond(), parameters.get("current"));
        assertEquals(firstInterface.getSecond().getThirds().get(0), parameters.get("ref"));
        assertEquals(firstInterface.getSecond().getProperties().getProperty("prop1"), "value1");

        System.out.println("TimeElapsed:" + (System.currentTimeMillis() - start));
    }
}