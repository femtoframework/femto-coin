package org.femtoframework.cron.ext;

import org.femtoframework.cron.Cron;
import org.femtoframework.cron.CrontabFile;
import org.femtoframework.pattern.ext.BaseFactory;
import org.femtoframework.util.StringUtil;

import java.io.File;
import java.util.Collection;


/**
 * Cron对象的管理器
 *
 * @author renex
 * @version 1.00  14:35:51 2003-7-7
 */
public class SimpleCrontab extends BaseFactory<Cron> implements CrontabFile {
    private String name;

    private static int NAME_SEEK = 0;

    protected boolean reload = false;

    protected String descFile;

    protected long lastModified;

    public SimpleCrontab() {
    }

    public void setFile(String file) {
        this.descFile = file;
        File f = new File(file);
        lastModified = f.lastModified();
    }

    public String getFile() {
        if (StringUtil.isInvalid(descFile)) {
            descFile = getNextName();
        }
        return descFile;
    }

    public void setReload(boolean reload) {
        this.reload = reload;
    }

    public boolean isReload() {
        return reload;
    }

    public void addCron(Cron cron) {
        super.add(cron);
    }

    public Cron removeCron(String name) {
        return super.delete(name);
    }

    public Cron getCron(String name) {
        return get(name);
    }

    public Collection<Cron> getCrons() {
        return super.getObjects();
    }

    public void clear() {
        map.clear();
    }

    public boolean isNeedUpdate() {
        File file = new File(this.descFile);
        return file.lastModified() > lastModified;
    }

    private static String getNextName() {
        NAME_SEEK++;
        return ("Crontab-" + NAME_SEEK);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
