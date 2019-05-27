package org.femtoframework.coin.api;

import org.femtoframework.coin.ResourceType;
import org.femtoframework.parameters.Parameters;

public interface APIPatch {

    Parameters<Object> parsePatch(String body);

    void apply(ResourceType resourceType, Object bean, Parameters<Object> patch) throws APIPatchException;
}
