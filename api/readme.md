# coin-api

APIs for COIN container accessing

It is designed similar as [K8s API](https://kubernetes.io/docs/reference/using-api/api-concepts/)

## Prefix /coin/api/v1

- /coin is the default context paths.
- /api is the paths for APIs
- /v1  is the version of the API which is the KindSpec name in KindSpecFactory

### Namespaces

- GET /namespaces     - Returns all namespaces
- GET /namespaces/NAMESPACE - Returns the namespace with 'NAMESPACE'

### Resources
- component
  - GET /components - Returns all components from all namespaces
  - GET /namespaces/NAMESPACE/components - Returns all components under namespace 'NAMESPACE'
  - GET /namespaces/NAMESPACE/components/NAME - Returns the component with given name under namespace 'NAMESPACE'
- spec
  - GET /specs - Returns all specs from all namespaces
  - GET /namespaces/NAMESPACE/specs - Returns all specs under namespace 'NAMESPACE'
  - GET /namespaces/NAMESPACE/specs/NAME - Returns the spec with given name under namespace 'NAMESPACE'
- config
  - GET /configs - Returns all configs from all namespaces
  - GET /namespaces/NAMESPACE/configs - Returns all configs under namespace 'NAMESPACE'
  - GET /namespaces/NAMESPACE/configs/NAME - Returns the config with given name under namespace 'NAMESPACE'
- info
  - GET /infos - Returns all infos from all namespaces
  - GET /namespaces/NAMESPACE/infos - Returns all infos under namespace 'NAMESPACE'
  - GET /namespaces/NAMESPACE/infos/NAME - Returns the info with given name under namespace 'NAMESPACE'
- bean
  - GET /beans - Returns all beans from all namespaces
  - GET /namespaces/NAMESPACE/beans - Returns all beans under namespace 'NAMESPACE'
  - GET /namespaces/NAMESPACE/beans/NAME - Returns the bean with given name under namespace 'NAMESPACE'
  - PATCH /namespaces/NAMESPACE/beans/NAME - Takes one action on the resource
  
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

Content-Type: application/strategic-merge-patch+json
[Strategic Merge Patch](https://github.com/kubernetes/community/blob/master/contributors/devel/sig-api-machinery/strategic-merge-patch.md)


- PATCH /namespaces/NAMESPACE/beans/NAME - Takes one action on the resource
```$yaml
PATCH /coin/api/v1/namespaces/cron/beans/test
minute: 10         #setMinute(int)
```

```$yaml
PATCH /coin/api/v1/namespaces/cron/beans/test
$invoke/doSomeThing1:
  - value1
  - value2           #doSomeThing1(String, String)
```

```$yaml
PATCH /coin/api/v1/namespaces/cron/beans/test
$invoke/doSomeThing2:
  - value           #doSomeThing2(String)
```