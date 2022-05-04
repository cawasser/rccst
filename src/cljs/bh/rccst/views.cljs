(ns bh.rccst.views
  (:require [bh.rccst.subs :as subs]
            [bh.rccst.ui-component.navbar :as navbar]

            [bh.rccst.ui-component.tabbed-pane.utils :as tab-utils]
            [bh.rccst.views.atoms :as atoms]
            [bh.rccst.views.giants :as giants]

            [bh.rccst.views.molecules :as molecules]
            [bh.rccst.views.technologies :as tech]
            [bh.rccst.views.templates :as templates]
            [bh.rccst.views.welcome :as welcome]
            [re-frame.core :as re-frame]
            [woolybear.ad.layout :as layout]
            [woolybear.packs.flex-panel :as flex]

            [woolybear.packs.tab-panel :as tab-panel]))


(def main-navbar [[:app-bar/welcome "Welcome!"]
                  [:app-bar/tech "Technologies"]
                  [:app-bar/atoms "'Atoms'"]
                  [:app-bar/molecules "'Molecules'"]
                  [:app-bar/templates "'Templates'"]
                  [:app-bar/giants "'Giants'"]])


(defn view
  "main view of the SPA. starts with `login`, switches to `widget-ish` after the user actually
  logs into the system
  "
  []

  (tab-utils/init-tabbed-panel "app-bar" :app-bar/welcome)

  (let [logged-in? (re-frame/subscribe [::subs/logged-in?])]
    (fn []
      ;(log/info "view" @logged-in?)

      [layout/page {:extra-classes :is-fluid}

       [flex/flex-panel {:width "100%"
                         :height "97vh"}
        [flex/flex-top {:extra-classes :is-fluid}
         [navbar/navbar main-navbar [:app-bar/tab-panel]]]
        [flex/flex-bottom "UI Component Catalog"]

        ;[layout/page-body {:extra-classes :is-fluid}]
        [tab-panel/tab-panel {:extra-classes             :is-fluid
                              :subscribe-to-selected-tab [:app-bar/value]}

         [tab-panel/sub-panel {:extra-classes :is-fluid :panel-id :app-bar/welcome}
          [welcome/page]]

         [tab-panel/sub-panel {:extra-classes :is-fluid :panel-id :app-bar/tech}
          [tech/page]]

         [tab-panel/sub-panel {:extra-classes :is-fluid :panel-id :app-bar/atoms}
          [atoms/page]]

         [tab-panel/sub-panel {:extra-classes :is-fluid :panel-id :app-bar/molecules}
          [molecules/page]]

         [tab-panel/sub-panel {:extra-classes :is-fluid :panel-id :app-bar/templates}
          [templates/page]]

         [tab-panel/sub-panel {:extra-classes :is-fluid :panel-id :app-bar/giants}
          [#'giants/view]]]]])))


