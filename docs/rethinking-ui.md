<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**Table of Contents**  *generated with [DocToc](https://github.com/thlorenz/doctoc)*

- [Re-thinking the UI](#re-thinking-the-ui)
  - [Background](#background)
    - [Pros](#pros)
    - [Cons](#cons)
  - [What do we want?](#what-do-we-want)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

# Re-thinking the UI

## Background

[Rocky-road](https://github.com/cawasser/rocky-road) continues along the path started by [Vanilla](https://github.com/cawasser/vanilla), 
specifically ["data-sources"](https://github.com/cawasser/rocky-road/blob/master/bases/vanilla/src/rocky_road/vanilla/service_deps.clj) 
visualized by ["widgets"](https://github.com/cawasser/rocky-road/tree/master/bases/vanilla/cljs/vanilla/widgets) 
and using a subscription model. We even extend this notion to the User, in
that the User specifies the data of interest and _then_ specifies the visualization.

Data-sources include metadata describing the structural format of the information they return, which allows the 
Client software to filter the list of available widgets to those amenable to showing that kind of data, ideally
in a meaningful way. This is _not_ always the case, but in general, most of the widget style available do a 
reasonable job.

### Pros

1. Putting the selection of the data source first is a significant departure from all software developed by the 
team to-date (on previous efforts), and open some interesting possibilities.
2. The [dashboard-clj](https://github.com/multunus/dashboard-clj) underpinnings provide a "fire and forget" 
mechanism for wiring the widget to the source
3. _Single Purpose_ widgets use the built-in dashboard-clj mechanism for data subscriptions
4. We have already built a subscription management system on top of the dashboard-clj mechanism to 
support multiple clients and multiple widgets from a single client all subscribing to the same source

### Cons

1. The current implementation is quite coarse. We have two "styles" of widgets: _single purpose_, where there is one 
UI element that render the information for a single source, and _composed_ where multiple sources are combined
in a single using multiple UI elements (typically one per source, but not always).
2. _Composed_ Widgets are all custom hand-crafted, with the source being subscribed **outside** the mechanism 
built into dashboard-clj, so it is the developer's responsibility to subscribe to all the necessary sources
   1. currently, we do _NOT_ have a mechanism for terminating a subscription when a widget closes, only when the entire client disconnects
3. The dashboard-clj data sources are intended to be triggered from the built-in scheduler, although we have adapted them to 
being event-driven within the server. Unfortunately, this is all hand-crafted code; we don't have a common
mechanism for managing the push to the clients in an automatic or automated way
4. 


## What do we want?

- We'd like more composability, specifically being able to build intermediate UI components out of lower-level
UI components. For example, it would be nice to compose Widgets out of charts, tables, forms, etc.
  - let's define _components_ as the low-level elements, and _containers_ as compositions of components 
  - as such a _widget_ is a _container_ and a _pie-chart_ is a _component_ 
- We'd like the UI _components_ to handle their own subscriptions to their data source(s)
  - should a low-level UI component be able to subscribe to more than one source, or should that be a **composition** of multiple _components_?
- We'd like to have a tool, perhaps only for developers (perhaps Users too?), to visually compose the UI via drag & drop
- We'd like for the different _components_ to be able to exchange information with each other
  - what the user has selected 
  - perhaps, just within the bounds of a single _container_
- We want an "information model" (literal data structure) to define components, containers and such, up to and including 
the overall UI
  - we've done a bunch of work on this already, documented [here](https://github.com/cawasser/rocky-road/blob/master/docs/datt/user-interface.md)
  - see also [All the World's a DAG]()