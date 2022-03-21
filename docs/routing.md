<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**Table of Contents**  *generated with [DocToc](https://github.com/thlorenz/doctoc)*

- [Routing](#routing)
  - [Compojure](#compojure)
    - [Pros](#pros)
    - [Cons](#cons)
  - [Reitit](#reitit)
    - [Pros](#pros-1)
    - [Cons](#cons-1)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

# Routing

Currently, we are using [Compojure](https://github.com/weavejester/compojure) (and some supporting libraries) 
for routing requests (GET, POST, etc.) to the functions that provide the appropriate processing.

But Compojure is not the only possibility here.

Would it be worth converting to [Reitit](https://github.com/metosin/reitit)?


## Compojure

Compojure is probably the "grand-daddy" of routing libraries in Clojure. It provides a simple syntax for
defining routes and wiring them to their handler functions.

With additional libraries, we can also get data definition, checking, and coercion; API documentation, including
Swagger-UI; and integration with [Sente](https://github.com/ptaoussanis/sente)

### Pros

1. Well known, lots of examples
2. Straightforward syntax

### Cons

1. Built from Macros, so all the routes must be defined at compile-time
2. Because it is all macros, the routes cannot be examined or manipulated at run-time


## Reitit

Reitit, developed by Metosin (who also provide a number of libraries for working with Compojure), is 
a data-driven routing solution, meaning that we use Clojure data structures to define the routes
and wire them to the handler functions.

### Pros

1. Data-driven, so Reitit ca be considered more flexible than Compojure
   1. specifically in that we can use all of clojure.core to manipulate, combine, filter, etc. the routes
   2. and that the data structure exists outside of program execution
      1. i.e., we can examine it even if the program is _NOT_ running
2. Bi-directional, so we can find a URL from the handler and vice versa

### Cons

1. less well-known than Compojure, fewer examples
2. we don't know it (how do we implement middleware? how does sente fit? etc.?)

