package org.femtoframework.coin.api.nanohttpd;


import fi.iki.elonen.NanoHTTPD;
import org.femtoframework.bean.BeanPhase;
import org.femtoframework.bean.LifecycleMBean;
import org.femtoframework.coin.api.APIHandler;
import org.femtoframework.coin.api.APIRequest;
import org.femtoframework.coin.api.APIResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.femtoframework.coin.CoinConstants.*;

public class APIServer implements LifecycleMBean {

    private String host = "0.0.0.0";
    private int port = 9169;
    private int timeout = 20000;
    private boolean daemon = true;

    private APIHandler apiHandler;

    private Httpd httpd;

    private Logger logger = LoggerFactory.getLogger(APIServer.class);


    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isDaemon() {
        return daemon;
    }

    public void setDaemon(boolean daemon) {
        this.daemon = daemon;
    }

    public static class Httpd extends NanoHTTPD {

        private APIHandler apiHandler;

        public Httpd(String hostname, int port, APIHandler handler) {
            super(hostname, port);
            this.setApiHandler(handler);
        }

        /**
         * Override this to customize the server.
         * <p/>
         * <p/>
         * (By default, this returns a 404 "Not Found" plain text error response.)
         *
         * @param session The HTTP session
         * @return HTTP response, see class Response for details
         */
        public Response serve(IHTTPSession session) {
            APIRequest request = new APIRequest();
            request.setQueryParams(session.getParameters());
            request.setMethod(session.getMethod().name());

            String uri = session.getUri();
            if (!uri.startsWith("/coin/api/v1/")) {
                return newFixedLengthResponse(Response.Status.NOT_FOUND, "text/html", "URI not found:" + uri);
            }
            String subUri = uri.substring("/coin/api/v1/".length());
            String[] paths = subUri.split("/");
            request.setPaths(paths);

            boolean invalidPath = false;
            if ("namespaces".equals(paths[0]) || NAMESPACE_NAMESPACE.equals(paths[0])) {
                if (paths.length == 1) {
                    request.setAll(true);
                    request.setType(NAMESPACE_NAMESPACE);
                }
                else if (paths.length == 2 && NAMESPACE_NAMESPACE.equals(paths[0])) {
                    request.setAll(false);
                    request.setType(NAMESPACE_NAMESPACE);
                    request.setNamespace(paths[1]);
                }
                else if (paths.length == 3) {
                    request.setAll(true);
                    request.setNamespace(paths[1]);
                    if ("components".equals(paths[2])) {
                        request.setType(RESOURCE_COMPONENT);
                    } else if ("beans".equals(paths[2])) {
                        request.setType(RESOURCE_BEAN);
                    } else if ("specs".equals(paths[2])) {
                        request.setType(RESOURCE_SPEC);
                    } else if ("configs".equals(paths[2])) {
                        request.setType("config");
                    }
                    else {
                        invalidPath = true;
                    }
                } else if (paths.length == 4) {
                    request.setNamespace(paths[1]);
                    if (RESOURCE_COMPONENT.equals(paths[2])) {
                        request.setType(RESOURCE_COMPONENT);
                    } else if (RESOURCE_BEAN.equals(paths[2])) {
                        request.setType(RESOURCE_BEAN);
                    } else if (RESOURCE_SPEC.equals(paths[2])) {
                        request.setType(RESOURCE_SPEC);
                    } else {
                        invalidPath = true;
                    }
                    request.setName(paths[3]);
                }
                else {
                    invalidPath = true;
                }
            } else if (paths.length == 1) {
                if ("components".equals(paths[0])) {
                    request.setAll(true);
                    request.setType(RESOURCE_COMPONENT);
                } else if ("beans".equals(paths[0])) {
                    request.setAll(true);
                    request.setType(RESOURCE_BEAN);
                } else if ("specs".equals(paths[0])) {
                    request.setAll(true);
                    request.setType(RESOURCE_SPEC);
                } else if ("configs".equals(paths[0])) {
                    request.setAll(true);
                    request.setType(RESOURCE_CONFIG);
                } else {
                    invalidPath = true;
                }
            } else {
                invalidPath = true;
            }
            if (invalidPath) {
                return newFixedLengthResponse(Response.Status.NOT_FOUND, "text/html", "URI not found:" + uri);
            }

            try {
                APIResponse response = apiHandler.handleRequest(request);
                if (response.getCode() == 200) {
                    return newFixedLengthResponse(Response.Status.OK, response.getContentType(), response.getContent());
                } else {
                    return newFixedLengthResponse(Response.Status.lookup(response.getCode()),
                            response.getContentType(), response.getMessage());
                }
            } catch (Exception ex) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ex.printStackTrace(new PrintStream(baos));
                return newFixedLengthResponse(Response.Status.INTERNAL_ERROR,
                        "text/plain", baos.toString());
            }
        }

        public APIHandler getApiHandler() {
            return apiHandler;
        }

        public void setApiHandler(APIHandler apiHandler) {
            this.apiHandler = apiHandler;
        }
    }

    /**
     * Initiliaze internally
     */
    public void _doInitialize() {
        httpd = new Httpd(getHost(), getPort(), apiHandler);
    }

    /**
     * Start internally
     */
    public void _doStart() {
        try {
            httpd.start(timeout, daemon);
        } catch (IOException e) {
            logger.error("Start nano httpd exception", e);
        }
    }

    /**
     * Stop internally
     */
    public void _doStop() {
        httpd.stop();
    }

    public APIHandler getApiHandler() {
        return apiHandler;
    }

    @Inject
    public void setApiHandler(APIHandler apiHandler) {
        this.apiHandler = apiHandler;
        if (httpd != null) {
            httpd.setApiHandler(apiHandler);
        }
    }

    private BeanPhase beanPhase;

    @Override
    public BeanPhase _doGetPhase() {
        return beanPhase;
    }

    @Override
    public void _doSetPhase(BeanPhase phase) {
        this.beanPhase = phase;
    }
}