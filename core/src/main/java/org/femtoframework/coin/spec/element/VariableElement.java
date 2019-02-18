package org.femtoframework.coin.spec.element;

import org.femtoframework.coin.Component;
import org.femtoframework.bean.annotation.Ignore;
import org.femtoframework.coin.spec.CoreKind;
import org.femtoframework.coin.spec.VariableResolverFactory;
import org.femtoframework.coin.spec.VariableSpec;

/**
 * Variable Element
 *
 * @author fengyun
 * @version 1.00 2011-04-28 13:18
 */
public class VariableElement extends PrimitiveElement<Object> implements VariableSpec
{
    @Ignore
    private String prefix;
    @Ignore
    private String suffix;
    @Ignore
    private String name;

    public VariableElement(CoreKind type, String name, String orig) {
        super(type, orig);
        this.name = name;
        int index = name.indexOf(':');
        if (index > 0) {
            prefix = name.substring(0, index);
            suffix = name.substring(index+1);
        }
        else {
            suffix = name;
        }
    }

    /**
     * Variable Name
     *
     * @return
     */
    public String getName()
    {
        return name;
    }

    /**
     * Prefix, the part in front of ":", it could be null
     *
     * @return null, if there is no ':' in the name
     */
    @Override
    public String getPrefix() {
        return prefix;
    }

    /**
     * Suffix
     */
    @Override
    public String getSuffix() {
        return suffix;
    }

    /**
     * Return the value of this element definition
     *
     * @param expectedType Expected kind
     * @param component      Component
     * @return the value
     */
    public <T> T getValue(Class<T> expectedType, Component component)
    {
        VariableResolverFactory vr = component.getModule().getVariableResolverFactory();
        return vr.resolve(this, expectedType, component);
    }
}
