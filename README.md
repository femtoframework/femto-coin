# femto-coin
A tiny cloud native [Inversion of Control(IoC) and the Dependency Injection(DI)](https://www.martinfowler.com/articles/injection.html) framework.  COIN stands for "New Inversion Of Control". 
Actually,the code was done in 2005, there was no IOC standard at that time, and refined to support JSR330 in 2014. Now in micro service, server-less and kubernetes' world, it is still useful for server side service development. That's the reason I make it open source.

# Top 3 reasons why femto-coin makes java micro-service easier

### No. 1 LESS is MORE
It supports limit dependency injections what are good enough for micro-service
### No. 2 KISS Keep it simple, stupid
It only has 7 dependencies includes slf4j.
### No. 3 FAST It takes only 500ms to launch a tiny service.

# Namespaces
## special namespaces
### e: System.env
### p: System.properties
### r: Remote service
### c: Configurators
### s: Bean Specs



# Annotations
## JSR330 (javax.inject)
### @Inject
### @Named
### @Singleton

## javax/annotation Common Annotations
### ManagedBean
### Resource
### Resources
### PostConstruct
### PreDestroy

## com/fasterxml/jackson/annotation
### @JsonProperty
### @JsonSetter
### @JsonGetter
### @JsonIgnore
### @JsonIgnoreProperties

## Does not support
### @Qualifier

