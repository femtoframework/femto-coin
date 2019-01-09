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
package org.femtoframework.coin.spec.ext;

import com.jsoniter.JsonIterator;
import com.jsoniter.JsonIteratorPool;
import com.jsoniter.spi.JsonException;
import org.femtoframework.coin.spec.SpecParser;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Spec Parser
 *
 * The default implementation use the following libraries:
 * 1. snakeyaml for yaml files
 * 2. jsoniter for json files
 *
 * Since jackson takes around 500ms to parse examples.yaml.
 * I consider other tiny libraries.
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public class SimpleSpecParser implements SpecParser {
    /**
     * Parse Spec
     *
     * @param uri Parse Spec
     * @return
     * @throws IOException
     */
    @Override
    public List<LinkedHashMap> parseSpec(URI uri) throws IOException {
        String path = uri.getPath();
        String lowerCase = path.toLowerCase();
        if (lowerCase.endsWith(".yaml") || lowerCase.endsWith(".yml")) { //Yaml
            Yaml yaml = new Yaml(new SpecConstructor());
            try (InputStream input = uri.toURL().openStream()) {
                Iterable<Object> iterable = yaml.loadAll(input);
                ArrayList<LinkedHashMap> list = new ArrayList<>();
                for (Object item :iterable) {
                    list.add((LinkedHashMap)item);
                }
                return list;
            }
        }
        else if (lowerCase.endsWith(".json")) {
            //TODO
            try (InputStream input = uri.toURL().openStream()) {
                JsonIterator iter = JsonIteratorPool.borrowJsonIterator();
                iter.reset(input);
                try {
                    return iter.read(ArrayList.class);
                } catch (ArrayIndexOutOfBoundsException e) {
                    throw iter.reportError("deserialize", "premature end");
                } catch (IOException e) {
                    throw new JsonException(e);
                } finally {
                    JsonIteratorPool.returnJsonIterator(iter);
                }
            }
        }
        else {
            throw new IllegalArgumentException("Unrecognized file type:" + path);
        }
    }
}
