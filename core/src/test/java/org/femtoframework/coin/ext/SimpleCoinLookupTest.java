package org.femtoframework.coin.ext;

import org.femtoframework.coin.CoinController;
import org.femtoframework.coin.CoinLookup;
import org.femtoframework.coin.CoinModule;
import org.femtoframework.coin.CoinUtil;
import org.femtoframework.util.nutlet.NutletUtil;
import org.junit.Test;

import java.io.File;

public class SimpleCoinLookupTest {

    @Test
    public void lookupComponent() throws Exception {
        File file = NutletUtil.getResourceAsFile("examples.yaml");

        long start = System.currentTimeMillis();

        CoinModule coinModule = CoinUtil.newModule();
        CoinController controller = coinModule.getController();

        controller.create(file);

        CoinLookup lookup = coinModule.getLookup();
        lookup.lookupBean("test:first");
    }
}