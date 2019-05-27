package org.femtoframework.coin.api.nanohttpd;


import fi.iki.elonen.NanoHTTPD;
import org.femtoframework.bean.BeanPhase;
import org.femtoframework.bean.LifecycleMBean;
import org.femtoframework.coin.ResourceType;
import org.femtoframework.coin.api.APIHandler;
import org.femtoframework.coin.api.APIRequest;
import org.femtoframework.coin.api.APIResponse;
import org.femtoframework.io.IOUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.*;

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
            String method= session.getMethod().name();
            if ("POST".equalsIgnoreCase(method)) {
                //All small case in headers
                String realMethod = session.getHeaders().get("x-http-method-override");
                if ("PATCH".equalsIgnoreCase(realMethod)) {
                    method = "PATCH";

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    try {
                        IOUtil.copy(session.getInputStream(), baos);
                    } catch (IOException e) {
                        return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, "text/html",
                                "Reading request body exception:" + e.getMessage());
                    }
                    try {
                        request.setBody(baos.toString("utf8"));
                    } catch (UnsupportedEncodingException e) {
                        //Never
                    }
                }
            }
            request.setMethod(method);

            String uri = session.getUri();
            if ("/".equals(uri) || "/coin/api/v1".equals(uri) || "/coin/api/v1/".equals(uri)) {
                InputStream inputStream = APIServer.class.getClassLoader().getResourceAsStream("META-INF/html/readme.html");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                try {
                    IOUtil.copy(inputStream, baos);
                    return newFixedLengthResponse(Response.Status.OK, "text/html", baos.toString());
                }
                catch (IOException ioe) {
                    return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, "text/html",
                            "Loading page exception:" + ioe.getMessage());
                }
            }
            if (!uri.startsWith("/coin/api/v1/")) {
                return newFixedLengthResponse(Response.Status.NOT_FOUND, "text/html", "URI not found:" + uri);
            }
            String subUri = uri.substring("/coin/api/v1/".length());
            String[] paths = subUri.split("/");
            request.setPaths(paths);
            if (paths.length == 0) {
                return newFixedLengthResponse(Response.Status.NOT_FOUND, "text/html", "URI not found:" + uri);
            }

            boolean invalidPath = false;
            if (ResourceType.NAMESPACE.getPluralName().equals(paths[0])) {
                if (paths.length == 1) {
                    request.setAll(true);
                    request.setType(ResourceType.NAMESPACE);
                }
                else if (paths.length == 2 && ResourceType.NAMESPACE.getPluralName().equals(paths[0])) {
                    request.setAll(false);
                    request.setType(ResourceType.NAMESPACE);
                    request.setNamespace(paths[1]);
                }
                else if (paths.length >= 3) {
                    request.setNamespace(paths[1]);
                    ResourceType type = ResourceType.toType(paths[2]);
                    if (type == ResourceType.UNKNOWN) {
                        invalidPath = true;
                    }
                    request.setType(type);
                    if (paths.length >= 4) {
                        request.setName(paths[3]);
                    }
                    else {
                        request.setAll(true);
                    }
                }
            } else if (paths.length == 1) { // resources
                ResourceType type = ResourceType.toType(paths[0]);
                if (type == ResourceType.UNKNOWN) {
                    invalidPath = true;
                }
                else {
                    request.setAll(true);
                    request.setType(type);
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
    public void _doInit() {
        httpd = new Httpd(getHost(), getPort(), apiHandler);
    }

    /**
     * Start internally
     */
    public void _doStart() {
        try {
            httpd.start(timeout, daemon);

            String h = "*".equals(host) || "0.0.0.0".equals(host) ? "127.0.0.1" : host;
            logger.info("COIN API Server started, http://" + h + ":" + port + "/coin/api/v1/");
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
