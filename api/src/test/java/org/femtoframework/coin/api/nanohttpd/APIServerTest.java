package org.femtoframework.coin.api.nanohttpd;

import org.femtoframework.coin.launcher.Launcher;
import org.junit.Test;

public class APIServerTest {

    @Test
    public void testAPIServer() throws Exception {
//        APIServer server = new APIServer();
//        server.setApiHandler(new SimpleAPIHandler());
//        server.setDaemon(false);
//        server.initialize();
//        server.start();
        Launcher.main();

        Thread.sleep(60000000);
    }
}