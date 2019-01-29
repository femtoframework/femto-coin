# coin-api

APIs for COIN container accessing

It is designed similar as [K8s API](https://kubernetes.io/docs/reference/using-api/api-concepts/)

## Prefix /coin/api/v1

- /coin is the default context path.
- /api is the path for APIs
- /v1  is the version of the API which is the KindSpec name in KindSpecFactory

### Namespaces

- GET /namespaces     - return all namespaces
- GET /namespace/NAMESPACE - return the namespace with 'NAMESPACE'

### Resources
- component
  - GET /components - return all components from all namespaces
  - GET /namespaces/NAMESPACE/components - return all components under namespace 'NAMESPACE'
  - GET /namespaces/NAMESPACE/component/NAME - return the component with given name under namespace 'NAMESPACE'
- bean
  - GET /beans - return all beans from all namespaces
  - GET /namespaces/NAMESPACE/beans - return all beans under namespace 'NAMESPACE'
  - GET /namespaces/NAMESPACE/bean/NAME - return the bean with given name under namespace 'NAMESPACE'
- spec
  - GET /specs - return all specs from all namespaces
  - GET /namespaces/NAMESPACE/specs - return all specs under namespace 'NAMESPACE'
  - GET /namespaces/NAMESPACE/spec/NAME - return the spec with given name under namespace 'NAMESPACE'
  - PATCH /namespaces/NAMESPACE/spec/NAME - patch configuration on bean spec

### Samples
```

```
### Parameters
- output=yaml|json
- limit=100
- offset=0



