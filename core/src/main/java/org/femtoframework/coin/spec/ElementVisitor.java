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

    /**
     * Visit Remote Spec
     *
     * @param remoteSpec VariableSpec
     */
    default void visitRemote(RemoteSpec remoteSpec) {

    }
}
