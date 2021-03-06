<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**Table of Contents**  *generated with [DocToc](https://github.com/thlorenz/doctoc)*

- [bh/rccst](#bhrccst)
  - [Things We've Already Done](#things-weve-already-done)
  - [Current Activities](#current-activities)
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

There are lots of things we wanted to learn more about, and we've [done many of them](/docs/already-done.md)


A current thrust for RCCST is the development of a catalog of reusable UI components, and a data-driven
mechanism for composing them into complex user interfaces to solve real-world problems for users.


![](/docs/figures/catalog-screenshot2.png)
Figure 1: A sample of our UI Catalog

## Current Activities 

1. [ ] [Re-think the UI](/docs/rethinking-ui.md)
    1. [X] more composition and better control over layout -> [re-com](https://github.com/Day8/re-com)
    2. [X] something to help alignment, grouping, etc. ... [fork?](https://github.com/luciodale/fork)
        1. [ ] can it be used for simply _displaying_ content, not just editing?
        2. [X] re-com??
    3. [X] is it better to just use CSS-styled Reagent components?
    4. [ ] how do we handle CSS for everything?
       1. [ ] [Sass](https://sass-lang.com)?
       2. [ ] what about conflict between libraries (re-com/bootstrap & wollybear/bulma)?
    5. [ ] what about [woolybear](https://github.com/cawasser/woolybear)?
        1. [X] catalog page
            1. [X] we should probably migrate the catalog into RCCST (i.e., out of woolybear)
            2. [X] make 'examples' namespaces for each example chunk
            3. [X] can we add Markdown to the examples for the descriptions?
        2. [ ] what are atoms?
           1. [ ] Re-com
           2. [ ] Woolybear
           3. [X] color picker?
              1. [X] [algorithm](https://ux.stackexchange.com/questions/107318/formula-for-color-contrast-between-text-and-background) to set text-color based on picker-color
           4. [ ] 3d & 2d globes
        3. [ ] what are the molecules? (or they still just atoms?)
           1. [X] login
           2. [X] charts inside popup/popover (see [here](http://worrydream.com/#!/MediaForThinkingTheUnthinkable))
           3. [ ] sidebar
           4. [ ] diagram
           5. [ ] toolbar
           6. [ ] user/app settings
           7. [ ] hamburger menu
           8. [ ] widget
              1. [ ] title-bar
              2. [ ] frame w/resizer
           9. [ ] widget-maker (or is this a template? or template builder?)
2. [ ] refactor
   1. [ ] rccst -> black-hammer for all UI components
      1. [ ] catalog -> atoms
      2. [ ] woolybear
      3. [ ] 3d-globe
      4. [ ] 2d-globe
   2. [ ] ui-component wrappers in re-com style
      1. [ ] woolybear components
      2. [ ] color-pickers
      3. [ ] diagrams
      4. [ ] cards
3. [ ] complete/clean-up catalog example "show code"
   1. [ ] Layout / Layout Grid
   2. [ ] Cards / Card
   3. [ ] Cards / Flippable Card
   4. [ ] Misc / 3d globe
   5. [ ] Misc / 2d globe
4. [ ] complete/cleanup molecule example 'code'
   1. [ ] general / header-bar
   2. [ ] general / login
5. [ ] build a better "source code" tool, so it shows the exact code as the component is actually configured in the catalog example




## Future Plans

But there are still many things we want to do/learn:

1. [ ] [Kafka](/docs/kafka.md) - how should we integrate this with Component?
   1. component per topology?
   2. component for kafka + "sub"component per topology?
   3. how should [Kafka, Pub-Sub and DataSource](/docs/kafka-pub-sub-datasource.md) all work together?
   4. also, can use Component to manage things? (see [rp-jackdaw-cljs](https://github.com/rentpath/rp-jackdaw-clj))
2. [ ] How do DataSources communicate with the:
   1. [ ] Client?
      1. [ ] core.async channel(s)? possible as a separate Component
   2. [ ] Kafka?
      1. [ ] core.async which looks/acts like an output `topic`?
3. [ ] Work out an automated [testing approach](/docs/testing.md)
    1. [X] ***FAILED** (fixtures don't work this way) -> open the database in a :once fixture, and the drop!/create! is the :every fixture
    2. [ ] how do (should we?) we test the UI? Can we take advantage of the Catalog?
4. [ ] [Develop a more complex API (paving the way to Rocky-road)](/docs/complex-api.md)
   1. [ ] should we introduce DataSources as a component (like rocky-road.dashboard-clj.data-source)?
5. [ ] Implement more of the API:
   1. [ ] Fix Users
       1. [ ] [Login]()
       2. [ ] [Register]()
   2. [ ] Widgets/Layouts
   3. [ ] Comms
   4. [ ] Sensors/Beams
6. [ ] Add metrics collection to the Server
   1. [ ] [statsd](https://github.com/statsd/statsd), via [clojure-statsd-client](https://github.com/unbounce/clojure-dogstatsd-client)
7. [ ] (optional) Consider moving to [Reitit](/docs/routing.md) (see item 13.3 above)
8. [ ] Will we ever need `websocket.handler`? (handles push messages from clients)





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

