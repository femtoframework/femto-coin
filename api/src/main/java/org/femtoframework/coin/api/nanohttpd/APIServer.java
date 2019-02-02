package org.femtoframework.coin.api.nanohttpd;


import fi.iki.elonen.NanoHTTPD;

public class APIServer extends NanoHTTPD {
    public APIServer(int port) {
        super(port);
    }

    public APIServer(String hostname, int port) {
        super(hostname, port);
    }
}
