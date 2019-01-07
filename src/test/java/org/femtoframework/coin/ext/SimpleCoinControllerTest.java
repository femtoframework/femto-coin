package org.femtoframework.coin.ext;

import org.femtoframework.coin.*;
import org.femtoframework.coin.spec.BeanSpec;
import org.femtoframework.coin.spec.ext.SimpleKindSpecFactory;
import org.femtoframework.util.DataBindUtil;
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
        ConfiguratorFactory configuratorFactory = new SimpleConfiguratorFactory();

        LifecycleStrategy lifecycleStrategy = new SimpleLifecycleStrategy(configuratorFactory);

        SimpleNamespaceFactory namespaceFactory = new SimpleNamespaceFactory(lifecycleStrategy);

        SimpleCoinController controller = new SimpleCoinController();

        SimpleKindSpecFactory kindSpecFactory = new SimpleKindSpecFactory();
        kindSpecFactory.initialize();

        controller.setKindSpecFactory(kindSpecFactory);
        controller.setNamespaceFactory(namespaceFactory);

        File file = NutletUtil.getResourceAsFile("examples.yaml");

        controller.create(file);


        Namespace ns = namespaceFactory.get("test");
        assertNotNull(ns);

        BeanSpec spec = ns.getBeanSpecFactory().get("first");
        assertNotNull(spec.get("second"));

        System.out.println(DataBindUtil.writeValueAsString(spec));

        Component component = ns.getComponentFactory().create(spec.getName(), spec, BeanStage.INITIALIZE);
        assertNotNull(component);
        assertNotNull(((FirstInterface)component.getBean()).getSecond());
    }
}