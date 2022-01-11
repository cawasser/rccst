(ns bh.rccst.db
  (:require [woolybear.ad.catalog :as catalog]

            [bh.rccst.ui-component.navbar :as nav-bar]))


(def default-db
  {:name "re-frame"
   :version ""
   :logged-in? false
   :sources {:number 0
             :string "empty"}
   :nav-bar nav-bar/init-db
   :catalog catalog/init-db})
