package org.femtoframework.coin.launcher;

import java.net.URI;
import java.util.EventListener;
import java.util.List;

public interface LauncherListener extends EventListener {

    /**
     * On Before Starting
     */
    default void onBeforeStarting() {

    }

    /**
     * On Yaml Files Found
     *
     * @param yamlFiles Yaml Files
     * @return URI List
     */
    default List<URI> onYamlFilesFound(List<URI> yamlFiles) {
        return yamlFiles;
    }

    /**
     * On After Loading Yaml Files
     *
     * @param yamlFiles Yaml Files
     */
    default void onAfterLoadingYamlFiles(List<URI> yamlFiles) {

    }
}
