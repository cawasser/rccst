# Using Transit (or EDN) for everything

Our goal is to be able to exchange full-fidelity Clojure data structures between the 
Clients and the "data" Server. We use Clojure [sets]() especially throughout Rocky-road and
having to "play games" to get this data across a JSON-only exchange is both a pain and error-prone.

> Note: JSON does NOT include a set literal type, therefore and Clojure sets get converted into just
> an array/vector and there is no way to have an automated conversion back into a Clojure set.