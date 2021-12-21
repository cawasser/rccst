# Swagger-UI using Compojure-api and Compojure.sweet

[Swagger-UI](https://swagger.io/tools/swagger-ui/), enabled by [compojure-api](https://github.com/metosin/compojure-api) 
and [ring-swagger](https://github.com/metosin/ring-swagger) libraries, document the endpoint API, including a
UI that can be used to experiment with, and test, the API calls in the Web Browser.


## Solution

1. Add metosin/compojure-api and prismatic/schema to deps.edn so we have access to the libraries
2. Add compojure-api and schema to bh.rccst.data-source.lookup, so we can mix-in the metadata
3. Update lookup-handler to add the swagger information
   1. develop a Schema for the lookup data structure (see [here]())
4. create a new namespace: [api](), so we can wrap all the API "endpoints" in Swagger in a singe place
   1. bh.rccst.data-source.api will mix-in all the actual API endpoint from their respective namespaces, like [this]()
5. [Update]() 
routes to get the endpoints from bh.rccst.data-source.api

   
> Note: we may want to migrate from prismatic/schema to clojure/spec (via metosin/spec-tools) as the more modern
> solution.
> 
> Curious that Metosin hasn't implemented [malli](https://github.com/metosin/malli) as a replacement.
 

## Future Expansion

To add more endpoints, just:

1. add a new namespace to provide the schema/handler function for the route(s),
2. add the dependency to [bh.rccst.data-source.api]()
3. call the new handler inside the correct `context` in api, like [this]()
