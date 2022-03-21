<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**Table of Contents**  *generated with [DocToc](https://github.com/thlorenz/doctoc)*

- [Testing Approach](#testing-approach)
  - [What to Test](#what-to-test)
    - [Practicality of "Test Everything"](#practicality-of-test-everything)
    - [Potential Waste in "Test Everything"](#potential-waste-in-test-everything)
    - [So, What _Should_ We Be Testing?](#so-what-_should_-we-be-testing)
    - [Components](#components)
    - [Routes](#routes)
    - [API](#api)
      - [URL Handlers](#url-handlers)
    - [Data Sources](#data-sources)
  - [Example-based Testing](#example-based-testing)
  - [Generative Testing](#generative-testing)
    - [Approach to Testing "Subsystems"](#approach-to-testing-subsystems)
    - [How Do We](#how-do-we)
  - [Testing UI (ClojureScript) Code](#testing-ui-clojurescript-code)
  - [Test Runners](#test-runners)
  - [Learn More](#learn-more)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

# Testing Approach

## What to Test

The conventional advice is to "test everything", but that is both not practical, and often very wasteful.

### Practicality of "Test Everything"

It isn't practical in that, as an application grows, the amount of code to perform tests grows as well. This isn't usually
a problem in the kinds of "toy examples" typical in tutorials or other training materials, but it needs to be seriously 
considered when building any real production system.

On the other hand, a large, complex production system is just the kind of thing to benefit greatly from having a thorough
unit testing approach, because large volumes of code tend to harbor large volumes of bugs.

### Potential Waste in "Test Everything"

In many cases, especially when developing in Clojure, functions are of such simple design and implementation, that _inspection_
can be sufficient to ensure bug-free code, at least for those kinds of functions. Adding tests just to have tests for "everything"
really only adds bulk to the system (every function has a test function, doubling code size - or more). Clojure developed
can (_should?_) also take advantage of the [Repl](https://clojure.org/guides/repl/introduction) when developing code in the first 
place, with the goals of both reducing the complexity of the design and implementation, and in handling the _normal_ and 
_boundary_ conditions typically testing using [example-based](#example-based-testing) testing.

Another, more subtle problem with the "Test Everything" mindset is discussed by Stu Halloway in his [Narcissistic Design]()
presentation, namely the use of tests to "expand the scope of functionality." It's a small step from "I expect these values" to 
"I expect _only_ these values", "I _must_ check for missing values", "I _must_ check for extra values", etc. Each of these
_"perfectly reasonable"_ enhancements adds to the test code, and also makes the system itself ***less*** flexible. (See Rich
Hickey's 2016 Clojure/conj presentation [Spec-ulation](https://www.youtube.com/watch?v=oyLBGkS5ICk)). Each of these tests codifies
the implementation in terms of "bug or not" actually changing the _design_ itself.

Clojure also supports idiomatic [Nil-punning](https://lispcast.com/nil-punning/), or using `nil` as a marker for a variety of meanings, 
while also having many (most?) functions accept `nil` as a valid input and just returning `nil`. Yes, at some point that `nil`
probably needs to be turned into something meaningful for an end-user of the system, but throughout most of the internal of a system,
`nil` can just be passed around without raising any error conditions. Nil-punning significantly reduces the number of places
in the system implementation that can create error, also reducing the amount of testing needed.


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

Let's look at the kinds of things each of these categories is supposed to provide, how they might fail,
and how we might test for these failures.

### Components

Components are functional "wrappers" around the either very stateful or Java-object-based to manage the ["lifecycle 
and dependencies of software components which have runtime state"](https://github.com/stuartsierra/component#component).
A hash-map of configuration is passed in, and that map is then augmented with any internal state or additional configuration
created by the `start` function.

Since this is mainly taking configuration and executing Component start-up, there are few error-states. The Component gets
created correctly, or everything fails. Main error scenario is _not_ providing require configuration information to the 
Component.

Testing could be done at the individual Component level, looking for the additional keys in the hash-map inside the `component/System`
value returned, as well as at the combined [`system-map`](https://github.com/cawasser/rccst/blob/9ee9df705e28a381053b8456fa3808e330afd65d/src/clj/bh/rccst/core.clj#L128)
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

In these cases, other than testing something like a _query_, the result of a call would depend on the state of the external
source. In other words, these functions are ***not*** pure, or Referentially Transparent. They can, and usually _do_, return
different values every time they are called. Controlling this variability will be key to writing any meaningful tests for 
Data Source. This typically requires a special "test database" containing only known values, or special "test data" mixed
in with production-type data.

Using a separate test database is fairly straightforward,as most testing libraries provide some mechanism for setting
program state (like which database to use) prior to executing the test code (called _setup_) and cleaning up these special
mechanisms when the test is complete (called _teardown_). Mixing test data in with "production data" is easier (no need 
for testing-specific setup or teardown), but much more error-prone, in that we'd _not_ want end-users to see the test data.

Therefore, for any testing ([example](#example-based-testing), or [generative](#generative-testing)) of Data Sources, we will 
construct a separate "testing database" that will be used for ***ALL***.

> ***Q: Should we use SQLite as the "testing database" and keep Postgres for production?***
> 
> ###### Pros
> ***
> - Simple. We already support both, so all the configuration code is written.
> 
> ###### Cons
> ***
> - We really aren't testing the same thing, since the production system will use Postgres. It has different syntax, semantics and
> failure modes

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

> Also known as "Property-based testing"

Rather than requiring developers to hand-craft specific example, what if we could have computer itself 
generate the test cases? That's the basic premise of "generative testing", to write test ins such a 
way that the computer is doing most of the work.

This allows us ot test a much wider space of inputs to a function that would be possible using 
[example-based testing](#example-based-testing).

Clojure, specifically [clojure.spec](https://clojure.org/guides/spec), as a means of defining the parameters and
return values of functions, specifications (hence "spec") that can be used by [_generators_](https://clojure.org/guides/spec#_generators)
ot produce pseudo-random values for use in testing, vis [test.check](https://github.com/clojure/test.check).

Generative testing can be used to test "computational" functions over wide range of inputs, especially of kind that no human
develop would consider. It is also provides a good mechanism for testing more complex state-managing "subsystems".

### Approach to Testing "Subsystems"

A subsystem, in this case, is typically a single namespace of functions which manage some type of state, such as a database table or
key/value store. Perhaps even something like a Kafka topic. 

The basic approach is:
1. Develop the "simple model" for the "subsystem under test" (SUT) (i.e., the function or collection of function in a single namespace)"
   1. is there some simple, built-in, data structure that mimics to state-management properties
2. Define the _properties_ of the SUT
   1. define the properties of the inputs
   2. define the properties of the outputs
   3. define the relationship between the inputs and output of each function
3. Build generator(s) to create sequences of operations
4. Perform the operations on both the SUT and the model and compare the results
   1. if they match, SUCCESS
   2. if not, FAILURE


### How Do We 



> Note: Currently (as of v0.2.1) RCCST does _not_ use clojure.spec. The closest we come is our use of 
> [plumatic/schema](https://github.com/plumatic/schema) as part of our route handler specifications.
> 
> See [database.users](https://github.com/cawasser/rccst/blob/b0fe0ac6ff10a0e2983f811b1ed005234c2dda21/src/clj/bh/rccst/components/database/users.clj#L37) and 
> [api.login](https://github.com/cawasser/rccst/blob/b0fe0ac6ff10a0e2983f811b1ed005234c2dda21/src/clj/bh/rccst/api/login.clj#L46) 
> for examples.

## Testing UI (ClojureScript) Code


## Test Runners


## Learn More

- [Example-based Unit Testing in Clojure - Eric Normand](https://purelyfunctional.tv/mini-guide/example-based-unit-testing-in-clojure/)
- [Generative testing in Clojure - James Trunk](https://www.youtube.com/watch?v=u0TkAw8QqrQ) - this uses a different library [simple-check]()
but the concepts are similar