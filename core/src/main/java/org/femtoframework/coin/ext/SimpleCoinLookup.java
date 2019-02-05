package org.femtoframework.coin.ext;

import org.femtoframework.coin.*;
import org.femtoframework.coin.naming.CoinNamingParser;
import org.femtoframework.coin.spec.ConfigSpec;
import org.femtoframework.parameters.Parameters;
import org.femtoframework.text.NamingConvention;

import javax.naming.Name;
import javax.naming.NamingException;

/**
 * Simple Coin Lookup
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public class SimpleCoinLookup implements CoinLookup {


    private NamespaceFactory namespaceFactory;

    private CoinNamingParser namingParser = new CoinNamingParser();


    public SimpleCoinLookup(NamespaceFactory namespaceFactory) {
        this.namespaceFactory = namespaceFactory;
    }

    /**
     * Resolve object by Name in string
     *
     * @param name Name
     * @return Object
     */
    public Object lookup(ResourceType resourceType, String name) throws NamingException {
        if (resourceType == ResourceType.CONFIG) {
            return CoinLookup.super.lookup(resourceType, name);
        }

        int index = name.indexOf(CoinConstants.CHAR_DELIM);
        if (index == -1) {
            //Simple Name
            index = name.indexOf(CoinConstants.CHAR_COLON);
            if (index == -1) {
                if (resourceType == ResourceType.NAMESPACE) {
                    return namespaceFactory.get(name);
                }
                else {
                    throw new IllegalArgumentException("No such resource " + name + " for type:" + resourceType);
                }
            }
            String namespace = name.substring(0, index);
            Namespace ns = namespaceFactory.get(namespace);
            if (ns == null) {
                throw new NamingException("No such namespace:" + namespace + " in name:" + name);
            }
            String on = name.substring(index+1);
            Component component = ns.getComponentFactory().get(on);
            if (component == null) {
                throw new IllegalArgumentException("No such component:" + on + " in namespace:" + namespace);
            }
            return component.getResource(resourceType);
        }
        else {
            return lookup(resourceType, namingParser.parse(name));
        }
    }

    @Override
    public Object lookup(ResourceType resourceType, Name name) {
        int size = name.size();
        if (size == 0) {
            return null;
        }
        else {
            String n = name.get(0);
            Namespace ns = namespaceFactory.get(n);
            if (ns == null) {
                throw new IllegalArgumentException("No such namespace:" + n);
            }
            if (size == 1) {
                if (resourceType == ResourceType.NAMESPACE) {
                    return ns;
                }
                else {
                    throw new IllegalArgumentException("No such resource " + name + " for type:" + resourceType);
                }
            }
            else {
                String on = name.get(1);
                if (resourceType == ResourceType.CONFIG) {
                    ConfigSpec configSpec = ns.getConfigSpecFactory().get(on);
                    if (configSpec == null) {
                        throw new IllegalArgumentException("No such config spec:" + on + " in namespace:" + n);
                    }
                    return doLookupConfig(configSpec, name, 2, size);
                }
                else {
                    Component component = ns.getComponentFactory().get(on);
                    if (component == null) {
                        throw new IllegalArgumentException("No such component:" + on + " in namespace:" + n);
                    }
                    Component next = doLookupComponent(component, name, 2, size);
                    return next.getResource(resourceType);
                }
            }
        }
    }

    @Override
    public Component lookupComponent(Component component, Name name) {
        return doLookupComponent(component,name, 0, name.size());
    }

    /**
     * Lookup Child Config based on existing ConfigSpec.
     *
     * @param configSpec ConfigSpec
     * @param name Name
     * @return Child ConfigSpec
     */
    public Parameters lookupConfig(ConfigSpec configSpec, Name name) {
        return doLookupConfig(configSpec, name, 0, name.size());
    }

    /**
     * Lookup Child Component based on existing Component.
     *
     * @param component Component
     * @param name Name
     * @return Child component
     */
    protected Component doLookupComponent(Component component, Name name, int start, int size) {
        int i = start;
        Component next = component;
        while(size > i) {
            String cn = name.get(i);
            next = next.getChild(NamingConvention.parse(cn, false));
            if (next == null) {
                throw new IllegalArgumentException("No such component:" + cn + " in component:" + next);
            }
            i ++;
        }
        return next;
    }

    /**
     * Lookup Config.
     *
     * @param configSpec Component
     * @param name Name
     * @return Child component
     */
    protected Parameters doLookupConfig(ConfigSpec configSpec, Name name, int start, int size) {
        int i = start;
        Parameters next = configSpec.getParameters();
        while(size > i) {
            String cn = name.get(i);
            next = next.getParameters(NamingConvention.parse(cn, false));
            if (next == null) {
                throw new IllegalArgumentException("No such config:" + cn + " in component:" + next);
            }
            i ++;
        }
        return next;
    }
}
