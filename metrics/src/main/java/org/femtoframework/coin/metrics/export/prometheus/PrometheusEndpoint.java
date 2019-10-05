package org.femtoframework.coin.metrics.export.prometheus;

import fi.iki.elonen.NanoHTTPD;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.common.TextFormat;
import org.femtoframework.bean.AbstractLifecycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class PrometheusEndpoint extends AbstractLifecycle {
    private String host = "0.0.0.0";
    private int port = 8090;
    private int timeout = 20000;
    private boolean daemon = true;

    private Httpd httpd;

    private static Logger logger = LoggerFactory.getLogger(PrometheusEndpoint.class);


    private CollectorRegistry collectorRegistry;

    public void setCollectorRegistry(CollectorRegistry collectorRegistry) {
        this.collectorRegistry = collectorRegistry;
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

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public boolean isDaemon() {
        return daemon;
    }

    public void setDaemon(boolean daemon) {
        this.daemon = daemon;
    }

    public static class Httpd extends NanoHTTPD {

        private CollectorRegistry collectorRegistry;

        public Httpd(String hostname, int port, CollectorRegistry collectorRegistry) {
            super(hostname, port);
            this.collectorRegistry = collectorRegistry;
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
            String method = session.getMethod().name();
            String path = session.getUri();
            if ("GET".equalsIgnoreCase(method) && "/metrics".equals(path)) {
                try {
                    StringWriter writer = new StringWriter();
                    TextFormat.write004(writer, collectorRegistry.metricFamilySamples());

                    return newFixedLengthResponse(Response.Status.OK,
                            TextFormat.CONTENT_TYPE_004, writer.toString());
                } catch (IOException e) {
                    // This actually never happens since StringWriter::write() doesn't throw any IOException
                    throw new RuntimeException("Writing metrics failed", e);
                }
            }
            else {
                return newFixedLengthResponse(Response.Status.METHOD_NOT_ALLOWED,
                        "text/plain", "Only GET is allowed");
            }
        }
    }

    /**
     * Initiliaze internally
     */
    public void _doInit() {
        httpd = new Httpd(getHost(), getPort(), collectorRegistry);
    }

    /**
     * Start internally
     */
    public void _doStart() {
        try {
            httpd.start(getTimeout(), isDaemon());

            String h = "*".equals(getHost()) || "0.0.0.0".equals(getHost()) ? "127.0.0.1" : getHost();
            logger.info("COIN Prometheus Endpoint started, http://" + h + ":" + getPort() + "/metrics");
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
}
