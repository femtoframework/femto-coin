package org.femtoframework.coin.spec.element;

import org.femtoframework.coin.annotation.Ignore;
import org.femtoframework.coin.spec.Element;
import org.femtoframework.coin.spec.Kind;

import java.io.Serializable;

/**
 * Abstract Element
 *
 * @author fengyun
 * @version 1.00 2011-04-28 09:35
 */
public abstract class AbstractElement implements Element, Serializable
{
    @Ignore
    protected Kind kind = null;

    @Ignore
    private String kindClass = null;

    /**
     * Type of the element
     *
     * @return Type of the element
     */
    public Kind getKind()
    {
        return kind;
    }

    /**
     * Set Type of the element
     *
     * @param kind Type
     */
    public void setKind(Kind kind)
    {
        this.kind = kind;
    }

    /**
     * Convert kind(String) to Class
     *
     * @return Class
     */
    public String getKindClass()
    {
        return kindClass;
    }

    public void setKindClass(String kindClass) {
        this.kindClass = kindClass;
    }

}
