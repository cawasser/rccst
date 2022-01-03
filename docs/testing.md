# Testing Approach

## What to Test

The conventional advice is to "test everything", but that is both not practical, and often very wasteful.

### Practicality of "Test Everything"

It isn't practical in that, as an application grows, the amount of code to perform tests grows as well. This isn't usually
a problem in the kinds of "toy examples" typical in tutorials or other training materials, but it needs to be seriously 
considered when building any real production system.

On the other hand, a large, complex production system is just the kind of thing to benefit greatly form having a thorough
unit testing approach, because large volumes of code tend to harbor large volumes of bugs.

### Potential Waste in "Test Everything"

In many cases, especially when developing in Clojure, functions are o such simple design and implementation, that _inspection_
can be sufficient to ensure bug-free code, at least for those kinds of functions. Adding tests just to have tests for "everything"
really only adds bulk to the system (every function has a test function, doubling code size - or more). Clojure developed
can (_should?_) also take advantage of the [Repl]() when developing code in the first place, with the goals of both reducing the
complexity of the design and implementation, and in handling the _normal_ and _boundary_ conditions typically testing using
[example-based](#example-based-testing).

Another, more subtle,problem with the "Test Everything" mindset is discussed by Stu Halloway in his [Narcissistic Design]()
presentation, namely the use of test to "expand the scope of functionality." It's a small step from "I expect these values" to 
"I expect _only_ these values", "I _must_ check for missing values", "I _must_ check or extra values", etc. Each of these
_"perfectly reasonable"_ extension adds to the test code, and also make the system itself ***less*** flexible. (See Rich
Hickey's 2016 Clojure/conj presentation [Spec-ulation](https://www.youtube.com/watch?v=oyLBGkS5ICk)). Each of these test codifies
the implementation in terms of "bug or not", and changing ho the function works requires also changing the tests.

Clojure support idiomatic [Nil-punning](https://lispcast.com/nil-punning/), or using `nil` as a marker for a variety of meanings, 
while also having many (most?) functions accept `nil` as a valid input and just returning `nil`. Yes, at some point that `nil`
probably needs to be turned into something meaningful for an end-user of the system, but throughout most of the internal of a system,
`nil` can just be passed around without raising any error conditions.


### So, What _Should_ We Be Testing?

Currently, the system has a number of major elements (components, namespaces, etc.):

- Components
  - Broadcast (for now)
  - Database
  - Repl
  - Subscribers
  - Webserver
  - Websocket
- Routes
- Api
  - Login
  - Subscribe
  - Version
  - etc.
- Data Sources
  - Lookup (for now)
  - etc.

Let's look at the kinds of things each of these categories is supposed to provide, and how they might fail,
and how we might test for these failures.

### Components

Components are functional "wrappers" around the either very stateful or Java-object-based to manage the ["lifecycle 
and dependencies of software components which have runtime state"](https://github.com/stuartsierra/component#component).
A hash-map of configuration is passed in, and that map is then augmented with any internal state or additional configuration
created by the `start` function.

Testing could be done at the individual Component level, looking for the additional keys in the hash-map inside the `component/System`
value returned, as well as at the combine [`system-map`](https://github.com/cawasser/rccst/blob/9ee9df705e28a381053b8456fa3808e330afd65d/src/clj/bh/rccst/core.clj#L128)
level.

### Routes

The [`routes`](/src/bh/rccst/routes) namespace consists of a single public function, also called [`routes`](/src/bh/rccst/routes/routes.clj) 
whose sole purpose is to construct the Ring handler chain for the API, while also defining all the middleware "wrappers" 
to simplify handler processing of HTML requests. As such, `routes` returns the fairly opaque [Ring handler](https://www.baeldung.com/clojure-ring#3-handlers)
[`compojure.core/routes`](https://github.com/weavejester/compojure/blob/a22e0fec1da92d2a5bddaf5b9428b7e495bf36b9/src/compojure/core.clj#L187), which
is really just another (Higher-order) function.

There is little we can test here other than confirming that the `routes/routes` function worked at all.

> Note: If we switch to [Reitit](https://github.com/metosin/reitit) for specifying route handlers, we should revisit 
> the unit testing need. Reitit defined routes not as Macros (i.e., functions) but as _data_.

### API

Like the [`routes`](#routes), [`api/api`](https://github.com/cawasser/rccst/blob/9ee9df705e28a381053b8456fa3808e330afd65d/src/clj/bh/rccst/api/api.clj#L36)
returns a Ring Handler function created by the macro [`compojure-sweet/api`](https://github.com/metosin/compojure-api/blob/f6ac0e9a5e65b8661e3224899682f34546363fb4/src/compojure/api/api.clj#L19).

There is really nothing we can test here, as the returned handler is opaque.

#### URL Handlers

Testing of the individual URL handlers, usually one function per URL(1), _can_ be done, but if they access stateful items (and
that's really their whole job), setting up repeatable test cases will be required, and could become cumbersome.

> (1) it's possible to have one function service multiple URLs via the parameters.

### Data Sources

"Data Source" are a general term for functions that support the route handlers of Pub/Sub data pushes by interfacing with the
state that comprises the system. For example, the `lookup` namespace defines a dummy hash-map that is the value that will be 
returned inside the response to a GET on `/lookup`. In such cases there is little to test. 

More likely are functions that interface with:

- the Database, specifically some table or tables inside the database
- some other state-management system, such as Kafka

In these cases, other than testing something like a _query_

## Example-based Testing

This is what most developers mean when they say "unit testing": testing with specific examples
encoded directly in tests. Typically, these examples test:

- normal conditions
- boundary conditions
  - missing values
  - out-of-range
  - incorrect types
  - etc.
- error conditions
  - handling unavailable third-party dependencies
  - etc.

In general, this is good, and far superior to not doing _any_ testing, but developing a good set
of examples can be tricky, even for experience developers. And there are subtle bugs, like memory leaks,
timing or race conditions, and others, that only occur under very specific conditions that a human
develop is unlikely to even consider, let alone develop an example test for.

To help guard against these kinds of bug, we can turn to _Generative Testing_.

> See [Heisenbug](https://en.wikipedia.org/wiki/Heisenbug)

## Generative Testing

Rather than requiring developers to hand-craft specific example, what if we could have computer itself generate the test cases?
That's the basic premise of "generative testing."



## Testing UI (ClojureScript) Code


## Test Runners


## Learn More

[Example-based Unit Testing in Clojure - Eric Normand](https://purelyfunctional.tv/mini-guide/example-based-unit-testing-in-clojure/)
[Generative testing in Clojure - James Trunk](https://www.youtube.com/watch?v=u0TkAw8QqrQ)