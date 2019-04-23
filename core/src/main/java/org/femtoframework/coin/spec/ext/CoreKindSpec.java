package org.femtoframework.coin.spec.ext;

import org.femtoframework.coin.exception.SpecSyntaxException;
import org.femtoframework.coin.spec.*;
import org.femtoframework.coin.spec.element.*;
import org.femtoframework.util.StringUtil;

import java.util.Map;

import static org.femtoframework.coin.CoinConstants.*;

/**
 * Core Kind Spec definition
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public class CoreKindSpec implements KindSpec {
    /**
     * The version this ResourceFactory supports
     *
     * @return Version
     */
    @Override
    public String getApiVersion() {
        return VERSION_COIN_V1;
    }

    /**
     * The Kind Class it uses
     *
     * @return The kind Class should be an Enum and Kind implementation
     */
    @Override
    public Class<? extends Kind> getKindClass() {
        return CoreKind.class;
    }

    /**
     * Convert Kind to right spec
     *
     * @param map Map like data
     * @return right spec object
     */
    public <S extends MapSpec> S toSpec(Map map) {
        String kind = MapSpec.getString(map, KIND, null);
        if (NAMESPACE.equalsIgnoreCase(kind)) {
            return (S)new NamespaceElement(map);
        }
        else if (COMPONENT.equalsIgnoreCase(kind)) {
            return (S)new ComponentElement(map);
        }
        else if (BEAN.equalsIgnoreCase(kind)) {
            return (S)new BeanElement(map);
        }
        else if (CONFIG.equalsIgnoreCase(kind)) {
            return (S)new ConfigElement(map);
        }
        else if (StringUtil.isInvalid(kind)) {
            String type = MapSpec.getString(map, CLASS, null);
            if (StringUtil.isValid(type)) { //Consider as Bean
                return (S)new BeanElement(map);
            }
            else {
                return (S)new MapElement(map);
            }
        }
        else {
            throw new SpecSyntaxException("No such kind:" + kind + " in version(" + getApiVersion() + ") ");
        }
    }
}
