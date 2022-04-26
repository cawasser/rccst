(ns bh.rccst.views.technologies.systems-services
  (:require [bh.rccst.views.technologies.overview.event-modeling :as event-modeling]
            [bh.rccst.views.technologies.overview.kafka :as kafka]
            [bh.rccst.views.technologies.overview.micro-services :as micro-services]
            [bh.rccst.views.technologies.overview.re-frame-10x :as re-frame-10x]
            [bh.rccst.views.technologies.overview.swagger :as swagger]
            [woolybear.ad.layout :as layout]
            [woolybear.packs.flex-panel :as flex]))


(defn page []

  [layout/page {:extra-classes :is-fluid}
   [flex/flex-panel {:extra-classes :is-fluid
                     :height        "80vh"}
    [flex/flex-top {:extra-classes :is-fluid}
     [:div.is-fluid
      [:h2.has-text-info "System / Services"]
      [layout/markdown-block
       "We use a number of techniques, technologies, and tools to provide critical system-level functionality:

> For a short video summary of event-driven design as the backbone of a microservice architecture in Clojure, [click here](https://www.youtube.com/watch?v=ZEcjEHYs0zo)

"]]]
    [layout/frame {:extra-classes :is-fluid}
     [event-modeling/overview]]
    [layout/frame {:extra-classes :is-fluid}
     [micro-services/overview]]
    [layout/frame {:extra-classes :is-fluid}
     [kafka/overview]]
    [layout/frame {:extra-classes :is-fluid}
     [swagger/overview]]
    [layout/frame {:extra-classes :is-fluid}
     [re-frame-10x/overview]]]])
