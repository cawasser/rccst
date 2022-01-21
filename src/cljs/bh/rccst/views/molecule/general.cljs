(ns bh.rccst.views.molecule.general
  "Catalog and demonstrations of general components."
  (:require [taoensso.timbre :as log]
            [bh.rccst.views.molecule.example.header-bar :as header-bar]
            [bh.rccst.views.molecule.example.login :as login]))


(defn catalog
  []

  [:div
   [header-bar/example]
   [login/example]])
