# femto-coin
A tiny JSR330 standard Inversion Of Control container.  COIN stands for "New Inversion Of Control".  Actually,the code was done in 2005, there was no IOC standard at that time, and refined to support JSR330 in 2014. Now in micro service, serverless and kubernetes' world, it is still useful for server side service development. That's the reason I make it open source.



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
### @JsonAlias
### @JsonProperty
### @JsonSetter
### @JsonGetter
### @JsonCreator
### @JsonIgnore
### @JsonIgnoreProperties

## Spring
### @Component
### @Service
### @Controller
### @Repository
### @Autowired
### @Configuration
### @Bean


## Doesn't support
### @Qualifier

