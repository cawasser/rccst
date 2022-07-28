(ns rccst.views.technologies.all
  (:require [rccst.views.technologies.overview.compojure :as compojure-overview]
            [rccst.views.technologies.overview.component :as component-overview]
            [rccst.views.technologies.overview.jackdaw :as jackdaw-overview]
            [rccst.views.technologies.overview.kafka :as kafka]
            [rccst.views.technologies.overview.next-jdbc :as next-jdbc-overview]
            [rccst.views.technologies.overview.re-com :as re-com]
            [rccst.views.technologies.overview.re-frame :as re-frame]
            [rccst.views.technologies.overview.reagent :as reagent]
            [rccst.views.technologies.overview.recharts :as recharts]
            [rccst.views.technologies.overview.ring :as ring-overview]
            [rccst.views.technologies.overview.sente :as sente-overview]
            [rccst.views.technologies.overview.swagger :as swagger]
            [rccst.views.technologies.overview.transit :as transit-overview]
            [rccst.views.technologies.overview.woolybear :as woolybear]
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

