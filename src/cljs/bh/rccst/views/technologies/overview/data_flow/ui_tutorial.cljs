(ns bh.rccst.views.technologies.overview.data-flow.ui-tutorial
  (:require [woolybear.ad.layout :as layout]))



(defn tutorial []
  [layout/frame {:extra-classes :is-fluid}
   [:h2.has-text-info "Let's Build One!"]
   [layout/markdown-block
    ""]])

