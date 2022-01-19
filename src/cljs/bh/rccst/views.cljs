(ns bh.rccst.views
  (:require [reagent.core :as r]
            [reagent.dom :as rdom]
            [re-frame.core :as re-frame]
            [taoensso.timbre :as log]
            [re-com.core :as rc]

            [woolybear.ad.containers :as containers]
            [woolybear.ad.layout :as layout]
            [woolybear.packs.flex-panel :as flex]
            [woolybear.packs.tab-panel :as tab-panel]

            [bh.rccst.events :as events]
            [bh.rccst.subs :as subs]
            [bh.rccst.ui-component.navbar :as navbar]
            [bh.rccst.views.catalog :as catalog]
            [bh.rccst.views.login :as login]
            [bh.rccst.views.header-bar :as header]
            [bh.rccst.views.technologies :as tech]
            [bh.rccst.views.widget-ish :as widget-ish]
            [bh.rccst.views.giants :as giants]))


(def data-path [:nav-bar :tab-panel])

(def init-db
  {:tab-panel (tab-panel/mk-tab-panel-data data-path :nav-bar/login)})


(def main-navbar [[:nav-bar/login "Login"]
                  [:nav-bar/catalog "'Atoms'"]
                  [:nav-bar/widget-ish "'Molecules'"]
                  [:nav-bar/tech "Technologies"]
                  [:nav-bar/giants "'Giants'"]])

(re-frame/reg-sub
  :db/nav-bar
  (fn [db _]
    (:nav-bar db)))

(re-frame/reg-sub
  :nav-bar/tab-panel
  :<- [:db/nav-bar]
  (fn [n]
    (:tab-panel n)))

(re-frame/reg-sub
  :nav-bar/selected-tab
  :<- [:nav-bar/tab-panel]
  (fn [tab-panel]
    (:value tab-panel)))


(defn view
  "main view of the SPA. starts with `login`, switches to `widget-ish` after the user actually
  logs into the system
  "
  []
  (re-frame/dispatch-sync [::events/init-locals :nav-bar init-db])

  (let [logged-in? (re-frame/subscribe [::subs/logged-in?])]
    (fn []
      (log/info "view" @logged-in?)

      [layout/page {:extra-classes :rccst}
       [flex/flex-panel {:height "calc(100vh - 2rem)"}
        [flex/flex-top
         [navbar/navbar main-navbar [:nav-bar/tab-panel]]]

        [layout/page-body {:extra-classes :rccst}
         [tab-panel/tab-panel {:extra-classes             :rccst
                               :subscribe-to-selected-tab [:nav-bar/selected-tab]}

          [tab-panel/sub-panel {:panel-id :nav-bar/login}
           [#'login/view]]

          [tab-panel/sub-panel {:panel-id :nav-bar/widget-ish}
           [rc/v-box :src (rc/at)
            :children [[#'header/view]
                       [rc/gap :size "5px"]
                       [rc/h-box :src (rc/at)
                        :gap "15px"
                        :children [[#'widget-ish/view "uuid-1"]
                                   [#'widget-ish/view "uuid-2"]
                                   [#'widget-ish/view "uuid-3"]]]]]]

          [tab-panel/sub-panel {:panel-id :nav-bar/catalog}
           [catalog/page]]

          [tab-panel/sub-panel {:panel-id :nav-bar/tech}
           [tech/page]]

          [tab-panel/sub-panel {:panel-id :nav-bar/giants}
           [#'giants/view]]]]]])))


