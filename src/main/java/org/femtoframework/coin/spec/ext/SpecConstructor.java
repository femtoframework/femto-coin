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

import org.femtoframework.coin.CoinUtil;
import org.femtoframework.coin.spec.CoreKind;
import org.femtoframework.coin.spec.KindSpec;
import org.femtoframework.coin.spec.KindSpecFactory;
import org.femtoframework.coin.spec.element.*;
import org.yaml.snakeyaml.constructor.Construct;
import org.yaml.snakeyaml.constructor.ConstructorException;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.Tag;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.femtoframework.coin.spec.CoreKind.MAP;
import static org.femtoframework.coin.spec.CoreKind.NULL;

/**
 * Spec Constructor
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public class SpecConstructor extends SafeConstructor {


    private static Tag[] tags = new Tag[] {
            Tag.NULL, Tag.STR, Tag.INT, null,  Tag.FLOAT, Tag.BOOL, Tag.BINARY, Tag.TIMESTAMP, Tag.MAP, Tag.SEQ, Tag.SET, null, null, null
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

        this.yamlConstructors.put(null, new ConstructWrapper(undefinedConstructor, NULL));
        this.yamlConstructors.put(Tag.OMAP, new ConstructWrapper(yamlConstructors.get(Tag.OMAP), MAP));
        this.yamlConstructors.put(Tag.PAIRS, new ConstructWrapper(yamlConstructors.get(Tag.PAIRS), MAP));

//        this.yamlClassConstructors.put(NodeId.scalar, undefinedConstructor);
//        this.yamlClassConstructors.put(NodeId.sequence, undefinedConstructor);
//        this.yamlClassConstructors.put(NodeId.mapping, undefinedConstructor);
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
                        return new VariableElement(CoreKind.VAR, var, v);
                    }
                    else {
                        return new PrimitiveElement(CoreKind.STRING, value);
                    }
                case INT:
                case LONG:
                    if (value instanceof Long) {
                        return new PrimitiveElement(CoreKind.LONG, value);
                    } else {
                        return new PrimitiveElement(CoreKind.INT, value);
                    }
                case DOUBLE:
                    return new PrimitiveElement(CoreKind.DOUBLE, value);
                case BOOLEAN:
                    return new PrimitiveElement(CoreKind.BOOLEAN, value);
                case BYTES:
                    return new PrimitiveElement(CoreKind.BYTES, value);
                case TIMESTAMP:
                    return new PrimitiveElement(CoreKind.TIMESTAMP, value);
                case NULL: // should not get this but...
                    return new PrimitiveElement(NULL, value);
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
