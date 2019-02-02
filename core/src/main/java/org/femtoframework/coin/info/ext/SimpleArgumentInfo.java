package org.femtoframework.coin.info.ext;


import org.femtoframework.coin.info.AbstractFeatureInfo;
import org.femtoframework.coin.info.ArgumentInfo;

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

    public void setType(String type) {
        this.type = type;
    }
}
