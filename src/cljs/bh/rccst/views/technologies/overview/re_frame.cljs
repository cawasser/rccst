(ns bh.rccst.views.technologies.overview.re-frame
  (:require [bh.rccst.views.technologies.overview.overview :as o]))

(defn overview []
      (o/overview "Re-frame"

"#### A framework for building Modern Web Apps in ClojureScript.\n
re-frame is a ClojureScript framework for building user interfaces. It has a data-oriented, functional design.
It's primary focus is on high programmer productivity and scaling up to larger Single-Page applications.
It is about data, and the functions which transform that data. And, because it is a reactive framework,
 data coordinates functions, not the other way around.\n\n
re-frame puts all application state into one place, which is called *app-db*. The data in app-db can be given a
strong schema so that, at any moment, we can validate all the data in the application.\n\n
\n\n **Data Loop:**  re-frame implements \"a perpetual loop\". To build an app, you hang pure functions on
certain parts of this loop, and re-frame looks after the conveyance of data around the loop, into and out of the transforming
functions you provide.\n\n Each iteration of the re-frame loop has 6 stages, and these stages happen one after another. These
stages are: \n\n

  1. **Event dispatch:** re-frame is *event driven*. An event is sent when something happens -
  the user clicks a button, or a websocket receives a new message.
  2. **Event handling:** In response to an event, an application must decide what action to take. This is known as event handling.
  event handlers are just functions which compute data, and that data describes what needs to happen.
  3. **Effect handling:** In this step, the *side effects*, calculated by the previous step, are actioned. *effects* move
  the app forward. Without them, an app stays stuck in one state forever, never achieving anything.
  4. **Query:** It is a *Signal Graph* which runs query functions on the app state, efficiently computing reactive,
  multi-layered, \"materialised views\" of it.
  5. **View:** It is many *ViewFunctions* (aka Reagent components) which collectively render the UI of the application.
  Each *ViewFunction* renders part of the whole. These functions compute and return data in a format called hiccup which represents DOM.
  6. **DOM:** This is the step in which the hiccup-formatted \"descriptions of required DOM\", returned by the *ViewFunctions* of
  previous stage, are actioned. The browser DOM nodes are mutated.\n\n

See also:
>[re-frame](https://day8.github.io/re-frame/) \n\n"
"/imgs/re-frame-logo.png"))


