package org.femtoframework.coin.ext;

import org.femtoframework.coin.CoinController;
import org.femtoframework.coin.CoinReloader;
import org.femtoframework.util.SystemUtil;
import org.femtoframework.util.thread.LifecycleThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

public class SimpleCoinReloader extends LifecycleThread
        implements CoinReloader {

    private CoinController controller;

    private Map<URI, Long> lastModified = new HashMap<>();

    private int interval;

    public SimpleCoinReloader(CoinController controller) {
        this.controller = controller;
        boolean isDev = SystemUtil.getEnvironment().isDev();
        this.interval = isDev ? 5 : 10;
    }

    /**
     * Reload specs
     *
     * @param force Reload anyway if it is true, otherwise follow the strategies
     */
    @Override
    public synchronized void reload(boolean force)  throws IOException {
        Set<URI> uris = controller.getUris();
        if (force) {
            reload(uris.toArray(new URI[uris.size()]));
        }
        else {
            boolean isDev = SystemUtil.getEnvironment().isDev();
            //URI --> New LastModified
            Map<URI, Long> toReload = new HashMap<>();
            for(URI u: uris) {
                String scheme = u.getScheme();
                if (!isDev && !"file".equalsIgnoreCase(scheme)) { //Check file ONLY for non-DEV environment
                    continue;
                }
                Long current = null;
                try {
                    URL url = u.toURL();
                    URLConnection conn = url.openConnection();
                    current = conn.getLastModified();
                    if (conn instanceof HttpURLConnection) {
                        ((HttpURLConnection)conn).disconnect();
                    }
                }
                catch(Exception ex) {
                    log.warn("Exception", ex);
                }
                if (current != null) {
                    Long last = lastModified.get(u);
                    if (last == null) {
                        lastModified.put(u, current);
                    }
                    else if (current.longValue() != last.longValue()) {
                        toReload.put(u, current);
                    }
                }
            }

            if (!toReload.isEmpty()) {
                reload(toReload.keySet().toArray(new URI[toReload.size()]));
                lastModified.putAll(toReload);
            }
        }
    }

    protected void reload(URI... uris) throws IOException {
        controller.apply(uris);
    }


    private static Logger log = LoggerFactory.getLogger(SimpleCoinReloader.class);

    /**
     * The real logic
     *
     * @throws Exception Exception
     * @see #run()
     */
    @Override
    protected void doRun() {
        int sleep = interval * 1000;
        try {
            Thread.sleep(sleep);

            reload();
        }
        catch(Throwable ex) {
            log.warn("Reload", ex);
        }
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }
}
