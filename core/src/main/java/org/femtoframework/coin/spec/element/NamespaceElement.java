package org.femtoframework.coin.spec.element;

import org.femtoframework.coin.spec.CoreKind;
import org.femtoframework.coin.NamespaceAccess;
import org.femtoframework.coin.spec.NamespaceSpec;
import org.femtoframework.coin.spec.SpecConstants;
import org.femtoframework.util.DataUtil;

import java.util.Map;

/**
 * Namespace Element
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public class NamespaceElement extends ModelElement implements NamespaceSpec {

    public NamespaceElement() {
        super(CoreKind.NAMESPACE);
    }

    public NamespaceElement(String name) {
        super(CoreKind.NAMESPACE);
        put(_NAME, new PrimitiveElement<>(CoreKind.STRING, name));
    }

    public NamespaceElement(Map map) {
        super(map);
        setKind(CoreKind.NAMESPACE);
    }

    /**
     * Access
     *
     * @return Accessible from different place
     */
    @Override
    public NamespaceAccess getAccess() {
        Object value = getValue(SpecConstants._ACCESS);
        return DataUtil.getEnum(NamespaceAccess.class, value, NamespaceAccess.LOCAL);
    }
}
