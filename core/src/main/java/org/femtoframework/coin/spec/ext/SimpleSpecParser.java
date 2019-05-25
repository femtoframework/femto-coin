package org.femtoframework.coin.spec.ext;

import com.jsoniter.JsonIterator;
import com.jsoniter.JsonIteratorPool;
import com.jsoniter.spi.JsonException;
import org.femtoframework.coin.json.CoinCompatibilityMode;
import org.femtoframework.coin.exception.SpecSyntaxException;
import org.femtoframework.coin.spec.SpecParser;
import org.femtoframework.io.IOUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
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

    private static Logger logger = LoggerFactory.getLogger(SimpleSpecParser.class);

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
        if (path == null) {
            path = uri.toString();
        }
        if (logger.isInfoEnabled()) {
            logger.info("Parsing specs:" + uri.toString());
        }
        
        String lowerCase = path.toLowerCase();
        if (lowerCase.endsWith(".yaml") || lowerCase.endsWith(".yml")) { //Yaml
            Yaml yaml = new Yaml(new SpecConstructor());
            InputStream input = null;
            try {
                input = uri.toURL().openStream();
                Iterable<Object> iterable = yaml.loadAll(input);
                ArrayList<LinkedHashMap> list = new ArrayList<>();
                for (Object item :iterable) {
                    list.add((LinkedHashMap)item);
                }
                return list;
            }
            catch(IOException ioe) {
                logger.warn("Loading yaml exception:" + uri.toString(), ioe);
                return Collections.emptyList();
            }
            finally {
                IOUtil.close(input);
            }
        }
        else if (lowerCase.endsWith(".json")) {
            InputStream input = null;
            try {
                input = uri.toURL().openStream();
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
            catch(IOException ioe) {
                logger.warn("Loading json exception:" + uri.toString(), ioe);
                return Collections.emptyList();
            }
            finally {
                IOUtil.close(input);
            }
        }
        else {
            throw new SpecSyntaxException("Unrecognized file type:" + path);
        }
    }
}
