(ns bh.rccst.views
  (:require
   [re-frame.core :as re-frame]
   [bh.rccst.subs :as subs]))


(defn main-panel []
  (let [name (re-frame/subscribe [::subs/name])]
    [:div
     [:h1
      "Hello from " @name]]))

