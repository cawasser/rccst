# Using Transit (or EDN) for everything

Our goal is to be able to exchange full-fidelity Clojure data structures between the 
Clients and the "data" Server. We use Clojure [sets]() especially throughout Rocky-road and
having to "play games" to get this data across a JSON-only exchange is both a pain and error-prone.

> Note: JSON does NOT include a set literal type, therefore and Clojure sets get converted into just
> an array/vector and there is no way to have an automated conversion back into a Clojure set.


## Step 1

The simplest way to see if we are passing Clojure data is to just modify the existing message sent by
bh.rccst.broadcast/broadcast!, namely, let's just add another field ot the message to hold a set of
the last 3 values for :i that we've sent. See [bh.rccst.broadcast](https://github.com/cawasser/rccst/blob/master/src/clj/bh/rccst/broadcast.clj)

## Step 2

Now we need to add at least one ["endpoint"](https://study.com/academy/lesson/what-is-web-service-endpoint-definition-concept.html) 
for the client to "query" such that the response contains some Clojure data collections, namely sets and 
hash-maps.

Let's call the new endpoint `/lookup`. We'll add an "FX" handler to bh.rccst.events, and we'll also need
to add a dependency on [day8.re-frame/http-fx](https://github.com/Day8/re-frame-http-fx).

