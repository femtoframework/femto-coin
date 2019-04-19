package org.femtoframework.coin.launcher;

import org.femtoframework.implement.ImplementConfig;
import org.femtoframework.implement.ImplementUtil;
import org.femtoframework.lang.reflect.Reflection;
import org.femtoframework.pattern.EventListeners;

import java.net.URI;
import java.util.Collection;
import java.util.List;

public class LauncherListeners extends EventListeners<LauncherListener> implements LauncherListener
{
    public LauncherListeners() {
        ImplementConfig<LauncherListener> implementConfig = ImplementUtil.getImplementConfig(LauncherListener.class);
        if (implementConfig != null) {
            Collection<String> classes = implementConfig.getImplementations();
            for (String className : classes) {
                addListener((LauncherListener) Reflection.newInstance(className));
            }
        }
    }

    /**
     * On Before Starting
     */
    @Override
    public void onBeforeStarting() {
        for(LauncherListener launcherListener: getListeners()) {
            launcherListener.onBeforeStarting();
        }
    }

    /**
     * On Yaml Files Found
     *
     * @param yamlFiles Yaml Files
     * @return URI List
     */
    @Override
    public List<URI> onYamlFilesFound(List<URI> yamlFiles) {
        for(LauncherListener launcherListener: getListeners()) {
            yamlFiles = launcherListener.onYamlFilesFound(yamlFiles);
        }
        return yamlFiles;
    }

    /**
     * On After Loading Yaml Files
     *
     * @param yamlFiles Yaml Files
     */
    @Override
    public void onAfterLoadingYamlFiles(List<URI> yamlFiles) {
        for(LauncherListener launcherListener: getListeners()) {
            launcherListener.onAfterLoadingYamlFiles(yamlFiles);
        }
    }
}
