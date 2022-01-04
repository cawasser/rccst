<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**Table of Contents**  *generated with [DocToc](https://github.com/thlorenz/doctoc)*

- [bh/rccst](#bhrccst)
  - [Things We've Already Done](#things-weve-already-done)
  - [Future Plans](#future-plans)
  - [Getting Started](#getting-started)
    - [Client](#client)
    - [Server](#server)
    - [Swagger-UI](#swagger-ui)
  - [Project Details](#project-details)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

> Note: to update the ToC here and in all the markdown files, install [doctoc](https://github.com/thlorenz/doctoc) and run:
>`doctoc .`


# bh/rccst

A [re-frame](https://github.com/day8/re-frame) application to explore using 
[Sente](https://github.com/ptaoussanis/sente) for server-side push of data updates to connected 
(and subscribed) clients.

RCCST stands for ["Ring"](https://github.com/ring-clojure/ring), 
["Component"](https://github.com/stuartsierra/component), 
["Compojure"](https://github.com/weavejester/compojure),
["Sente"](https://github.com/ptaoussanis/sente) 
and ["Transit"](https://github.com/cognitect/transit-clj), the major Clojure technologies we intend to 
explore and make work together.

Read all about the API [here](/docs/api/index.html).

## Things We've Already Done

There are lots of things we wanted to learn more about, and we've done that:

1. [X] [Combine back into a single server](/docs/single-server.md)
2. [X] [Push data from the server](/docs/data-push.md)
3. [X] [Use transit (or maybe just EDN?) to exchange ALL data between the Client and the Sever](/docs/transit.md)
4. [X] [Add Swaggerui using Compojure-api](/docs/swagger-ui.md)
5. [X] Get logging to work correctly (added ch.qos.logback/logback-classic "1.2.5", see [here](https://spin.atomicobject.com/2015/05/11/clojure-logging/))
6. [X] [Anti-forgery support](/docs/anti-forgery.md)
    1. [X] Toggle anti-forgery OFF when doing development
       1. [X] Server-side
       2. [X] Do some testing in "production mode"
7. [X] Add :edn format to the swagger-UI to make it easier to work with
8. [X] [Add a "real" database](/docs/database.md)
    1. [X] Postgres
    2. [X] SQLite
9. [X] Add [reveal](https://vlaaad.github.io/reveal/) tooling
10. [X] Docstrings
11. [X] Defaults for system configuration parameters
    1. [X] Webserver port
    2. [X] nRepl port
    3. [X] Broadcast timeout
12. [X] [Codox](https://github.com/weavejester/codox) (weavejester comes through again!)
13. [X] Get "repl reload" working again
    1. [X] start/stop/reset Component/System
    2. [X] modify URL handlers
    3. [X] (**FAILED**) add/remove URLs and handlers (requires a server bounce because Compojure is MACRO-based)
14. [X] version support using [metav](https://github.com/jgrodziski/metav)
15. [X] what does [cloverage](https://github.com/cloverage/cloverage) actually do, and how do we interpret the results?
    1. see [here](https://blog.jeaye.com/2016/12/29/clojure-test-coverage/)



## Future Plans

But there are still many things we want to do/learn:

1. [ ] [De-complect (or at least simplify) things](/docs/decompleting.md)
   1. [ ] get rid of `websocket.publish`, since we already have `publish!` in `component.subscribers`
      1. [ ] what is the calling mechanism? (public function wrapping `@(system/system)`?), see rich comments
      2. [ ] maybe subscribers should _use_ publish?
   2. [ ] why do we have `websocket.handler`?
   3. [ ] refactor `routes/routes`
   4. [ ] Remove "Broadcast" (Concept22 doesn't really support this notion of the server publishing on a timer)
   5. [ ] split into api // data-source
      1. [ ] version
      2. [ ] lookup
2. [ ] [Develop a more complex API (paving the way to Rocky-road)](/docs/complex-api.md)
   1. [ ] should we introduce DataSources as a component (like rocky-road.dashboard-clj.data-source)?
3. [ ] Implement more of the API:
   1. [ ] Fix Users
      1. [ ] [Login]()
      2. [ ] [Register]()
   2. [ ] Widgets/Layouts
   3. [ ] Comms
   4. [ ] Sensors/Beams
4. [ ] [Kafka](/docs/kafka.md) - how should we integrate this with Component?
   1. component per topology?
   2. component for kafka + "sub"component per topology?
5. [ ] [Re-think the UI](/docs/rethinking-ui.md)
6. [ ] Work out an automated [testing approach](/docs/testing.md)
   1. [ ] open the database in a :once fixture, and the drop!/create! is the :every fixture
7. [ ] Add metrics collection to the Server
   1. [ ] [statsd](https://github.com/statsd/statsd), via [clojure-statsd-client](https://github.com/unbounce/clojure-dogstatsd-client)
8. [ ] (optional) Consider moving to [Reitit](/docs/routing.md) (see item 13.3 above)
9. [ ] do we (can we?) need to stop the database connection using next.jdbc?


## Getting Started

We have successfully converted this 2-part app back into just having a single server at port 8280. The startup and development
instructions must change accordingly. You still use 2 CLI tools to develop the app, but there is only
ony webpage now.

### Client

The client is built using [shadow-cljs](https://shadow-cljs.github.io/docs/UsersGuide.html). It can be run 
at the terminal using:

    shadow-cljs watch :app

Once the Server is running, you can open a browser to:

[http://localhost:8280/](http://localhost:8280/)

The client also hosts an [nRepl](https://nrepl.org/nrepl/index.html) at port 8777.

Additional detail on working with the client are [below](#development)

### Server

The server is build using the [Clojure CLI](https://clojure.org/guides/deps_and_cli) and can be started at the terminal with:

    clojure -M:run

If you prefer, you can start the server at a local repl. Configure you IDE to start a repl. You can user the
`reveal` alias to provide support for `(tap> ...)` using the very nice [reveal]() tool.

Then open 

    src/clj/bh/rccst/core.clj 

Load the file into the open repl and begin evaluating.

### Swagger-UI

The app now provides a swagger-ui page, so you can interactively try out all the endpoint calls (GETs and POSTs).
Just open a new browser tab to:

[http://localhost:8280/api-docs](http://localhost:8280/api-docs)

And have a ball.

> Note 1: You must provide any inputs in "transit over json" [format](https://github.com/cognitect/transit-format#ground-and-extension-types). 
> See [here](https://github.com/cognitect/transit-format) for more details.

> Note 2: You must also be running the server in _dev-mode_, ie.e, _without_ CSRF Anti-forgery support.

## Project Details 

See [here](/docs/project-details.md) for more detailed information (caveats apply) 

