package org.femtoframework.coin.spec;

import org.femtoframework.coin.CoinConstants;

import java.util.Collections;
import java.util.List;

/**
 * Bean
 *
 * Bean means Model with type and other common stuffs
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public interface BeanSpec extends ModelSpec {
    /**
     * Return belongsTo
     *
     * belongsTo syntax
     *
     * [NAMESPACE:]&lt;NAME&gt;#&lt;METHOD_NAME&gt;
     * 1. The method must be declared on an interface
     * 2. The method must have only one argument
     * 3. The method argument should be an interface which the current bean implements
     * 4. NAMESPACE is optional, if it doesn't specify, use the bean's namespace
     *
     * @return A list of belongsTo
     */
    default List<String> getBelongsTo() {
        return Collections.emptyList();
    }

    /**
     * Indicate the kind of this bean
     *
     * @return
     */
    String getType();

    /**
     * The real class of the kind
     *
     * @return kind class
     */
    Class<?> getTypeClass();

    /**
     * Bean Name
     *
     * @return Bean Name
     */
    String getName();


    /**
     * Is it singleton?
     * If it is true, then coin will try to create it in singleton pattern first.
     *
     * @return Singleton
     */
    default boolean isSingleton() {
        return false;
    }

    /**
     * Return namespace of current bean spec
     *
     * @return namespace
     */
    default String getNamespace() { //Default is "current" namespace
        return CoinConstants.DEFAULT_NAMESPACE;
    }

    /**
     * Qualified Name
     *
     * @return Qualified Name
     */
    default String getQualifiedName() {
        return getNamespace() + ":" + getName();
    }

    /**
     * Whether it is default for the specific interface.
     * If multiple beans set default==true, the last one will be enabled as "default=true" in the factory
     *
     * @return is it the default implementation of the interface
     */
    default boolean isDefault() {
        return true;
    }

    /**
     * Whether the bean is enabled or not.
     *
     * TODO check that in component
     * @return Is enabled or not
     */
    default boolean isEnabled() {
        return true;
    }

    /**
     * Start visiting this spec
     *
     * @param visitor Visitor
     */
    default void accept(ElementVisitor visitor) {
        visitor.visitBean(this);
    }
}
