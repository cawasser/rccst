# Swagger-UI using Compojure-api and Compojure.sweet

[Swagger-UI](https://swagger.io/tools/swagger-ui/), enabled by [compojure-api](https://github.com/metosin/compojure-api) 
and [ring-swagger](https://github.com/metosin/ring-swagger) libraries, document the endpoint API, including a
UI that can be used to experiment with, and test, the API calls in the Web Browser.


## Solution

1. Add metosin/compojure-api and prismatic/schema to deps.edn so we have access to the libraries
2. Add compojure-api and schema to bh.rccst.data-source.lookup, so we can mix-in the metadata
3. Update lookup-handler to add the swagger information
   1. develop a Schema for the lookup data structure (like [here](https://github.com/cawasser/rccst/blob/33231c5503d43f39ef8d51212c0bcc87a4ea6e76/src/clj/bh/rccst/data_source/lookup.clj#L9))
4. create a new namespace: [api](https://github.com/cawasser/rccst/blob/master/src/clj/bh/rccst/data_source/api.clj)
, so we can wrap all the API "endpoints" in Swagger in a single place
   1. bh.rccst.data-source.api will mix-in all the actual API endpoint from their respective namespaces, like [this](https://github.com/cawasser/rccst/blob/33231c5503d43f39ef8d51212c0bcc87a4ea6e76/src/clj/bh/rccst/data_source/api.clj#L17)
6. [Update](https://github.com/cawasser/rccst/blob/33231c5503d43f39ef8d51212c0bcc87a4ea6e76/src/clj/bh/rccst/routes.clj#L34) 
routes to get the endpoints from bh.rccst.data-source.api

   
> Note: we may want to migrate from prismatic/schema to clojure/spec (via metosin/spec-tools) as the more modern
> solution.
> 
> Curious that Metosin hasn't implemented [malli](https://github.com/metosin/malli) as a replacement.
 

## Future Expansion

To add more endpoints, just:

1. add a new namespace to provide the schema/handler function for the route(s),
2. add the dependency to [bh.rccst.data-source.api](https://github.com/cawasser/rccst/blob/33231c5503d43f39ef8d51212c0bcc87a4ea6e76/src/clj/bh/rccst/data_source/api.clj#L3)
3. call the new handler inside the correct `context` in api, like [this](https://github.com/cawasser/rccst/blob/33231c5503d43f39ef8d51212c0bcc87a4ea6e76/src/clj/bh/rccst/data_source/api.clj#L17)
