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
package org.femtoframework.coin.launcher;

import org.femtoframework.coin.CoinController;
import org.femtoframework.coin.CoinModule;
import org.femtoframework.coin.CoinUtil;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Coin Launcher
 *
 * Flow,
 *
 * 1. Get all META-INF/spec/component.yaml
 * 2. Get META-INF/spec/application.yaml
 * 3. Get all yaml files in args
 * 4. Call CoinController.create
 *
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public class Launcher {

    /**
     * Main Method
     *
     * @param args Yaml files
     * @throws IOException
     */
    public static void main(String... args) throws IOException {
        CoinModule module = CoinUtil.getModule();
        CoinController controller = module.getController();
        List<URI> componentYamls = controller.getComponentYamls(null);
        URI applicationYaml = controller.getApplicationYaml(null);

        List<URI> allUris = new ArrayList<>(componentYamls.size() + 1 + args.length);
        allUris.addAll(componentYamls);
        allUris.add(applicationYaml);
        for(int i = 0; i < args.length; i ++) {
            allUris.add(new File(args[i]).toURI());
        }

        if (allUris.isEmpty()) {
            System.out.println("WARNING: no any yaml files, launcher exit");
            System.exit(0);
        }

        controller.create(allUris.toArray(new URI[allUris.size()]));
    }
}
