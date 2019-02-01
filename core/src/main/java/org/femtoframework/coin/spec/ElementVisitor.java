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
package org.femtoframework.coin.spec;

/**
 * Element Visitor
 *
 * @author fengyun
 * @version 1.00 2011-04-28 08:55
 */
public interface ElementVisitor {

    /**
     * Visit Primitive Element
     *
     * @param primitiveSpec Visit Primitive Element
     */
    default void visitPrimitive(PrimitiveSpec primitiveSpec) {

    }

    /**
     * Visit Map Spec
     *
     * @param mapSpec Map Spec
     */
    default void visitMap(MapSpec mapSpec) {

    }

    /**
     * Visit List Spec
     *
     * @param listSpec List Spec
     */
    default void visitList(ListSpec listSpec) {

    }

    /**
     * Visit Set Spec
     *
     * @param setSpec Set Spec
     */
    default void visitSet(SetSpec setSpec) {

    }

    /**
     * Visit Bean Spec
     *
     * @param beanSpec BeanSpec
     */
    default void visitBean(BeanSpec beanSpec) {
        visitMap(beanSpec);
    }

    /**
     * Visit NamespaceSpec
     *
     * @param namespaceSpec NamespaceSpec
     */
    default void visitNamespace(NamespaceSpec namespaceSpec) {
        visitMap(namespaceSpec);
    }

    /**
     * Visit Variable Spec
     *
     * @param variableSpec VariableSpec
     */
    default void visitVariable(VariableSpec variableSpec) {

    }
}