(ns bh.rccst.views.technologies.clojure
  (:require [woolybear.ad.layout :as layout]
            [woolybear.packs.flex-panel :as flex]

            [bh.rccst.views.technologies.overview.ring :as ring-overview]
            [bh.rccst.views.technologies.overview.compojure :as compojure-overview]
            [bh.rccst.views.technologies.overview.component :as component-overview]
            [bh.rccst.views.technologies.overview.sente :as sente-overview]
            [bh.rccst.views.technologies.overview.transit :as transit-overview]
            [bh.rccst.views.technologies.overview.next-jdbc :as next-jdbc-overview]
            [bh.rccst.views.technologies.overview.jackdaw :as jackdaw-overview]))

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
    [component-overview/overview]
    [sente-overview/overview]
    [transit-overview/overview]
    [next-jdbc-overview/overview]
    [jackdaw-overview/overview]]])
