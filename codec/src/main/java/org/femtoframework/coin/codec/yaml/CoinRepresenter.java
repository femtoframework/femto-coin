package org.femtoframework.coin.codec.yaml;

import org.femtoframework.coin.info.BeanInfoFactory;
import org.yaml.snakeyaml.representer.Representer;

public class CoinRepresenter extends Representer {

    public CoinRepresenter(BeanInfoFactory beanInfoFactory) {
        setPropertyUtils(new CoinPropertyUtils(beanInfoFactory));
    }
}
