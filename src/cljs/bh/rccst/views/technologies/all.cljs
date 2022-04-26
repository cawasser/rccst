(ns bh.rccst.views.technologies.all
  (:require [woolybear.ad.layout :as layout]
            [woolybear.packs.flex-panel :as flex]

            [bh.rccst.views.technologies.overview.ring :as ring-overview]
            [bh.rccst.views.technologies.overview.compojure :as compojure-overview]
            [bh.rccst.views.technologies.overview.component :as component-overview]
            [bh.rccst.views.technologies.overview.sente :as sente-overview]
            [bh.rccst.views.technologies.overview.transit :as transit-overview]
            [bh.rccst.views.technologies.overview.next-jdbc :as next-jdbc-overview]
            [bh.rccst.views.technologies.overview.jackdaw :as jackdaw-overview]
            [bh.rccst.views.technologies.overview.reagent :as reagent]
            [bh.rccst.views.technologies.overview.re-frame :as re-frame]
            [bh.rccst.views.technologies.overview.re-com :as re-com]
            [bh.rccst.views.technologies.overview.woolybear :as woolybear]
            [bh.rccst.views.technologies.overview.recharts :as recharts]
            [bh.rccst.views.technologies.overview.kafka :as kafka]
            [bh.rccst.views.technologies.overview.swagger :as swagger]))




(defn page []
  [layout/page {:extra-classes :is-fluid}
   [flex/flex-panel {:extra-classes :is-fluid
                     :height "80vh"}
    [flex/flex-top  {:extra-classes :is-fluid}
     [:div.is-fluid
      [:h2.has-text-info "All"]
      [layout/text-block "An overview of many of the technologies we use in RCCST"]
      [layout/section]]]

    [ring-overview/overview]
    [compojure-overview/overview]
    [component-overview/overview]
    [sente-overview/overview]
    [transit-overview/overview]
    [next-jdbc-overview/overview]
    [jackdaw-overview/overview]

    [reagent/overview]
    [re-frame/overview]
    [re-com/overview]
    [woolybear/overview]
    [recharts/overview]

    [kafka/overview]
    [swagger/overview]]])

