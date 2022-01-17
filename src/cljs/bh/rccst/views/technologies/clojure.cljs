(ns bh.rccst.views.technologies.clojure
  (:require [woolybear.ad.layout :as layout]
            [woolybear.packs.flex-panel :as flex]

            [bh.rccst.views.technologies.overview.ring :as ring-overview]
            [bh.rccst.views.technologies.overview.compojure :as compojure-overview]))


(defn page []

  [layout/page
   [flex/flex-panel
    {:height "80vh"}
    [flex/flex-top
     [:div
      [:h2.has-text-info "Clojure"]
      [layout/markdown-block "Discuss [Clojure](https://clojure.org) here"]
      [layout/section "We use a number of libraries to provide critical server-side functionality:"]]]

    [ring-overview/overview]
    [compojure-overview/overview]

    [layout/frame
     [:h2 "Component"]
     [layout/markdown-block "Discuss [Component](https://github.com/stuartsierra/component) here"]]
    [layout/frame
     [:h2 "Sente"]
     [layout/markdown-block "Discuss [Sente](https://github.com/ptaoussanis/sente) here"]]
    [layout/frame
     [:h2 "Transit"]
     [layout/markdown-block "Discuss [Transit](https://github.com/cognitect/transit-clj) here"]]
    [layout/frame
     [:h2 "next.jdbc"]
     [layout/markdown-block "Discuss [next.jdbc](https://github.com/seancorfield/next-jdbc) here"]]
    [layout/frame
     [:h2 "Jacdaw"]
     [layout/markdown-block "Discuss [Jackdaw](https://github.com/FundingCircle/jackdaw) here"]]]])
