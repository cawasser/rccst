(ns bh.rccst.views.technologies.clojurescript
  (:require [woolybear.ad.layout :as layout]
            [woolybear.packs.flex-panel :as flex]))


(defn page []

  [layout/page
   [flex/flex-panel
    {:height "80vh"}
    [flex/flex-top
     [:div
      [:h2.has-text-info "Clojurescript"]
      [layout/text-block "Discuss Clojurescript here"]
      [layout/section "We use a number of libraries to provide critical client-side functionality:"]]]
    [layout/frame
     [:h2 "Reagent"]
     [layout/text-block "Discuss Reagent here"]]
    [layout/frame
     [:h2 "Re-frame"]
     [layout/text-block "Discuss Re-frame here"]]
    [layout/frame
     [:h2 "Re-com"]
     [layout/text-block "Discuss Re-com here"]]
    [layout/frame
     [:h2 "Woolybear"]
     [layout/text-block "Discuss Woolybear here"]]]])
