(ns bh.rccst.views.technologies.systems-services
  (:require [woolybear.ad.layout :as layout]
            [woolybear.packs.flex-panel :as flex]))


(defn page []

  [layout/page
   [flex/flex-panel
    {:height "80vh"}
    [flex/flex-top
     [:div
      [:h2.has-text-info "System / Services"]
      [layout/text-block "Discuss Systems and Services here"]
      [layout/section "We use a number of technologies to provide critical system-level functionality:"]]]
    [layout/frame
     [:h2 "Kafka"]
     [layout/text-block "Discuss Kafka here"]]
    [layout/frame
     [:h2 "Swagger-UI"]
     [layout/text-block "Discuss Swagger-UI here"]]]])