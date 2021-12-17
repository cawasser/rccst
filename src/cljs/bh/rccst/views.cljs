(ns bh.rccst.views
  (:require
   [re-frame.core :as re-frame]
   [bh.rccst.subs :as subs]
   [bh.rccst.events :as events]))


(defn main-panel []
  (let [name (re-frame/subscribe [::subs/name])
        s (re-frame/subscribe [::subs/set])]
    (fn []
      [:div
       [:h1 "Hello from " @name]
       [:h3 (str @s)]])))


; some things for the repl
(comment
  (re-frame/dispatch [::events/initialize-db])

  @re-frame.db/app-db

  (re-frame/dispatch [::events/add-to-set 7])

  ())