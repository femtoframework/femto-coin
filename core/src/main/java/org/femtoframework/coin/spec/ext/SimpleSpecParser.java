package org.femtoframework.coin.spec.ext;

import com.jsoniter.JsonIterator;
import com.jsoniter.JsonIteratorPool;
import com.jsoniter.spi.JsonException;
import org.femtoframework.coin.json.CoinCompatibilityMode;
import org.femtoframework.coin.exception.SpecSyntaxException;
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
            try (InputStream input = uri.toURL().openStream()) {
                JsonIterator iter = JsonIteratorPool.borrowJsonIterator();
                iter.configCache = CoinCompatibilityMode.DEFAULT;
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
            throw new SpecSyntaxException("Unrecognized file type:" + path);
        }
    }
}
