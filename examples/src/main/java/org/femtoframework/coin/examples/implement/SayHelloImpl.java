package org.femtoframework.coin.examples.implement;

import lombok.Getter;
import lombok.Setter;
import org.femtoframework.coin.annotation.Action;
import org.femtoframework.coin.examples.service.SayHello;

/**
 * CALL Action
 *
 * method: PATCH
 * URL:    http://127.0.0.1:9169/coin/api/v1/namespace/coin-examples/bean/helloworld?_action=action&_name=sayHello&1=Sheldon
 *
 * Configuration:
 * method: PATCH
 * URL:   http://127.0.0.1:9169/coin/api/v1/namespace/coin-examples/bean/helloworld?_action=set&_property=response&_value=Hello
 */
public class SayHelloImpl implements SayHello {

    @Getter
    @Setter
    private String response = "Hello";

    @Override
    @Action
    public String sayHello(String message) {
        return response + " " + message + "!";
    }
}
