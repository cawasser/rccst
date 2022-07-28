(ns rccst.views.technologies.overview.event-modeling
  (:require [rccst.views.technologies.overview.overview :as o]))


(defn overview []
  (o/overview "Event Modeling"
    "### _Your System Over Time_

Event Modeling is a method of describing systems using an example of how information has changed within them
over time.  Specifically, this omits unimportant details, and looks at what is durable stored and what the user
sees at any particular point in time.  These moments are the 'events' on the timeline that form the description
of the system.  The real benefit of event modeling however, is that its able to describe the entire system at a
level that makes perfect sense to non-technical stakeholders, while also laying out at a very technical level exactly how
things should function.  For that reason alone its an extremely useful tool.  When all members of the project are able to
understand and refine the system using a common language/model, so much cross-team communication overhead disappears.
Below is an example image of what an event model looks like, for more information [click here](https://eventmodeling.org/posts/what-is-event-modeling/)

![event-modeling](/imgs/event-modeling-ex.jpg)

> For more information on Event Modeling, please see [EventModeling.org](https://eventmodeling.org)
    "))

