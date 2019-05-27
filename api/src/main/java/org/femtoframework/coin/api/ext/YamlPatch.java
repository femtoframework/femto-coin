package org.femtoframework.coin.api.ext;

import org.femtoframework.bean.info.*;
import org.femtoframework.coin.ResourceType;
import org.femtoframework.coin.api.APIPatch;
import org.femtoframework.coin.api.APIPatchException;
import org.femtoframework.coin.spec.CoreKind;
import org.femtoframework.coin.spec.MapSpec;
import org.femtoframework.coin.spec.element.PrimitiveElement;
import org.femtoframework.parameters.Parameters;
import org.femtoframework.parameters.ParametersMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.util.List;
import java.util.Map;

/**
 * Patch with yaml
 */
public class YamlPatch implements APIPatch {

    private BeanInfoFactory beanInfoFactory;

    public YamlPatch(BeanInfoFactory beanInfoFactory) {
        this.beanInfoFactory = beanInfoFactory;
    }

    public Parameters<Object> parsePatch(String body) {
        Yaml yaml = new Yaml();
        return yaml.loadAs(body, ParametersMap.class);
    }

//    private enum PatchDirective {
//        replace,
//
//        merge,
//
//        delete
//    }

//    DELETEF_ROMP_RIMITIVE_LIST,
//
//    SET_ELEMENT_ORDER,
//
//    RETAINS_KEY,

    private static Logger log = LoggerFactory.getLogger(YamlPatch.class);

    @Override
    public void apply(ResourceType resourceType, Object resource, Parameters<Object> patch) throws APIPatchException {
//        if (resourceType == ResourceType.CONFIG) {
//            Parameters config = (Parameters)resource;
//            PatchDirective directive = patch.getEnum(PatchDirective.class, "$patch", PatchDirective.replace);
//            for(String key: patch.keySet()) {
//                if (!"$patch".equals(key)) {
//                    Object value = patch.get(key);
//                    if (value == null) {
//                        config.remove(value);
//                    }
//                    else if (value instanceof Map) {
//                        Parameters next = config.getParameters(key);
//                        if (next == null) {//Add
//                            config.put(key, value);
//                        }
//                        else {
//                            if (directive == PatchDirective.delete) {
//                                config.remove(key);
//                            }
//                            else if (directive == PatchDirective.merge) {
//                                next.putAll((Map)value);
//                            }
//                            else if (directive == PatchDirective.replace) {
//                                config.put(key, patch.getParameters(key));
//                            }
//                        }
//                    }
//                    else {
//                        Object currentValue = config.get(key);
//                        if (currentValue == null) {
//                            config.put(key, value);
//                        }
//                        else {
//                            if (directive == PatchDirective.delete) {
//                                config.remove(key);
//                            }
//                            else {
//                                config.put(key, value);
//                            }
//                        }
//                    }
//                }
//            }
//        }
        if (resourceType == ResourceType.BEAN) {
            for(String key: patch.keySet()) {
                if (key.startsWith("$invoke/")) {
                    String actionName = key.substring("$invoke/".length());
                    BeanInfo beanInfo = beanInfoFactory.getBeanInfo(resource.getClass(), true);
                    ActionInfo actionInfo = beanInfo.getAction(actionName);
                    if (actionInfo == null) {
                        throw new APIPatchException("No such action '" + actionName + "' at resource:" + resource.getClass());
                    }
                    else { // Invoke action
                        List<ArgumentInfo> argumentInfos = actionInfo.getArguments();
                        try {
                            Object returnValue = null;
                            if (argumentInfos == null || argumentInfos.isEmpty()) {
                                returnValue = actionInfo.invoke(resource);
                            } else {
                                int size = argumentInfos.size();
                                Object[] arguments = new Object[size];
                                for(int i = 0; i < size; i ++) {
                                    ArgumentInfo argumentInfo = argumentInfos.get(i);
                                    arguments[i] = argumentInfo.toValue(patch.get(argumentInfo.getName()));
                                }
                                returnValue = actionInfo.invoke(resource, arguments);
                            }

                            if (returnValue != null) {
                                log.warn("Action result:" + returnValue);
                            }

//                            if (returnValue != null) { // Return the value if the returnValue is not null and the action specified as "INFO" or "ACTION_INFO"
//                                Action.Impact impact = actionInfo.getImpact();
//                                if (Action.Impact.ACTION_INFO == impact || Action.Impact.INFO == impact) {
//                                    encoder.encode(toVisible(null, returnValue), baos);
//                                }
//                            }
                        }
                        catch (Exception ex) {
                            throw new APIPatchException("Invoking action '" + actionName + "' exception at resource:"
                                    + resource.getClass() + " " + ex.getMessage());
                        }
                    }
                }
                else { //Setter
                    Object value = patch.get(key);
                    if (resource instanceof MapSpec) {
                        ((MapSpec) resource).put(key, new PrimitiveElement<>(CoreKind.STRING, value));
                    } else if (resource instanceof Map) {
                        ((Map) resource).put(key, value);
                    } else {
                        BeanInfo beanInfo = beanInfoFactory.getBeanInfo(resource.getClass(), true);
                        PropertyInfo propertyInfo = beanInfo.getProperty(key);
                        if (propertyInfo == null || !propertyInfo.isWritable()) {
                            throw new APIPatchException("No such property '" + key + "' or it is not allowed. ");
                        } else {
                            try {
                                propertyInfo.invokeSetter(resource, value);
                            } catch (Exception ex) {
                                throw new APIPatchException("Invoking setter exception:" + ex.getMessage());
                            }
                        }
                    }
                }
            }
        }
    }

}
