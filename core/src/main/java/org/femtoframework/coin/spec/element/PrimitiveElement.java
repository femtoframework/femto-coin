package org.femtoframework.coin.spec.element;

import org.femtoframework.coin.Component;
import org.femtoframework.bean.annotation.AsValue;
import org.femtoframework.bean.annotation.Coined;
import org.femtoframework.bean.annotation.Ignore;
import org.femtoframework.coin.spec.CoreKind;
import org.femtoframework.coin.spec.PrimitiveSpec;
import org.femtoframework.util.convert.ConverterUtil;

import java.lang.reflect.Type;

/**
 * PrimitiveSpec Element
 *
 * @author Sheldon Shao
 * @version 1.0
 */
@Coined
public class PrimitiveElement<E> extends AbstractElement implements PrimitiveSpec<E> {

    @Ignore
    private E primitiveValue;

    public PrimitiveElement(CoreKind type, E value) {
        setKind(type);
        this.primitiveValue = value;
        if (value != null) {
            setKindClass(value.getClass().getName());
        }
    }

    /**
     * Return the value of this element definition
     *
     * @param expectedType Expected kind
     * @param genericType
     * @param component    Component
     * @return the value
     */
    public <T> T getValue(Class<T> expectedType, Type genericType, Component component) {
        if (kind == CoreKind.NULL) {
            return null;
        }
        if (expectedType == null) {
            return (T)primitiveValue;
        }
        else {
            return ConverterUtil.convertToType(primitiveValue, expectedType);
        }
    }

    public String toString() {
        return String.valueOf(primitiveValue);
    }

    @AsValue
    public E getPrimitiveValue() {
        return primitiveValue;
    }

    public void setPrimitiveValue(E value) {
        this.primitiveValue = value;
    }
}
