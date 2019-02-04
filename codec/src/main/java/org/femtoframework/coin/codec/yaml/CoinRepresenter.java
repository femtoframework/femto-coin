package org.femtoframework.coin.codec.yaml;

import org.femtoframework.coin.CoinModule;
import org.femtoframework.coin.info.BeanInfoFactory;
import org.yaml.snakeyaml.representer.Representer;

public class CoinRepresenter extends Representer {

    private BeanInfoFactory beanInfoFactory;

    public CoinRepresenter(CoinModule module) {
        this.beanInfoFactory = module.getBeanInfoFactory();
        setPropertyUtils(new CoinPropertyUtils(beanInfoFactory));
    }
}
