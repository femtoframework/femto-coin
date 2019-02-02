package org.femtoframework.coin.info;

import org.femtoframework.coin.annotation.Action;

import java.util.List;

/**
 * An ACTION indicate the method could be invoked from Remote, API or JMX.
 *
 * 0. Method has only non-map or non-object argument(s) (Primitive, String, Enum, Array of non-structure).
 * 1. Method must can be identified by its name, so method should not have overloading methods.
 * 2. Method whose annotated with @Action, is considered as action.
 * 3. If a method is considered as an Action, it won't be considered as Getter or Setter.
 */
public interface ActionInfo extends FeatureInfo {

    /**
     * Return the method name, usually it is same as action name,
     * The name could be redefined in the @Action, we have to remember the method name for finding the method again.
     * @return Method Name
     */
    default String getMethodName() {
        return getName();
    }

    /**
     * Return Type of this action
     *
     * @return Return Type, if the return is void, it returns <code>null</code>
     */
    String getReturnType();

    /**
     * 返回功能的参数信息
     *
     * @return 参数信息数组
     */
    List<ArgumentInfo> getArguments();

    /**
     * What's the impact?
     *
     * @see Action.Impact
     * @return the name() of Action.Impact
     */
    String getImpact();


    /**
     * Invoke the action method
     *
     * @param bean The bean
     * @param arguments Invoke the action
     * @return value
     */
    <T> T invoke(Object bean, Object... arguments) throws Exception;
}
