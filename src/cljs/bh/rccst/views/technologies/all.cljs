(ns bh.rccst.views.technologies.all
  (:require [bh.rccst.views.technologies.overview.compojure :as compojure-overview]
            [bh.rccst.views.technologies.overview.component :as component-overview]
            [bh.rccst.views.technologies.overview.jackdaw :as jackdaw-overview]
            [bh.rccst.views.technologies.overview.kafka :as kafka]
            [bh.rccst.views.technologies.overview.next-jdbc :as next-jdbc-overview]
            [bh.rccst.views.technologies.overview.re-com :as re-com]
            [bh.rccst.views.technologies.overview.re-frame :as re-frame]
            [bh.rccst.views.technologies.overview.reagent :as reagent]
            [bh.rccst.views.technologies.overview.recharts :as recharts]
            [bh.rccst.views.technologies.overview.ring :as ring-overview]
            [bh.rccst.views.technologies.overview.sente :as sente-overview]
            [bh.rccst.views.technologies.overview.swagger :as swagger]
            [bh.rccst.views.technologies.overview.transit :as transit-overview]
            [bh.rccst.views.technologies.overview.woolybear :as woolybear]
            [re-com.core :as rc]
            [woolybear.ad.layout :as layout]))


(defn page []
  [layout/page {:extra-classes :is-fluid}
   [:h2.has-text-info "All"]
   [layout/text-block "An overview of many of the technologies we use in RCCST"]
   [rc/gap :size "8px"]

   [layout/frame {:extra-classes :is-fluid}
    [ring-overview/overview]]
   [layout/frame {:extra-classes :is-fluid}
    [compojure-overview/overview]]
   [layout/frame {:extra-classes :is-fluid}
    [component-overview/overview]]
   [layout/frame {:extra-classes :is-fluid}
    [sente-overview/overview]]
   [layout/frame {:extra-classes :is-fluid}
    [transit-overview/overview]]
   [layout/frame {:extra-classes :is-fluid}
    [next-jdbc-overview/overview]]
   [layout/frame {:extra-classes :is-fluid}
    [jackdaw-overview/overview]]

   [layout/frame {:extra-classes :is-fluid}
    [reagent/overview]]
   [layout/frame {:extra-classes :is-fluid}
    [re-frame/overview]]
   [layout/frame {:extra-classes :is-fluid}
    [re-com/overview]]
   [layout/frame {:extra-classes :is-fluid}
    [woolybear/overview]]
   [layout/frame {:extra-classes :is-fluid}
    [recharts/overview]]

   [layout/frame {:extra-classes :is-fluid}
    [kafka/overview]]
   [layout/frame {:extra-classes :is-fluid}
    [swagger/overview]]])

