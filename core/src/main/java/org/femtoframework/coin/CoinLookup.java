package org.femtoframework.coin;

import org.femtoframework.coin.naming.CoinName;
import org.femtoframework.coin.spec.ConfigSpec;
import org.femtoframework.parameters.Parameters;

import javax.naming.Name;
import javax.naming.NamingException;

/**
 * Coin Lookup
 * <p>
 *
 * 1. Name follows this syntax 'namespace:name', it can point to a bean
 * 2. 'namespace:name.childName' can indicate a child of a bean if the bean has a child component with name 'child_name'
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public interface CoinLookup {

    /**
     * Resolve object by Name
     *
     * @param name Name
     * @return Object
     */
    default Object lookupBean(Name name) {
        return lookup(ResourceType.BEAN, name);
    }

    /**
     * Resolve object by Name in string
     *
     * @param name Name
     * @return Object
     */
    default Object lookupBean(String name) throws NamingException {
        return lookupBean(new CoinName(name));
    }

    /**
     * Resolve resource by Name in string
     *
     * @param resourceType ResourceType
     * @param name Name
     * @return Object
     */
    default Object lookup(ResourceType resourceType, String name) throws NamingException {
        return lookup(resourceType, new CoinName(name));
    }

    /**
     * Resolve resource by Name
     *
     * @param resourceType ResourceType
     * @param name Name
     * @return Object
     */
    Object lookup(ResourceType resourceType, Name name);

    /**
     * Resolve resource by Name in specific namespace
     *
     * @param resourceType ResourceType
     * @param namespace Namespace
     * @param name Name
     * @return Object
     */
    Object lookup(ResourceType resourceType, Namespace namespace, Name name);

    /**
     * Lookup Child Component based on existing Component.
     *
     * @param component Component
     * @param name Name
     * @return Child component
     */
    Component lookupComponent(Component component, Name name);


    /**
     * Lookup Child Config based on existing ConfigSpec.
     *
     * @param configSpec ConfigSpec
     * @param name Name
     * @return Child ConfigSpec
     */
    Parameters lookupConfig(ConfigSpec configSpec, Name name);
}
