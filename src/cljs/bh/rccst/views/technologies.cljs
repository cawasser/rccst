(ns bh.rccst.views.technologies
  (:require [re-frame.core :as re-frame]
            [woolybear.ad.buttons :as buttons]
            [woolybear.ad.layout :as layout]
            [woolybear.packs.flex-panel :as flex]
            [woolybear.packs.tab-panel :as tab-panel]

            [bh.rccst.events :as events]
            [bh.rccst.ui-component.navbar :as navbar]
            [bh.rccst.views.technologies.clojure :as tech-clj]
            [bh.rccst.views.technologies.clojurescript :as tech-cljs]
            [bh.rccst.views.technologies.systems-services :as s-s]
            [bh.rccst.views.technologies.all :as all]))


(def data-path [:tech :tab-panel])

(def init-db
  {:tab-panel (tab-panel/mk-tab-panel-data data-path :tech/server)})

(re-frame/reg-sub
  :db/tech
  (fn [db _]
    (:tech db)))

(re-frame/reg-sub
  :tech/tab-panel
  :<- [:db/tech]
  (fn [tech]
    (:tab-panel tech)))

(re-frame/reg-sub
  :tech/value
  :<- [:tech/tab-panel]
  (fn [tab-panel]
    (:value tab-panel)))


(def tech-navbar [[:tech/server "Server-side"]
                  [:tech/client "Client-side"]
                  [:tech/system "System / Services"]
                  [:tech/all "All"]])


(defn page
  "Some background on the various technologies we are using"
  []
  (re-frame/dispatch-sync [::events/init-locals :tech init-db])

  [layout/page {:extra-classes :is-fluid}
   ;[layout/page-header {:extra-classes :is-fluid}
   ; [:h1.has-text-info "Technology Overview"]]

   [flex/flex-panel {:extra-classes :is-fluid
                     :height "85vh"}
    [flex/flex-top
     [navbar/navbar tech-navbar [:tech/tab-panel]]]

    [layout/page-body {:extra-classes :is-fluid}
     [tab-panel/tab-panel {:extra-classes             :is-fluid
                           :subscribe-to-selected-tab [:tech/value]}

      [tab-panel/sub-panel {:extra-classes :is-fluid :panel-id :tech/server}
       [tech-clj/page]]

      [tab-panel/sub-panel {:extra-classes :is-fluid :panel-id :tech/client}
       [tech-cljs/page]]

      [tab-panel/sub-panel {:extra-classes :is-fluid :panel-id :tech/system}
       [s-s/page]]

      [tab-panel/sub-panel {:extra-classes :is-fluid :panel-id :tech/all}
       [all/page]]]]]])

