package org.femtoframework.coin.codec.yaml;

import org.femtoframework.coin.info.PropertyInfo;
import org.yaml.snakeyaml.introspector.Property;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;

public class YamlProperty extends Property {

    private PropertyInfo propertyInfo;

    public YamlProperty(PropertyInfo propertyInfo) {
        super(propertyInfo.getName(), propertyInfo.getTypeClass());
        this.propertyInfo = propertyInfo;
    }

    @Override
    public Class<?>[] getActualTypeArguments() {
        return new Class[] { propertyInfo.getTypeClass() };
    }

    @Override
    public void set(Object object, Object value) throws Exception {
        propertyInfo.invokeSetter(object, value);
    }

    @Override
    public Object get(Object object) {
        return propertyInfo.invokeGetter(object);
    }

    @Override
    public List<Annotation> getAnnotations() {
        return Arrays.asList(propertyInfo.getTypeClass().getAnnotations());
    }

    @Override
    public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
        return propertyInfo.getTypeClass().getAnnotation(annotationType);
    }
}
