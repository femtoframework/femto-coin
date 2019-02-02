package org.femtoframework.coin.api;

import java.io.IOException;

/**
 * API Handler
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public interface APIHandler {

    /**
     * Handle API Request
     *
     * @param request API Request
     * @return
     */
    APIResponse handleRequest(APIRequest request) throws IOException;
}
