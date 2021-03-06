package org.femtoframework.coin.spec.element;

import org.femtoframework.coin.CoinConstants;
import org.femtoframework.coin.Component;
import org.femtoframework.coin.ComponentFactory;
import org.femtoframework.coin.remote.RemoteGenerator;
import org.femtoframework.coin.spec.CoreKind;
import org.femtoframework.coin.spec.RemoteSpec;
import org.femtoframework.implement.ImplementUtil;
import org.femtoframework.util.DataUtil;

import java.lang.reflect.Type;
import java.util.Map;

import static org.femtoframework.coin.CoinConstants.INTERFACES;

public class RemoteElement extends BeanElement implements RemoteSpec {


    public RemoteElement(Map map) {
        super(CoreKind.REMOTE, map);
    }

    /**
     * Indicate the kind of this bean
     *
     * @return
     */
    public String getType() {
        return Object.class.getName();
    }

    /**
     * The real class of the kind
     *
     * @return kind class
     */
    public Class<?> getTypeClass() {
        return Object.class;
    }

    /**
     * Interfaces for generating this remote reference
     *
     * @return Interfaces
     */
    @Override
    public String[] getInterfaces() {
        return DataUtil.getStrings(getValue(INTERFACES), null);
    }

    /**
     * URI of this remote reference
     *
     * @return URI of this reference
     */
    @Override
    public String getUri() {
        return getString(CoinConstants.URI, null);
    }

    /**
     * Return the value of this element definition
     *
     * @param expectedType Expected kind
     * @param genericType
     * @param parentComponent    Component
     * @return the value
     */
    public <T> T getValue(Class<T> expectedType, Type genericType, Component parentComponent) {
        RemoteGenerator generator = ImplementUtil.getInstance(RemoteGenerator.class);

        ComponentFactory factory = parentComponent.getCurrentComponentFactory();
        Object bean = generator.generate(expectedType != null ? expectedType.getName() : Object.class.getName(), getUri(), getInterfaces());
        Component component = factory.create(null, bean, parentComponent.getStage());
        return component.getBean(expectedType);
    }
}
