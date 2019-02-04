package org.femtoframework.coin.spec.element;

import org.femtoframework.coin.spec.Element;
import org.femtoframework.coin.spec.ListSpec;
import org.femtoframework.coin.spec.MapSpec;
import org.femtoframework.parameters.ParametersMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Convert MapSpec to Parameters
 */
public class SpecParameters extends ParametersMap<Object> {

    public SpecParameters(MapSpec<Element> spec) {
        Set<String> keys = spec.keySet();
        for(String key: keys) {
            Element element = spec.get(key);
            put(key, toValue(element));
        }
    }

    protected Object toValue(Element element) {
        if (element instanceof PrimitiveElement) {
            return ((PrimitiveElement)element).getPrimitiveValue();
        }
        else if (element instanceof MapSpec) {
            return new SpecParameters((MapSpec)element);
        }
        else if (element instanceof ListSpec) {
            ListSpec<Element> listSpec = (ListSpec)element;
            List list = new ArrayList(listSpec.size());
            for(Element e : listSpec) {
                list.add(toValue(e));
            }
            return list;
        }
        else {
            throw new IllegalStateException("Unsupported type:" + element);
        }
    }
}
