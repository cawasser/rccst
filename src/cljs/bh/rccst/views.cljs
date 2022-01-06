(ns bh.rccst.views
  (:require [reagent.core :as r]
            [reagent.dom :as rdom]
            [re-frame.core :as re-frame]
            [taoensso.timbre :as log]

            [bh.rccst.events :as events]
            [bh.rccst.subs :as subs]
            [bh.rccst.views.login :as login]
            [bh.rccst.views.header-bar :as header]
            [bh.rccst.views.widget-ish :as widget-ish]))


(defn view
  "main view of the SPA. starts with `login`, switches to `widget-ish` after the user actually
  logs into the system
  "
  []
  (let [logged-in? (re-frame/subscribe [::subs/logged-in?])]
    (fn []
      (log/info @logged-in?)
      [:div
       (if @logged-in?
         [:div
          [#'header/view]
          [:div {:style {:display :flex}}
           [#'widget-ish/view "uuid-1"]
           [#'widget-ish/view "uuid-2"]]]
         [#'login/view])])))



; some things for the repl
(comment

  (re-frame/dispatch [::events/initialize-db])

  @re-frame.db/app-db

  (re-frame/dispatch [::events/add-to-set 7])

  ())