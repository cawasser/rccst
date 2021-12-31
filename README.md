<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**Table of Contents**  *generated with [DocToc](https://github.com/thlorenz/doctoc)*

- [bh/rccst](#bhrccst)
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
and ["Transit"](https://github.com/cognitect/transit-clj), all the Clojure technologies we intend to 
explore and make work together.

Read all about the API [here](/docs/api/index.html).

## Future Plans

There are many things we want to learn to do, and we are trying to use this repo to learn them:

1. [X] [Combine back into a single server](/docs/single-server.md)
2. [X] [Push data from the server](/docs/data-push.md)
3. [X] [Use transit (or maybe just EDN?) to exchange ALL data between the Client and the Sever](/docs/transit.md)
4. [X] [Add Swaggerui using Compojure-api](/docs/swagger-ui.md)
5. [X] Get logging to work correctly (added ch.qos.logback/logback-classic "1.2.5", see [here](https://spin.atomicobject.com/2015/05/11/clojure-logging/))
6. [ ] [Develop a more complex API (paving the way to Rocky-road)](/docs/complex-api.md)
7. [X] [Anti-forgery support](/docs/anti-forgery.md)
   1. [X] Toggle anti-forgery OFF when doing development
      2. [X] Server-side
      3. [X] Do some testing in "production mode"
8. [X] Add :edn format to the swagger-UI to make it easier to work with
9. [X] [Add a "real" database](/docs/database.md)
   1. [X] Postgres
   2. [X] SQLite
10. [ ] Implement more of the API:
    1. [ ] Fix Users
       1. [ ] [Login]()
       2. [ ] [Register]()
    2. [ ] Widgets/Layouts
    3. [ ] Comms
    4. [ ] Sensors/Beams
11. [ ] Get "repl reload" working again
12. [X] Docstrings
13. [X] Defaults for system configuration parameters
    1. [X] Webserver port
    2. [X] nRepl port
    3. [X] Broadcast timeout
14. [X] [Codox](https://github.com/weavejester/codox) (weavejester comes through again!)
15. [ ] Remove the "broadcast" (Concept22 doesn't really support this notion of the server publishing on a timer)
16. [ ] [De-complecting things](/docs/decompleting.md)
17. [ ] [Re-thinking the UI](/docs/rethinking-ui.md)
18. [ ] (optional) Consider moving to [Reitit](/docs/routing.md)

## Getting Started

We have successfully converted this 2-part app back into just having a single server at port 8280. The startup and development
instructions must change accordingly. You still use 2 CLI tools to develop the app, but there is only
ony webpage now.

### Client

The client is built using [shadow-cljs](https://shadow-cljs.github.io/docs/UsersGuide.html). It can be run 
at the terminal using:

    shadow-cljs watch :app

ONce the Server is running, you can open a browser to:

[http://localhost:8280/](http://localhost:8280/)

The client also hosts an [nRepl](https://nrepl.org/nrepl/index.html) at port 8777.

Additional detail on working with the client are [below](#development)

### Server

The server is build using the [Clojure CLI](https://clojure.org/guides/deps_and_cli) and can be started at the terminal with:

    clojure -M:run

If you prefer, you can start the server at a local repl. Configure you IDE to start a repl; don't
worry about any aliases, there aren't any at this time.

Then open src/clj/bh/rccst/core.clj. Load the file into the open repl and then begin evaluating

### Swagger-UI

The app now provides a swagger-ui page, so you can interactively try out all the endpoint calls (GETs and POSTs).
Just open a new browser tab to:

[http://localhost:8280/api-docs](http://localhost:8280/api-docs)

And have a ball.

> Note: You must provide any inputs in "transit over json" [format](https://github.com/cognitect/transit-format#ground-and-extension-types). 
> See [here](https://github.com/cognitect/transit-format) for more details.

## Project Details 

See [here](/docs/project-details.md) for more detailed information (caveats apply) 

