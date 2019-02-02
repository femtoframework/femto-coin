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
  - GET /namespaces/NAMESPACE/components - Returns all components under namespace 'NAMESPACE'
  - GET /namespaces/NAMESPACE/component/NAME - Returns the component with given name under namespace 'NAMESPACE'
- bean
  - GET /beans - Returns all beans from all namespaces
  - GET /namespaces/NAMESPACE/beans - Returns all beans under namespace 'NAMESPACE'
  - GET /namespaces/NAMESPACE/bean/NAME - Returns the bean with given name under namespace 'NAMESPACE'
  - PATCH /namespaces/NAMESPACE/bean/NAME - Takes one action on the resource
- spec
  - GET /specs - Returns all specs from all namespaces
  - GET /namespaces/NAMESPACE/specs - Returns all specs under namespace 'NAMESPACE'
  - GET /namespaces/NAMESPACE/spec/NAME - Returns the spec with given name under namespace 'NAMESPACE'
  - PATCH /namespaces/NAMESPACE/spec/NAME - Applies patch configuration on bean spec

### Samples
```

```
### Parameters
- output=yaml|json
- limit=100
- offset=0



