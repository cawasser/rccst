(ns rccst.views.molecule.all
  "Catalog and demonstrations of general components."
  (:require [taoensso.timbre :as log]
            [rccst.views.molecule.example.header-bar :as header-bar]))


(defn catalog
  []

  [:div
   [header-bar/example]])
