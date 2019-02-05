# coin-api

APIs for COIN container accessing

It is designed similar as [K8s API](https://kubernetes.io/docs/reference/using-api/api-concepts/)

## Prefix /coin/api/v1

- /coin is the default context paths.
- /api is the paths for APIs
- /v1  is the version of the API which is the KindSpec name in KindSpecFactory

### Namespaces

- GET /namespaces     - Returns all namespaces
- GET /namespace/NAMESPACE - Returns the namespace with 'NAMESPACE'

### Resources
- component
  - GET /components - Returns all components from all namespaces
  - GET /namespace/NAMESPACE/components - Returns all components under namespace 'NAMESPACE'
  - GET /namespace/NAMESPACE/component/NAME - Returns the component with given name under namespace 'NAMESPACE'
- spec
  - GET /specs - Returns all specs from all namespaces
  - GET /namespace/NAMESPACE/specs - Returns all specs under namespace 'NAMESPACE'
  - GET /namespace/NAMESPACE/spec/NAME - Returns the spec with given name under namespace 'NAMESPACE'
  - PATCH /namespace/NAMESPACE/spec/NAME - Applies patch configuration on bean spec
- config
  - GET /configs - Returns all configs from all namespaces
  - GET /namespace/NAMESPACE/configs - Returns all configs under namespace 'NAMESPACE'
  - GET /namespace/NAMESPACE/config/NAME - Returns the config with given name under namespace 'NAMESPACE'
  - PATCH /namespace/NAMESPACE/config/NAME - Applies patch properties on config
- info
  - GET /infos - Returns all infos from all namespaces
  - GET /namespace/NAMESPACE/infos - Returns all infos under namespace 'NAMESPACE'
  - GET /namespace/NAMESPACE/info/NAME - Returns the info with given name under namespace 'NAMESPACE'
- bean
  - GET /beans - Returns all beans from all namespaces
  - GET /namespace/NAMESPACE/beans - Returns all beans under namespace 'NAMESPACE'
  - GET /namespace/NAMESPACE/bean/NAME - Returns the bean with given name under namespace 'NAMESPACE'
  - PATCH /namespace/NAMESPACE/bean/NAME - Takes one action on the resource
  
NAME could be the name of the component or multiple paths for child component.
For examples,
- first  The component on first level
- first/second  Means "second" child component under "first" component

### Samples
```

```
### Parameters
- output=yaml|json
- limit=100
- offset=0

### PATCH
- _action Action of this PATCH
  - "action" Invoke @Action method
  - "set" Invoke setter method
- _name
  - actionName @Action method name
  - property  Property Name for setter
- Other arguments
  - spec PATCH
    - {_action:"set", _property:"property", _value:"NewPropertyValue"} 
  - config PATCH
    - {_action:"set", _property:"property", _value:"NewPropertyValue"}
  - bean PATCH
    - {_action:"set", _property:"property", _value:"NewPropertyValue"}
    - {_action:"action", _name:"doSomeThing1", "1":"value1", "2":"value2"}
    - {_action:"action", _name:"doSomeThing2", value:"ArgumentValue"}


