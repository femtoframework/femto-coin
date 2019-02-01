package org.femtoframework.coin.info.ext;

import org.femtoframework.coin.NamespaceFactory;
import org.femtoframework.coin.annotation.*;
import org.femtoframework.coin.ext.BaseFactory;
import org.femtoframework.coin.info.BeanInfo;
import org.femtoframework.coin.info.BeanInfoFactory;
import org.femtoframework.coin.info.PropertyInfo;
import org.femtoframework.text.NamingConvention;
import org.femtoframework.util.StringUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

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
                    if (generate) {
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

        LinkedHashMap<String, PropertyInfo> propInfos = new LinkedHashMap<>(4);

        Coined coined = clazz.getAnnotation(Coined.class);

        SimpleBeanInfo beanInfo = new SimpleBeanInfo();

        Description classDescription = clazz.getAnnotation(Description.class);
        if (classDescription != null) {
            beanInfo.setDescription(classDescription.value());
        }

        Field[] fields = clazz.getDeclaredFields();
        for(int i = 0; i < fields.length; i ++) {
            Field field = fields[i];
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            if (field.isAnnotationPresent(Ignore.class)) { //If the field is ignored, the entire property is ignored
                continue;
            }
            if (field.getDeclaringClass() == Object.class) {
                continue;
            }
            Property jsonProperty = field.getAnnotation(Property.class);
            SimplePropertyInfo propertyInfo = null;
            if (jsonProperty != null) {
                propertyInfo = new SimplePropertyInfo(jsonProperty, field);
            }
            else {
                propertyInfo = new SimplePropertyInfo(field.getName(), field.getType().getTypeName());
            }
            propInfos.put(propertyInfo.getName(), propertyInfo);
            Description jsonPropertyDescription = field.getAnnotation(Description.class);
            if (jsonPropertyDescription != null) {
                propertyInfo.setDescription(jsonPropertyDescription.value());
            }
        }

        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (Modifier.isStatic(method.getModifiers()) || !Modifier.isPublic(method.getModifiers())) {
                continue;
            }
            if (method.getDeclaringClass() == Object.class) {
                continue;
            }


            boolean isGetter = method.getParameterTypes().length == 0;
            boolean isSetter = method.getParameterTypes().length == 1;

            if (isGetter || isSetter) {
                String methodName = method.getName();
                Class<?> type = isGetter ? method.getReturnType() : method.getParameterTypes()[0];

                SimplePropertyInfo propertyInfo = null;
                String propertyName = NamingConvention.toPropertyName(methodName, type);
                if (method.isAnnotationPresent(Ignore.class)) {
                    propertyInfo = (SimplePropertyInfo)propInfos.get(propertyName);
                    if (propertyInfo != null) {
                        if (isGetter) {
                            propertyInfo.setReadable(false);
                        }
                        if (isSetter) {
                            propertyInfo.setWritable(false);
                        }
                    }
                }

                Property jsonProperty = method.getAnnotation(Property.class);
                if (jsonProperty != null) {
                    propertyInfo = (SimplePropertyInfo)propInfos.get(jsonProperty.value());
                    if (propertyInfo == null) {
                        propertyInfo = new SimplePropertyInfo(jsonProperty, null);
                    }
                    if (methodName.startsWith("set")) {
                        propertyInfo.setSetter(method.getName());
                    }
                    else if (methodName.startsWith("get") || methodName.startsWith("is")) {
                        propertyInfo.setGetter(methodName);
                    }
                }

                Getter jsonGetter = method.getAnnotation(Getter.class);
                if (jsonGetter != null) {
                    String v = jsonGetter.value();
                    v = StringUtil.isInvalid(v) ? propertyName : v;
                    propertyInfo = (SimplePropertyInfo)propInfos.get(v);
                    if (propertyInfo != null) {
                        propertyInfo.setGetter(methodName);
                    }
                }

                Setter jsonSetter = method.getAnnotation(Setter.class);
                if (jsonSetter != null) {
                    String v = jsonSetter.value();
                    v = StringUtil.isInvalid(v) ? propertyName : v;
                    propertyInfo = (SimplePropertyInfo)propInfos.get(v);
                    if (propertyInfo != null) {
                        propertyInfo.setSetter(methodName);
                    }
                }

                Description jsonPropertyDescription = method.getAnnotation(Description.class);
                if (jsonPropertyDescription != null) {
                    if (propertyInfo != null) {
                        propertyInfo.setDescription(jsonPropertyDescription.value());
                    }
                }
            }
        }

        if (coined != null) {
            beanInfo.setAlphabeticOrder(coined.alphabeticOrder());
            beanInfo.setIgnoreUnknownProperties(coined.ignoreUnknownProperties());
        }


            //Order by index
        beanInfo.setInfos(propInfos);
        return beanInfo;
    }
}
