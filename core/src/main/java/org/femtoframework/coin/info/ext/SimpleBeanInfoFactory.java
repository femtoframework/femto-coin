package org.femtoframework.coin.info.ext;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.femtoframework.coin.NamespaceFactory;
import org.femtoframework.coin.ext.BaseFactory;
import org.femtoframework.coin.info.BeanInfo;
import org.femtoframework.coin.info.BeanInfoFactory;
import org.femtoframework.coin.info.PropertyInfo;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class SimpleBeanInfoFactory extends BaseFactory<BeanInfo> implements BeanInfoFactory {

    public SimpleBeanInfoFactory(NamespaceFactory namespaceFactory, String namespace) {
        super(namespaceFactory, namespace);
    }

    @Override
    public BeanInfo getBeanInfo(final Class clazz, boolean generate) {
        if (clazz == null) {
            throw new IllegalArgumentException("Null class parameter.");
        }

        String className = clazz.getName();
        BeanInfo beanInfo = map.get(className);
        if (beanInfo == null) {
            synchronized (clazz) {
                beanInfo = map.get(className);
                if (beanInfo == null) {

                    Class superClass = clazz.getSuperclass();
                    BeanInfo superBeanInfo = null;
                    if (superClass != null && superClass != Object.class) {
                        superBeanInfo = getBeanInfo(superClass, generate);
                    }
                    if (beanInfo == null && generate) {
                        beanInfo = generate(clazz);
                    }
                    if (beanInfo != null) {
                        if (superBeanInfo != null) {
                            beanInfo.mergeSuper(superBeanInfo);
                        }
                        if (beanInfo.getClassName() == null) {
                            beanInfo.setClassName(className);
                        }
                        map.put(className, beanInfo);
                    }
                    else if (superBeanInfo != null) {
                        beanInfo = superBeanInfo;
                        map.put(className, superBeanInfo);
                    }
                }
            }
        }
        return beanInfo;
    }

    /**
     * Generate bean info automatically
     *
     * @param clazz Class
     * @return BeanInfo
     */
    protected BeanInfo generate(Class<?> clazz) {

        Method[] methods = clazz.getDeclaredMethods();

        List<PropertyInfo> propInfos = new ArrayList<>(4);

        for (Method method : methods) {
            if (Modifier.isStatic(method.getModifiers()) || !Modifier.isPublic(method.getModifiers())) {
                continue;
            }
            if (method.getDeclaringClass() == Object.class) {
                continue;
            }


        }


        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            JsonProperty property = field.getAnnotation(JsonProperty.class);
            if (property != null) {
//                propInfos.add(new SimplePropertyInfo(property, field));
                continue;
            }
        }

        return null;
    }
}
