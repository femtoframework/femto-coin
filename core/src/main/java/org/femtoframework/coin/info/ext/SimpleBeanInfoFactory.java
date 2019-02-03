package org.femtoframework.coin.info.ext;

import org.femtoframework.coin.NamespaceFactory;
import org.femtoframework.coin.annotation.*;
import org.femtoframework.coin.exception.BeanInfoSyntaxException;
import org.femtoframework.coin.ext.BaseFactory;
import org.femtoframework.coin.info.*;
import org.femtoframework.lang.reflect.Reflection;
import org.femtoframework.text.NamingConvention;
import org.femtoframework.util.StringUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public class SimpleBeanInfoFactory extends BaseFactory<BeanInfo> implements BeanInfoFactory {

    //For testing
    public SimpleBeanInfoFactory() {
    }

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
            synchronized (this) {
                beanInfo = map.get(className);
                if (beanInfo == null) {

                    Class superClass = clazz.getSuperclass();
                    List<BeanInfo> superBeanInfos = null;
                    if (superClass != null && superClass != Object.class) {
                        superBeanInfos = append(superBeanInfos, getBeanInfo(superClass, generate));
                    }
                    Class[] interfaceClasses = clazz.getInterfaces();
                    if (interfaceClasses.length > 0) {
                        for(Class interfaceClass: interfaceClasses) {
                            superBeanInfos = append(superBeanInfos, getBeanInfo(interfaceClass, generate));
                        }
                    }

                    if (generate) {
                        beanInfo = generate(clazz);
                    }
                    if (beanInfo != null || superBeanInfos != null) {
                        if (beanInfo == null) {
                            beanInfo = getBeanInfo(superClass, true);
                        }

                        if (superBeanInfos != null) {
                            for(BeanInfo parentInfo: superBeanInfos) {
                                beanInfo.mergeSuper(parentInfo);
                            }
                        }
                        if (beanInfo.getClassName() == null) {
                            beanInfo.setClassName(className);
                        }
                        map.put(className, beanInfo);
                    }
                }
            }
        }
        return beanInfo;
    }

    protected List<BeanInfo> append(List<BeanInfo> beanInfos, BeanInfo beanInfo) {
        if (beanInfo != null) {
            if (beanInfos == null) {
                beanInfos = new ArrayList<>(2);
            }
            beanInfos.add(beanInfo);
        }
        return beanInfos;
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

        Map<String, ActionInfo> actions = null;
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (Modifier.isStatic(method.getModifiers()) || !Modifier.isPublic(method.getModifiers())) {
                continue;
            }
            if (method.getDeclaringClass() == Object.class) {
                continue;
            }

            String description = "";
            Description annoDescription = method.getAnnotation(Description.class);
            if (annoDescription != null) {
                description = annoDescription.value();
            }


            Action action = method.getAnnotation(Action.class);
            if (action != null) {
                if (!isAction(method)) {
                    throw new BeanInfoSyntaxException("@Action should only be method follows the definition on @Action");
                }
                SimpleActionInfo actionInfo = new SimpleActionInfo();
                actionInfo.setName(method.getName());
                actionInfo.setDescription(description);
                actionInfo.setImpact(action.impact());
                Class<?> returnType = method.getReturnType();
                actionInfo.setReturnType(returnType == Void.class ? null : returnType.getTypeName());
                actionInfo.setMethod(method);

                if (actions == null) {
                    actions = new HashMap<>(4);
                }
                if (actions.containsKey(actionInfo.getName())) {
                    throw new BeanInfoSyntaxException("Duplicated @Action with same name");
                }

                actions.put(actionInfo.getName(), actionInfo);
            }
            else { //Could be setter or getter
                boolean isGetter = method.getParameterTypes().length == 0;
                boolean isSetter = method.getParameterTypes().length == 1;

                if (isGetter || isSetter) {
                    String methodName = method.getName();
                    Class<?> type = isGetter ? method.getReturnType() : method.getParameterTypes()[0];

                    SimplePropertyInfo propertyInfo = null;
                    String propertyName = NamingConvention.toPropertyName(methodName, isGetter ? type : null);
                    Property jsonProperty = method.getAnnotation(Property.class);
                    if (jsonProperty != null) {
                        propertyName = jsonProperty.value();
                    }

                    if (method.isAnnotationPresent(Ignore.class)) {
                        propertyInfo = (SimplePropertyInfo) propInfos.get(propertyName);
                        if (propertyInfo != null) {
                            if (isGetter) {
                                propertyInfo.setReadable(false);
                            }
                            if (isSetter) {
                                propertyInfo.setWritable(false);
                            }
                        }
                    }

                    propertyInfo = (SimplePropertyInfo) propInfos.get(propertyName);
                    if (propertyInfo == null) {
                        if (jsonProperty != null) {
                            propertyInfo = new SimplePropertyInfo(jsonProperty, null);
                            propertyInfo.setDescription(description);
                        }
                        else { // Since the property is not found in the field, so ignore this setter or getter
                            continue;
                        }
                    }

                    Getter jsonGetter = method.getAnnotation(Getter.class);
                    if (jsonGetter != null) {
                        String v = jsonGetter.value();
                        v = StringUtil.isInvalid(v) ? propertyName : v;
                        propertyInfo = (SimplePropertyInfo) propInfos.get(v);
                        if (propertyInfo != null) {
                            propertyInfo.setGetterMethod(method);
                            continue;
                        }
                    }

                    Setter jsonSetter = method.getAnnotation(Setter.class);
                    if (jsonSetter != null) {
                        String v = jsonSetter.value();
                        v = StringUtil.isInvalid(v) ? propertyName : v;
                        propertyInfo = (SimplePropertyInfo) propInfos.get(v);
                        if (propertyInfo != null) {
                            propertyInfo.setSetterMethod(method);
                            continue;
                        }
                    }

                    if (propertyInfo != null) {
                        if (isSetter && methodName.startsWith("set")) {
                            propertyInfo.setSetterMethod(method);
                        } else if (isGetter && (methodName.startsWith("get") || methodName.startsWith("is"))) {
                            propertyInfo.setGetterMethod(method);
                        }
                    }
                }
            }
        }

        if (actions != null) {
            beanInfo.setActions(actions);
        }

        if (coined != null) {
            beanInfo.setAlphabeticOrder(coined.alphabeticOrder());
            beanInfo.setIgnoreUnknownProperties(coined.ignoreUnknownProperties());
        }

        //TODO order by index
        //Order by index
        beanInfo.setProperties(propInfos);
        return beanInfo;
    }

    /**
     * 0. Method has only non-map or non-object argument(s) (Primitive, String, Enum, Array of non-structure).
     * 1. Method must can be identified by its name, so method should not have overloading methods.
     * 2. Method whose annotated with @Action, is considered as action.
     * 3. If a method is considered as an Action, it won't be considered as Getter or Setter.
     *
     * @param method Method
     * @return Is Action or not
     */
    protected boolean isAction(Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        for(Class<?> type: parameterTypes) {
            if (!isActionArgument(type)) {
                return false;
            }
        }
        return true;
    }

    /**
     * non-map or non-object argument(s) (Primitive, String, Enum, Array of non-structure)
     *
     * @param type Type
     * @return Primitive, String, Enum, List of non-structure
     */
    protected boolean isActionArgument(Class<?> type) {
        return Reflection.isNonStructureClass(type);
    }
}
