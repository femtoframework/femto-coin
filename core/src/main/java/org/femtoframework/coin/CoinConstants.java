package org.femtoframework.coin;

/**
 * Coin Constants
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public interface CoinConstants {

    String NAME_MODULE = "module";
    String NAME_CONTROLLER = "controller";
    String NAME_LOOKUP = "lookupBean";
    String NAME_VARIABLE_RESOLVER_FACTORY = "variable_resolver_factory";
    String NAME_CONFIGURATOR_FACTORY = "configurator_factory";
    String NAME_LIFECYCLE_STRATEGY = "lifecycle_strategy";
    String NAME_NAMESPACE_FACTORY = "namespace_factory";
    String NAME_KIND_SPEC_FACTORY = "kind_spec_factory";

    String NAMESPACE_COIN = "coin";

    String NAMESPACE_NAMESPACE = "namespace";

    String DEFAULT_NAMESPACE = "default";


    String COMPONENT_YAML = "META-INF/spec/component.yaml";
    String APPLICATION_YAML = "META-INF/spec/application.yaml";

    char CHAR_DELIM = '.';
    char CHAR_COLON = ':';
    char CHAR_SLASH = '/';
}
