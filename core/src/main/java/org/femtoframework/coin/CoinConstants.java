package org.femtoframework.coin;

/**
 * Coin Constants
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public interface CoinConstants {

    String NAME_MODULE = "module";
    String NAME_CONTROLLER = "service";
    String NAME_LOOKUP = "lookupBean";
    String NAME_VARIABLE_RESOLVER_FACTORY = "variable_resolver_factory";
    String NAME_CONFIGURATOR_FACTORY = "configurator_factory";
    String NAME_DEFAULT_COMPONENT_FACTORY = "default_component_factory";
    String NAME_LIFECYCLE_STRATEGY = "lifecycle_strategy";
    String NAME_NAMESPACE_FACTORY = "namespace_factory";
    String NAME_KIND_SPEC_FACTORY = "kind_spec_factory";
    String NAME_RELOADER = "reloader";

    String NAMESPACE_COIN = "coin";

    String NAMESPACE_NAMESPACE = "namespace";

    String NAMESPACE_DEFAULT = "default";


    String COMPONENT_YAML = "META-INF/spec/component.yaml";
    String APPLICATION_YAML = "META-INF/spec/application.yaml";

    char CHAR_DELIM = '.';
    char CHAR_COLON = ':';
    char CHAR_SLASH = '/';

    /**
     * Kind
     */
    String KIND = "kind";

    String METADATA = "metadata";

    String NAMESPACE = "namespace";

    String LABELS = "labels";

    String ANNOTATIONS = "annotations";

    /**
     * If it uses "name" in the spec, that means the name could be part of spec and it also could inject to bean
     */
    String NAME = "name";

    String SPEC = "spec";

    String API_VERSION = "apiVersion";


    String VERSION_K8S_V1 = "v1";
    String VERSION_COIN_V1 = "coin/v1";

    String URI = "uri";

    String INTERFACES = "interfaces";

    String BEAN = "bean";

    String REMOTE = "remote";

    String COMPONENT = "component";

    String REMOTE_COMPONENT = "RemoteComponent";

    String CONFIG = "config";

    String CLASS = "class";

    String LABEL_DEFAULT = "default";

    String LABEL_ENABLED = "enabled";

    String LABEL_ACCESS = "access";

    String LABEL_SINGLETON = "singleton";

    /**
     * Specify the component is default for specify interface
     */
    String ANNOTATIONS_DEFAULT_FOR = "defaultFor";

    /**
     * Link the current bean to target beans
     */
    String LABEL_BELONGS_TO = "belongsTo";
}
