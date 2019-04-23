package org.femtoframework.coin.spec;

import org.femtoframework.bean.NamedBean;
import org.femtoframework.coin.CoinConstants;

import static org.femtoframework.coin.CoinConstants.NAME;

/**
 * Bean
 *
 * Bean means Model with type and other common stuffs
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public interface BeanSpec extends MapSpec<Element>, NamedBean {

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
    default String getName() {
        return getString(NAME, null);
    }

    /**
     * Return generated name
     *
     * @return generated name
     */
    default String getGenerateName() {
        return getName();
    }

    String getNamespace();

    /**
     * Qualified Name
     *
     * @return Qualified Name
     */
    default String getQualifiedName() {
        return getNamespace() + CoinConstants.CHAR_COLON + getGenerateName();
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
