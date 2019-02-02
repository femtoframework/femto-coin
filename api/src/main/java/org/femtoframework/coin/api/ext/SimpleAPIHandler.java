package org.femtoframework.coin.api.ext;

import org.femtoframework.coin.CoinModule;
import org.femtoframework.coin.api.APIHandler;
import org.femtoframework.coin.api.APIRequest;
import org.femtoframework.coin.api.APIResponse;
import org.femtoframework.coin.spi.CoinModuleAware;

/**
 * API Handler
 * 
 * @author Sheldon Shao
 * @version 1.0
 */
public class SimpleAPIHandler implements APIHandler, CoinModuleAware {

    private CoinModule coinModule;

    /**
     * Handle API Request
     *
     * @param request API Request
     * @return
     */
    @Override
    public APIResponse handleRequest(APIRequest request) {

        APIResponse response = new APIResponse();
        response.setCode(200);

        String resourceType = request.getType();
        switch (resourceType) {
            case "namespace":
                if (request.isAll()) {

                }
        }
        return null;
    }




    @Override
    public void setCoinModule(CoinModule module) {
        this.coinModule = module;
    }
}
