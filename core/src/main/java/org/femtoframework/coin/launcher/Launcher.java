package org.femtoframework.coin.launcher;

import org.femtoframework.bean.Startable;
import org.femtoframework.coin.CoinController;
import org.femtoframework.coin.CoinModule;
import org.femtoframework.coin.CoinUtil;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Coin Launcher
 *
 * Flow,
 *
 * 1. Get all META-INF/spec/component.yaml
 * 2. Get META-INF/spec/application.yaml
 * 3. Get all yaml files in args
 * 4. Call CoinController.create
 *
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public class Launcher {

    /**
     * Main Method
     *
     * @param args Yaml files
     * @throws IOException
     */
    public static void main(String... args) throws IOException {
        LauncherListeners launcherListener = new LauncherListeners();
        launcherListener.onBeforeStarting();

        CoinModule module = CoinUtil.getModule();
        CoinController controller = module.getController();
        List<URI> componentYamls = controller.getComponentYamls(null);
        List<URI> applicationYamls = controller.getApplicationYamls(null);

        List<URI> allUris = new ArrayList<>(componentYamls.size() + 1 + args.length);
        allUris.addAll(componentYamls);
        if (applicationYamls != null) {
            allUris.addAll(applicationYamls);
        }
        for (String arg : args) {
            allUris.add(new File(arg).toURI());
        }

        allUris = launcherListener.onYamlFilesFound(allUris);

        if (allUris.isEmpty()) {
            System.out.println("WARNING: no any yaml files, launcher exit");
            System.exit(0);
        }

        controller.create(allUris.toArray(new URI[allUris.size()]));

        launcherListener.onAfterLoadingYamlFiles(allUris);

        ((Startable)module).start();
    }
}
