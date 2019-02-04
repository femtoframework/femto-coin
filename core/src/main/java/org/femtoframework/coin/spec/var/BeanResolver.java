package org.femtoframework.coin.spec.var;

import org.femtoframework.coin.*;
import org.femtoframework.coin.exception.SpecSyntaxException;
import org.femtoframework.coin.spec.VariableResolver;
import org.femtoframework.coin.spec.VariableSpec;
import org.femtoframework.coin.spi.CoinModuleAware;
import org.femtoframework.util.StringUtil;

import javax.naming.NamingException;

/**
 * Bean Resolver
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public class BeanResolver implements VariableResolver, CoinModuleAware {

    private CoinModule coinModule;

    /**
     * Resolve variable by spec
     *
     * @param <T> Convert it to type
     * @param var VariableSpec
     * @param expectedType
     * @param component
     * @return
     */
    @Override
    public <T> T resolve(VariableSpec var, Class<T> expectedType, Component component) {
        NamespaceFactory namespaceFactory = component.getNamespaceFactory();
        String prefix = var.getPrefix();
        Namespace namespace;
        if (StringUtil.isInvalid(prefix) || "b".equalsIgnoreCase(prefix)) {
            namespace = component.getCurrentNamespace();;
        }
        else {
            namespace = namespaceFactory.get(prefix);
            if (namespace == null) {
                throw new SpecSyntaxException("No such namespace '" + prefix + "' in ${" + var.getName() + "}");
            }
        }
        String suffix = var.getSuffix();
        if (suffix == null) {
            return (T)namespace;
        }
        if (suffix.indexOf(CoinConstants.CHAR_DELIM) > 0) {
            try {
                return (T)coinModule.getLookup().lookup(namespace.getName() + CoinConstants.CHAR_COLON + var.getSuffix());
            } catch (NamingException e) {
                throw new SpecSyntaxException("Bean name syntax exception:" + var.getName(), e);
            }
        }
        else {
            Component next = namespace.getComponentFactory().get(suffix);
            if (next == null) {
                throw new SpecSyntaxException("No such component '" + suffix + "' in ${" + var.getName() + "}");
            }
            return next.getBean(expectedType);
        }
    }

    @Override
    public void setCoinModule(CoinModule module) {
        this.coinModule = module;
    }
}
