(ns bh.rccst.views
  (:require [re-frame.core :as re-frame]
            [taoensso.timbre :as log]

            [woolybear.ad.layout :as layout]
            [woolybear.packs.flex-panel :as flex]
            [woolybear.packs.tab-panel :as tab-panel]

            [bh.rccst.subs :as subs]
            [bh.rccst.ui-component.navbar :as navbar]
            [bh.rccst.views.atoms :as atoms]
            [bh.rccst.views.molecules :as molecules]
            [bh.rccst.views.templates :as templates]
            [bh.rccst.views.technologies :as tech]
            [bh.rccst.views.giants :as giants]

            [bh.rccst.ui-component.tabbed-pane.utils :as tab-utils]))


(def main-navbar [[:app-bar/atoms "'Atoms'"]
                  [:app-bar/molecules "'Molecules'"]
                  [:app-bar/templates "'Templates'"]
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
      ;(log/info "view" @logged-in?)

      [layout/page {:extra-classes :rccst}
       [flex/flex-panel {:height "calc(100vh - 2rem)"}
        [flex/flex-top
         [navbar/navbar main-navbar [:app-bar/tab-panel]]]

        [layout/page-body {:extra-classes :rccst}
         [tab-panel/tab-panel {:extra-classes             :rccst
                               :subscribe-to-selected-tab [:app-bar/value]}

          [tab-panel/sub-panel {:panel-id :app-bar/molecules}
           [molecules/page]]

          [tab-panel/sub-panel {:panel-id :app-bar/atoms}
           [atoms/page]]

          [tab-panel/sub-panel {:panel-id :app-bar/templates}
           [templates/page]]

          [tab-panel/sub-panel {:panel-id :app-bar/tech}
           [tech/page]]

          [tab-panel/sub-panel {:panel-id :app-bar/giants}
           [#'giants/view]]]]]])))


