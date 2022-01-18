(ns bh.rccst.db
  (:require [bh.rccst.ui-component.navbar :as nav-bar]
            [bh.rccst.views.catalog :as catalog]
            [bh.rccst.views.technologies :as tech]

            [bh.rccst.views.catalog.example.navbar :as navbar]))


(def default-db
  {:name           "RCCST"
   :version        ""
   :logged-in?     false
   :sources        {:number 0
                    :string "empty"}
   :nav-bar        nav-bar/init-db
   :catalog        catalog/init-db
   :tech           tech/init-db
   :navbar-example navbar/init-db})
