(ns bh.rccst.views.catalog
  (:require [re-frame.core :as re-frame]
            [woolybear.ad.buttons :as buttons]
            [woolybear.ad.layout :as layout]
            [woolybear.packs.flex-panel :as flex]
            [woolybear.packs.tab-panel :as tab-panel]

            [bh.rccst.views.catalog.layouts :as layout-demo]
            [bh.rccst.views.catalog.containers :as containers-demo]
            [bh.rccst.views.catalog.cards :as cards-demo]
            [bh.rccst.views.catalog.charts :as charts-demo]
            [bh.rccst.views.catalog.diagrams :as diagrams-demo]
            [bh.rccst.views.catalog.icons :as icons-demo]
            [bh.rccst.views.catalog.buttons :as buttons-demo]
            [bh.rccst.views.catalog.forms :as forms-demo]
            [bh.rccst.views.catalog.all :as all-demo]))


(def data-path [:catalog :tab-panel])

(def init-db
  {:tab-panel (tab-panel/mk-tab-panel-data data-path :demo/layouts)})

(re-frame/reg-sub
  :db/catalog
  (fn [db _]
    (:catalog db)))

(re-frame/reg-sub
  :catalog/tab-panel
  :<- [:db/catalog]
  (fn [catalog]
    (:tab-panel catalog)))

(re-frame/reg-sub
  :catalog/selected-tab
  :<- [:catalog/tab-panel]
  (fn [tab-panel]
    (:value tab-panel)))

(defn page
  "Top-level AD Catalog page"
  []
  [layout/page {:extra-classes :rccst}
   [flex/flex-panel {:height "calc(100vh - 2rem)"}
    [flex/flex-top
     [layout/page-header {:extra-classes :rccst}
      [layout/page-title "UI Catalog"]]

     [tab-panel/tab-bar {:extra-classes               :rccst
                         :subscribe-to-component-data [:catalog/tab-panel]}
      [buttons/tab-button {:panel-id :demo/layouts} "Layout"]
      [buttons/tab-button {:panel-id :demo/containers} "Containers"]
      [buttons/tab-button {:panel-id :demo/cards} "Cards"]
      [buttons/tab-button {:panel-id :demo/charts} "Charts"]
      [buttons/tab-button {:panel-id :demo/diagrams} "Diagrams"]
      [buttons/tab-button {:panel-id :demo/icons} "Icons / Images"]
      [buttons/tab-button {:panel-id :demo/buttons} "Buttons"]
      [buttons/tab-button {:panel-id :demo/forms} "Forms"]
      [buttons/tab-button {:panel-id :demo/all} "All"]]]


    [layout/page-body {:extra-classes :rccst}
     [tab-panel/tab-panel {:extra-classes             :rccst
                           :subscribe-to-selected-tab [:catalog/selected-tab]}

      [tab-panel/sub-panel {:panel-id :demo/layouts}
       [layout-demo/catalog]]

      [tab-panel/sub-panel {:panel-id :demo/containers}
       [containers-demo/catalog]]

      [tab-panel/sub-panel {:panel-id :demo/cards}
       [cards-demo/catalog]]

      [tab-panel/sub-panel {:panel-id :demo/charts}
       [charts-demo/catalog]]

      [tab-panel/sub-panel {:panel-id :demo/diagrams}
       [diagrams-demo/catalog]]

      [tab-panel/sub-panel {:panel-id :demo/icons}
       [icons-demo/catalog]]

      [tab-panel/sub-panel {:panel-id :demo/buttons}
       [buttons-demo/catalog]]

      [tab-panel/sub-panel {:panel-id :demo/forms}
       [forms-demo/catalog]]

      [tab-panel/sub-panel {:panel-id :demo/all}
       [all-demo/catalog]]]]]])
