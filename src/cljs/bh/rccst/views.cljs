(ns bh.rccst.views
  (:require [re-frame.core :as re-frame]
            [taoensso.timbre :as log]

            [woolybear.ad.layout :as layout]
            [woolybear.packs.flex-panel :as flex]
            [woolybear.packs.tab-panel :as tab-panel]

            [bh.rccst.subs :as subs]
            [bh.rccst.ui-component.navbar :as navbar]
            [bh.rccst.views.catalog :as catalog]
            [bh.rccst.views.molecules :as molecules]
            [bh.rccst.views.technologies :as tech]
            [bh.rccst.views.widget-ish :as widget-ish]
            [bh.rccst.views.giants :as giants]

            [bh.rccst.ui-component.tabbed-pane.utils :as tab-utils]))


(def main-navbar [[:app-bar/atoms "'Atoms'"]
                  [:app-bar/molecules "'Molecules'"]
                  [:app-bar/tech "Technologies"]
                  [:app-bar/giants "'Giants'"]])


(defn view
  "main view of the SPA. starts with `login`, switches to `widget-ish` after the user actually
  logs into the system
  "
  []

  (tab-utils/init-tabbed-panel "app-bar" :app-bar/atoms)

  (let [logged-in? (re-frame/subscribe [::subs/logged-in?])]
    (fn []
      (log/info "view" @logged-in?)

      [layout/page {:extra-classes :rccst}
       [flex/flex-panel {:height "calc(100vh - 2rem)"}
        [flex/flex-top
         [navbar/navbar main-navbar [:app-bar/tab-panel]]]

        [layout/page-body {:extra-classes :rccst}
         [tab-panel/tab-panel {:extra-classes             :rccst
                               :subscribe-to-selected-tab [:app-bar/selected-tab]}

          [tab-panel/sub-panel {:panel-id :app-bar/molecules}
           [molecules/page]]
           ;[rc/v-box :src (rc/at)
           ; :children [[#'header/header-bar]
           ;            [rc/gap :size "5px"]
           ;            [rc/h-box :src (rc/at)
           ;             :gap "15px"
           ;             :children [[#'widget-ish/view "uuid-1"]
           ;                        [#'widget-ish/view "uuid-2"]
           ;                        [#'widget-ish/view "uuid-3"]]]]]]

          [tab-panel/sub-panel {:panel-id :app-bar/atoms}
           [catalog/page]]

          [tab-panel/sub-panel {:panel-id :app-bar/tech}
           [tech/page]]

          [tab-panel/sub-panel {:panel-id :app-bar/giants}
           [#'giants/view]]]]]])))


