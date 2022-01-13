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
            [bh.rccst.views.widget-ish :as widget-ish]))

            


(defn view
  "main view of the SPA. starts with `login`, switches to `widget-ish` after the user actually
  logs into the system
  "
  []
  (let [logged-in? (re-frame/subscribe [::subs/logged-in?])]
    (fn []
      (log/info "view" @logged-in?)

      [layout/page {:extra-classes :rccst}
       [flex/flex-panel {:height "calc(100vh - 2rem)"}
        [flex/flex-top
         [navbar/navbar]]

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
           [tech/page]]]]]])))



(comment
  (def logged-in? (atom true))
  (def logged-in? (atom false))


  ())



; some things for the repl
(comment
  [view]

  (re-frame/dispatch [::events/initialize-db])

  @re-frame.db/app-db

  (re-frame/dispatch [::events/add-to-set 7])

  ())