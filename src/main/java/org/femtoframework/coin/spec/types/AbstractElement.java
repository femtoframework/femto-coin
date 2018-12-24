/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.femtoframework.coin.spec.types;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.femtoframework.coin.spec.Element;
import org.femtoframework.coin.spec.ElementType;

import java.io.Serializable;

/**
 * Abstract Element
 *
 * @author fengyun
 * @version 1.00 2011-04-28 09:35
 */
public abstract class AbstractElement implements Element, Serializable
{

    protected ElementType type = null;

    private String elementClass = null;

//    private transient Class<?> typeClass = null;

    /**
     * Type of the types
     *
     * @return Type of the types
     */
    @JsonIgnore
    public ElementType getType()
    {
        return type;
    }

    /**
     * Set Type of the types
     *
     * @param type Type
     */
    public void setType(ElementType type)
    {
        this.type = type;
    }

//    /**
//     * Create collection
//     *
//     * @return
//     */
//    protected static <T> T createCollection(Class<T> clazz, int initSize)
//    {
//        if (clazz.isArray()) {
//            Class<?> ct = clazz.getComponentType();
//            return (T)Array.newInstance(ct, initSize);
//        }
//        return createContainer(clazz, initSize);
//    }
//
//    private static final Class[] TYPE_INIT_SIZE = new Class[]{Integer.TYPE};
//
//    /**
//     * 创建容器类型对象
//     *
//     * @param clazz 类名
//     * @return
//     */
//    protected static <T> T createContainer(Class<T> clazz, int initSize)
//    {
//        try {
//            Constructor<T> constr = clazz.getConstructor(TYPE_INIT_SIZE);
//            return constr.newInstance(initSize);
//        }
//        catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException ignore) {
//            //Ignore
//        }
//
//        try {
//            return clazz.newInstance();
//        }
//        catch (IllegalAccessException e) {
//            throw new ObjectCreationException("Illegal access", e);
//        }
//        catch (InstantiationException e) {
//            throw new ObjectCreationException("Instantiation", e);
//        }
//    }
//
//    /**
//     * 返回属性数据定义对应的值
//     *
//     * @param context 对象部署上下文
//     */
//    protected static void fillCollection(Object collection, BeanSpec[] data, BeanContext context)
//    {
//        if (data == null || data.length == 0) {
//            //构建一个空的默认的值
//            return;
//        }
//
//        if (collection instanceof List) {
//            List list = (List)collection;
//            for (int i = 0, len = data.length; i < len; i++) {
//                list.add(data[i].getValue(null, context));
//            }
//        }
//        else if (collection instanceof Set) {
//            Set set = (Set)collection;
//            for (int i = 0, len = data.length; i < len; i++) {
//                set.add(data[i].getValue(null, context));
//            }
//        }
//        else if (collection.getClass().isArray()) {
//            Class<?> expectedClass = collection.getClass().getComponentType();
//            for (int i = 0, len = data.length; i < len; i++) {
//                Object value = data[i].getValue(expectedClass.getName(), context);
//                //The conversion was done by #getValue
//                Array.set(collection, i, value);
//            }
//        }
//        else {
//            throw new CoinException("Unsupported type for <list> structure");
//        }
//    }
//
//     /**
//     * 返回属性数据定义对应的值
//     *
//     * @param context 对象部署上下文
//     */
//    protected static void fillCollection(Object collection, List<BeanSpec> data, BeanContext context)
//    {
//        if (data == null || data.size() == 0) {
//            //构建一个空的默认的值
//            return;
//        }
//
//        if (collection instanceof List) {
//            List list = (List)collection;
//            for (int i = 0, len = data.size(); i < len; i++) {
//                list.add(data.get(i).getValue(null, context));
//            }
//        }
//        else if (collection instanceof Set) {
//            Set set = (Set)collection;
//            for (int i = 0, len = data.size(); i < len; i++) {
//                set.add(data.get(i).getValue(null, context));
//            }
//        }
//        else if (collection.getClass().isArray()) {
//            Class<?> expectedClass = collection.getClass().getComponentType();
//            for (int i = 0, len = data.size(); i < len; i++) {
//                Object value = data.get(i).getValue(expectedClass.getName(), context);
//                //The conversion was done by #getValue
//                Array.set(collection, i, value);
//            }
//        }
//        else {
//            throw new CoinException("Unsupported type for <list> structure");
//        }
//    }
//
//    /**
//     * 返回属性数据定义对应的值
//     *
//     * @return 数据定义对应的值
//     */
//    protected static Object getValue(String type, Object value, Object defValue)
//    {
//        if (type != null) {
//            return ConverterUtil.convertToType(value, type, defValue);
//        }
//        else {
//            return value;
//        }
//    }
//
//    /**
//     * 根据类型和数据定义数组合成对象
//     *
//     * @param expectedType 数据类型
//     * @param array        定义数组
//     * @param context      环境
//     * @return
//     */
//    public static Object getValue(String expectedType, BeanSpec[] array, BeanContext context)
//    {
//        int len = array.length;
//        if (len == 1) {
//            //如果只有一个数据项，那么直接用这个数据项的值
//            return array[0].getValue(expectedType, context);
//        }
//        else {
//            if (expectedType != null) {
//                //有指定的类型，那么用指定的类型来构建集合
//                Class<?> typeClass = null;
//                try {
//                    typeClass = Reflection.getClass(expectedType);
//                }
//                catch (ClassNotFoundException e) {
//                    throw new NoSuchClassException(expectedType);
//                }
//                Object collection = createCollection(typeClass, len);
//                fillCollection(collection, array, context);
//                return collection;
//            }
//            else {
//                //先获得数据
//                Object[] dataArray = new Object[len];
//                for (int i = 0; i < len; i++) {
//                    dataArray[i] = array[i].getValue(null, context);
//                }
//
//                //确定产生什么类型的数组
//                //检查所有的数据项类型是不是一致
//                Class<?> clazz = dataArray[0].getClass();
//                for (int i = 1; i < len; i++) {
//                    if (dataArray[i] == null) {
//                        continue;
//                    }
//                    if (clazz != dataArray[i].getClass()) {
//                        //如果有一个类型不同，那么就返回对象数组
//                        return dataArray;
//                    }
//                }
//                //类型都是相同的，那么返回相应的类型数组
//                Object newArray = Array.newInstance(clazz, len);
//                for (int i = 0; i < len; i++) {
//                    Array.set(newArray, i, dataArray[i]);
//                }
//                return newArray;
//            }
//        }
//    }

    /**
     * Convert type(String) to Class
     *
     * @return Class
     */
    @JsonIgnore
    public String getElementClass()
    {
        return elementClass;
    }

    /**
     * Convert type(String) to Class
     *
     * @return
     */
    protected Class<?> getTypeClass(String expectedType)
    {
        //TODO
//        ElementType type = this.type != null ? this.type : expectedType;
//        if (type != null) {
//            if (typeClass == null) {
//                try {
//                    typeClass = Reflection.getClass(type);
//                }
//                catch (ClassNotFoundException e) {
//                    throw new NoSuchClassException("No such class:" + type);
//                }
//            }
//        }
//        return typeClass;
        return null;
    }

    public void setElementClass(String elementClass) {
        this.elementClass = elementClass;
    }

}
