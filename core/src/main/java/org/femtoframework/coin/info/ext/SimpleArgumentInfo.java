package org.femtoframework.coin.info.ext;

import org.femtoframework.coin.info.AbstractFeatureInfo;
import org.femtoframework.coin.info.ArgumentInfo;
import org.femtoframework.util.convert.ConverterUtil;
import org.femtoframework.util.convert.DataConverter;

/**
 * Simple Argument Info
 */
public class SimpleArgumentInfo extends AbstractFeatureInfo implements ArgumentInfo {

    private String type;

    public SimpleArgumentInfo() {
    }

    public SimpleArgumentInfo(String name, String description) {
        super(name, description);
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public <V> V toValue(Object value) {
        if (value == null) {
            return null;
        }
        DataConverter converter = ConverterUtil.getConverter(type);
        if (converter != null) {
            return (V)converter.convert(value);
        }
        else {
            return (V)value;
        }
    }

    public void setType(String type) {
        this.type = type;
    }
}
