package org.femtoframework.coin;

import org.femtoframework.parameters.Parameters;
import org.femtoframework.parameters.ParametersMap;
import org.femtoframework.util.DataBindUtil;
import org.femtoframework.util.nutlet.NutletUtil;
import org.junit.Test;
import org.junit.runners.Parameterized;

import java.io.File;
import java.net.URL;

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
public class FirstBeanTest {

    @Test
    public void getSecond() throws Exception {

        File file = NutletUtil.getResourceAsFile("examples.yaml");
        Parameters parameters = DataBindUtil.readValueFromYaml(file, ParametersMap.class);
        assertNotNull(parameters.getParameters("second"));
        assertEquals("First", parameters.getString("_name"));
    }
}