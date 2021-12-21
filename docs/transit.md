<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**Table of Contents**  *generated with [DocToc](https://github.com/thlorenz/doctoc)*

- [Using Transit (or EDN) for everything](#using-transit-or-edn-for-everything)
  - [Step 1](#step-1)
  - [Step 2](#step-2)
  - [Solution](#solution)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

# Using Transit (or EDN) for everything

Our goal is to be able to exchange full-fidelity Clojure data structures between the 
Clients and the "data" Server. We use Clojure [sets](https://clojure.org/reference/data_structures#Sets) 
especially throughout Rocky-road and having to "play games" to get this data across a JSON-only exchange 
is both a pain and error-prone.

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

## Solution

Again, this is pretty easy once we understand how all the libraries work together:

1. The Client needs to make the request with the `:response-format` set to [`(ajax/transit-response-format)`](https://github.com/cawasser/rccst/blob/3f6ffcc50b6594ff4686474d21d910eac91c0fd2/src/cljs/bh/rccst/events.cljs#L81). 
This ensures that it will be able to parse al the Clojure data we get back from the server.
2. The Server needs to separate the "websocket" routes from the "general" routes because they need different processing. See [here](https://github.com/cawasser/rccst/blob/3f6ffcc50b6594ff4686474d21d910eac91c0fd2/src/clj/bh/rccst/routes.clj#L19).
3. We also need a handler for the "/lookup" call, so we've added bh.rccst.data-source.lookup. It has one function `lookup-function` which
returns the complex Clojure data structure wrapped in the "application/transit+json" content-type. This converts the Clojure data into JSON using
the transit marshalling approach.
4. We need to add a few dependencies to shadow-cljs.edn and deps.edn to support these calls and middlewares.

> Note: we should look into wrapping all the "endpoint" calls inside the "application/transit+json" content-type at once
> so we can't forget to do it on each one