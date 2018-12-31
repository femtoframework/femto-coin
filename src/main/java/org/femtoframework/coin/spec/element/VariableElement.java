package org.femtoframework.coin.spec.element;

import org.femtoframework.coin.BeanContext;
import org.femtoframework.coin.spec.CoreKind;
import org.femtoframework.coin.spec.ElementVisitor;

/**
 * Variable Element
 *
 * @author fengyun
 * @version 1.00 2011-04-28 13:18
 */
public class VariableElement extends PrimitiveElement
{

    private String name;

    public VariableElement(CoreKind type, Object value) {
        super(type, value);
    }

    /**
     * 返回变量名
     *
     * @return
     */
    public String getName()
    {
        return name;
    }

    /**
     * 设置变量名
     *
     * @param name 变量名
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * 返回属性数据定义对应的值
     *
     * @param expectedType
     * @param context      对象部署上下文
     * @return 数据定义对应的值
     */
    public Object getValue(String expectedType, BeanContext context)
    {
//        if ("namespace".equals(name)) {
//            return context.getNamespace();
//        }
//        else if ("factory".equals(name)) {
//            return context.getObjectFactory();
//        }
//        else if ("object".equals(name)) {
//            return context.getObject();
//        }
//        else if ("name".equals(name)) {
//            return context.getObjectName();
//        }
//        else if ("object_meta".equals(name)) {
//            return context.getObjectMeta();
//        }
//        else if ("namespace_meta".equals(name)) {
//            return context.getNamespaceMeta();
//        }
//        else {
//            throw new IllegalArgumentException("Unsupported variable:" + name);
//        }
        return null;
    }

    @Override
    public void accept(ElementVisitor visitor) {

    }
}
