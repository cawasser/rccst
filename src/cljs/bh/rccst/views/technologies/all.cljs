(ns bh.rccst.views.technologies.all
  (:require [woolybear.ad.layout :as layout]
            [woolybear.packs.flex-panel :as flex]

            [bh.rccst.views.technologies.overview.ring :as ring]))


(defn page []
  [layout/page
   [flex/flex-panel
    {:height "80vh"}
    [flex/flex-top
     [:div
      [:h2.has-text-info "All"]
      [layout/text-block "An overview of many of the technologies we use in RCCST"]]]

    [layout/section
     [ring/overview]]]])