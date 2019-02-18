package org.femtoframework.bean.info.ext;

import org.femtoframework.bean.annotation.Coined;
import org.femtoframework.bean.annotation.Ignore;
import org.femtoframework.bean.annotation.Property;

@Coined
public class BeanInfoSample {

    @Property(index = 1)
    private String standard;

    @Property(value = "renameProeprty", index = 2)
    private String _renameProperty;

    @Property(value="getterOnly", writable = false, index = 3)
    private String getterOnly = "GetterOnly";

    @Ignore
    private transient Object ignoredProperty;

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public String getRenameProperty() {
        return _renameProperty;
    }

    public void setRenameProperty(String _renameProperty) {
        this._renameProperty = _renameProperty;
    }

    public String getGetterOnly() {
        return getterOnly;
    }

    public Object getIgnoredProperty() {
        return ignoredProperty;
    }

    public void setIgnoredProperty(Object ignoredProperty) {
        this.ignoredProperty = ignoredProperty;
    }
}
