(ns bh.rccst.views.technologies.systems-services
  (:require [woolybear.ad.layout :as layout]
            [woolybear.ad.images :as images]
            [woolybear.packs.flex-panel :as flex]
            [bh.rccst.views.technologies.overview.event-modeling :as event-modeling]
            [bh.rccst.views.technologies.overview.micro-services :as micro-services]
            [bh.rccst.views.technologies.overview.kafka :as kafka]
            [bh.rccst.views.technologies.overview.swagger :as swagger]
            [bh.rccst.views.technologies.overview.re-frame-10x :as re-frame-10x]))


(defn page []

  [layout/page
   [flex/flex-panel
    {:height "80vh"}
    [flex/flex-top
     [:div
      [:h2.has-text-info "System / Services"]
      [layout/section "We use a number of techniques, technologies, and tools to provide critical system-level functionality:"]]]
    [layout/frame
     [event-modeling/overview]]
    [layout/frame
     [micro-services/overview]]
    [layout/frame
     [kafka/overview]]
    [layout/frame
     [swagger/overview]]
    [layout/frame
     [re-frame-10x/overview]]]])
