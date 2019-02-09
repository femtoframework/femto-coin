package org.femtoframework.coin.spec.ext;

import org.femtoframework.coin.CoinUtil;
import org.femtoframework.coin.spec.CoreKind;
import org.femtoframework.coin.spec.KindSpec;
import org.femtoframework.coin.spec.KindSpecFactory;
import org.femtoframework.coin.spec.element.*;
import org.yaml.snakeyaml.constructor.Construct;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.Tag;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.femtoframework.coin.spec.CoreKind.*;

/**
 * Spec Constructor
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public class SpecConstructor extends SafeConstructor {


    private static Tag[] tags = new Tag[] {
            Tag.NULL, Tag.STR, Tag.INT, null,  Tag.FLOAT, Tag.BOOL, Tag.BINARY, Tag.TIMESTAMP, Tag.MAP, Tag.SEQ, Tag.SET, null, null, null, null
    };


    public SpecConstructor() {
        super();

        init();
    }

    private Construct strConstruct;

    private void init() {

        strConstruct = this.yamlConstructors.get(Tag.STR);

        CoreKind[] values = CoreKind.values();
        for(int i = 0; i < values.length; i ++) {
            if (tags[i] != null) {
                Construct construct = yamlConstructors.get(tags[i]);
                if (construct != null) {
                    yamlConstructors.put(tags[i], new ConstructWrapper(construct, values[i]));
                }
            }
        }

        this.yamlConstructors.put(null, new ConstructWrapper(SafeConstructor.undefinedConstructor, NULL));
        this.yamlConstructors.put(Tag.OMAP, new ConstructWrapper(yamlConstructors.get(Tag.OMAP), MAP));
        this.yamlConstructors.put(Tag.PAIRS, new ConstructWrapper(yamlConstructors.get(Tag.PAIRS), MAP));
    }

    /**
     * Construct object from the specified Node. Return existing instance if the
     * node is already constructed.
     *
     * @param node Node to be constructed
     * @return Java instance
     */
    protected Object constructString(Node node) {
        Object data = strConstruct.construct(node);
        finalizeConstruction(node, data);
        if (node.isTwoStepsConstruction()) {
            strConstruct.construct2ndStep(node, data);
        }
        return data;
    }


    protected void constructMapping2ndStep(MappingNode node, Map<Object, Object> mapping) {
        List<NodeTuple> nodeValue = node.getValue();
        for (NodeTuple tuple : nodeValue) {
            Node keyNode = tuple.getKeyNode();
            Node valueNode = tuple.getValueNode();
            Object key = constructString(keyNode);
            Object value = constructObject(valueNode);
            mapping.put(key, value);
        }
    }

    public static class ConstructWrapper implements Construct {

        private Construct construct;

        private CoreKind kind;

        public ConstructWrapper(Construct construct, CoreKind kind) {
            this.construct = construct;
            this.kind = kind;
        }

        /**
         * Construct a Java instance with all the properties injected when it is
         * possible.
         *
         * @param node composed Node
         * @return a complete Java instance
         */
        @Override
        public Object construct(Node node) {
            Object value = construct.construct(node);
            switch (kind) {
                case STRING:
                    String v = (String)value;
                    String trimmedV = v.trim();
                    if (trimmedV.startsWith("${") && trimmedV.endsWith("}")) {
                        String var = trimmedV.substring(2, trimmedV.length()-1);
                        return new VariableElement(VAR, var, v);
                    }
                    else {
                        return new PrimitiveElement<>(STRING, v);
                    }
                case INT:
                case LONG:
                    if (value instanceof Long) {
                        return new PrimitiveElement<>(LONG, (Long)value);
                    } else {
                        return new PrimitiveElement<>(INT, value);
                    }
                case DOUBLE:
                    return new PrimitiveElement<>(DOUBLE, value);
                case BOOLEAN:
                    return new PrimitiveElement<>(BOOLEAN, value);
                case BYTES:
                    return new PrimitiveElement<>(BYTES, value);
                case TIMESTAMP:
                    return new PrimitiveElement<>(TIMESTAMP, value);
                case NULL: // should not get this but...
                    return new PrimitiveElement<>(NULL, value);
                case SET:
                    return new SetElement<>((Set)value);
                case MAP:
                    Map map = (Map)value;
                    String version = ModelElement.getVersion(map);
                    KindSpecFactory factory = CoinUtil.getKindSpecFactory();
                    KindSpec kindSpec = factory.get(version);
                    if (kindSpec == null) {
                        throw new IllegalStateException("Version:" + version + " doesn't support, please make sure " +
                                "your KindSpec implementation has been put in your jar " +
                                "file /META-INF/spec/implements.properties");
                    }
                    return kindSpec.toSpec(map);
                case LIST:
                    return new ListElement<>((List)value);
                    default:
                        throw new IllegalStateException("Unsupported:" + kind);
            }
        }

        /**
         * Apply the second step when constructing recursive structures. Because the
         * instance is already created it can assign a reference to itself.
         *
         * @param node   composed Node
         * @param object the instance constructed earlier by
         */
        @Override
        public void construct2ndStep(Node node, Object object) {
            construct.construct2ndStep(node, object);
        }
    }
}
