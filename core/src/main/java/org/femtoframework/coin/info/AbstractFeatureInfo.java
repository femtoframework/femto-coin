package org.femtoframework.coin.info;

/**
 * Implements FeatureInfo
 * 
 * @author Sheldon Shao
 * @version 1.0
 */
public abstract class AbstractFeatureInfo implements FeatureInfo {
    private String name;
    private String description;

    public AbstractFeatureInfo() {
    }

    public AbstractFeatureInfo(String name, String description) {
        setName(name);
        setDescription(description);
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
