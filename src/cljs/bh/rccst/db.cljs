(ns bh.rccst.db
  (:require [re-frame.core :as re-frame]

            [bh.rccst.views :as views]
            [bh.rccst.views.catalog :as catalog]
            [bh.rccst.views.technologies :as tech]
            [bh.rccst.views.catalog.example.navbar :as navbar]))


(def default-db
  {:name           "RCCST"
   :version        ""
   :logged-in?     false
   :sources        {:number 0
                    :string "empty"}
   :nav-bar        views/init-db
   :catalog        catalog/init-db
   :tech           tech/init-db
   :navbar-example navbar/init-db})


(re-frame/reg-event-fx
  ::initialize-db
  (fn [_ _]
    {:dispatch [::get-version]
     :db       default-db}))