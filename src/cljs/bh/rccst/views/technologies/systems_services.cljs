(ns bh.rccst.views.technologies.systems-services
  (:require [woolybear.ad.layout :as layout]
            [woolybear.ad.images :as images]
            [woolybear.packs.flex-panel :as flex]
            [bh.rccst.views.technologies.overview.kafka :as kafka]
            [bh.rccst.views.technologies.overview.swagger :as swagger]))

(defn description []
      [:div
        [layout/markdown-block
"### Event Modeling
Event Modeling is a method of describing systems using an example of how information has changed within them
over time.  Specifically, this omits unimportant details, and looks at what is durable stored and what the user
sees at any particular point in time.  These moments are the 'events' on the timeline that form the description
of the system.  The real benefit of event modeling however, is that its able to describe the entire system at a
level that makes perfect sense to non-technical stakeholders, while also laying out at a very technical level exactly how
things should function.  For that reason alone its an extremely useful tool.  When all members of the project are able to
understand and refine the system using a common language/model, so much cross-team communication overhead disappears.
Below is an example image of what an event model looks like, for more information [click here](https://eventmodeling.org/posts/what-is-event-modeling/)"]
        [images/image {:src "/imgs/event-modeling-ex.jpg"}]
        [layout/markdown-block
"### Microservices
Microservices - aka the microservice architecture - is an architecture style that structures a system as a collection of
services that are: highly maintainable and testable, loosely coupled, independently deployable, organized around business
capabilities, and owned by a small team.  Microservices enable the rapid, frequent, and reliable delivery of large,
complex applications.  It also allows for technological innovation and trying out new ideas on a much smaller and easier
to work with scale.  Breaking a stovepipe system up into easily manageable microservices allows for the system to have
greater overall flexibility, while making development and maintenance easier and faster.

### System Technologies"]])


(defn page []

  [layout/page
   [flex/flex-panel
    {:height "80vh"}
    [flex/flex-top
     [:div
      [:h2.has-text-info "System / Services"]
      [description]
      [layout/section "We use a number of technologies to provide critical system-level functionality:"]]]
    [layout/frame
     [kafka/overview]]
    [layout/frame
     [swagger/overview]]]])
