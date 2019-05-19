package org.femtoframework.coin.configurator;

import org.femtoframework.coin.CoinConstants;
import org.femtoframework.coin.Component;
import org.femtoframework.coin.Configurator;
import org.femtoframework.coin.Namespace;
import org.femtoframework.coin.exception.SpecSyntaxException;
import org.femtoframework.coin.spec.BeanSpec;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * BelongsTo configurator
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public class BelongsToConfigurator implements Configurator {
    /**
     * Configure the bean
     *
     * @param component Component
     */
    @Override
    public void configure(Component component) {
        List<String> belongsTo = component.getBelongsTo();
        if (belongsTo != null && !belongsTo.isEmpty()) {
            BeanSpec spec = component.getSpec();
            for(String onePart : belongsTo) {
                link(onePart, spec, component);
            }
        }
    }


    private void link(String link, BeanSpec spec, Component component) {
        BelongsTo belongsTo = parse(link, spec);

        Namespace ns = component.getNamespaceByName(belongsTo.getNamespace());
        if (ns == null) {
            throw new SpecSyntaxException("Invalid belongsTo field, the namespace '" + belongsTo.getNamespace() +
                    "' does not exist '" + link + "' in bean spec " + spec.getQualifiedName());
        }

        Component parent = ns.getComponentFactory().get(belongsTo.getName());
        if (parent == null) {
            throw new SpecSyntaxException("Invalid belongsTo field, the parent bean name '" + belongsTo.getName()
                    + "' does not exist '" + link + "' in bean spec " + spec.getQualifiedName());
        }

        String method = belongsTo.getMethod();
        Class<?> parentTypeClass = parent.getSpec().getTypeClass();
        Class[] parentTypeInterfaces = parentTypeClass.getInterfaces();

        Class<?> typeClass = spec.getTypeClass();
        Class[] typeInterfaces = typeClass.getInterfaces();


        for(Class parentTypeInterface : parentTypeInterfaces) {
            Method m = match(method, parentTypeInterface, typeInterfaces);
            if (m != null) {
                Object parentBean = parent.getBean(parentTypeInterface);
                Object currentBean = component.getBean();
                try {
                    m.invoke(parentBean, currentBean);
                } catch (IllegalAccessException e) {
                    throw new SpecSyntaxException("Invalid belongsTo field, the method is not able to access: '" + belongsTo.getMethod()
                            + "', belongsTo '" + link + "' in bean spec " + spec.getQualifiedName());
                } catch (InvocationTargetException e) {
                    throw new SpecSyntaxException("Invalid belongsTo field, invoking method : '" + belongsTo.getMethod()
                            + "' exception, belongsTo '" + link + "' in bean spec " + spec.getQualifiedName(), e.getCause());
                }
            }
        }

    }

    private Method match(String method, Class<?> parentTypeInterface, Class[] typeInterfaces) {
        Method[] declaredMethods = parentTypeInterface.getDeclaredMethods();
        for(Method m: declaredMethods) {
            if (m.getParameterCount() == 1 && m.getName().equals(method)) {
                if (isMatch(m.getParameterTypes()[0], typeInterfaces)) {
                    return m;
                }
            }
        }
        return null;
    }

    private boolean isMatch(Class<?> argumentInterface, Class[] typeInterfaces) {
        for(Class clazz : typeInterfaces) {
            if (argumentInterface.isAssignableFrom(clazz)) {
                return true;
            }
        }
        return false;
    }


    private BelongsTo parse(String str, BeanSpec spec) {
        int colonIndex = str.indexOf(CoinConstants.CHAR_COLON);
        BelongsTo belongsTo = new BelongsTo();
        belongsTo.setNamespace(spec.getNamespace());
        int start =  0;
        if (colonIndex > 0) {
            belongsTo.setNamespace(str.substring(0, colonIndex));
            start = colonIndex + 1;
        }
        int poundIndex = str.indexOf('#', start);
        if (poundIndex < start) {
            throw new SpecSyntaxException("Invalid belongsTo field " + str + " in bean spec " + spec.getQualifiedName()
                    + ", it should be in format [NAMESPACE':']NAME'#'METHOD, for example myNS:myBean#myMethod or myBean#myMethod");
        }
        belongsTo.setName(str.substring(start, poundIndex));
        belongsTo.setMethod(str.substring(poundIndex+1));
        return belongsTo;
    }

    public static class BelongsTo {
        private String namespace;
        private String name;
        private String method;

        public String getNamespace() {
            return namespace;
        }

        public void setNamespace(String namespace) {
            this.namespace = namespace;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }
    }
}
