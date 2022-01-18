(ns bh.rccst.views.technologies.clojurescript
  (:require [woolybear.ad.layout :as layout]
            [woolybear.packs.flex-panel :as flex]
            [bh.rccst.views.technologies.overview.reagent :as reagent]
            [bh.rccst.views.technologies.overview.re-frame :as re-frame]))


(defn page []

  [layout/page
   [flex/flex-panel
    {:height "80vh"}
    [flex/flex-top
     [:div
      [:h2.has-text-info "Clojurescript"]
      [layout/markdown-block "Discuss [Clojurescript]() here"]
      [layout/section "We use a number of libraries to provide critical client-side functionality:"]]]
    [layout/frame
     [reagent/overview]]
    [layout/frame
     [re-frame/overview]
     [:h2 "Re-frame"]
     [layout/markdown-block "Discuss [Re-frame](https://day8.github.io/re-frame/) here"]]
    [layout/frame
     [:h2 "Re-com"]
     [layout/markdown-block "Discuss [Re-com](https://github.com/Day8/re-com) here"]]
    [layout/frame
     [:h2 "Woolybear"]
     [layout/markdown-block "Discuss [Woolybear](https://github.com/manutter51/woolybear) here"]]
    [layout/frame
     [:h2 "Recharts"]
     [layout/markdown-block "Discuss [Recharts](https://recharts.org/en-US) here"]]]])
