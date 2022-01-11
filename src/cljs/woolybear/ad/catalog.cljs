(ns woolybear.ad.catalog
  (:require [re-frame.core :as re-frame]
            [woolybear.ad.buttons :as buttons]
            [woolybear.ad.layout :as layout]
            [woolybear.packs.flex-panel :as flex]
            [woolybear.packs.tab-panel :as tab-panel]
            [woolybear.ad.catalog.layouts :as layout-demo]
            [woolybear.ad.catalog.containers :as containers-demo]
            [woolybear.ad.catalog.icons :as icons-demo]
            [woolybear.ad.catalog.buttons :as buttons-demo]
            [woolybear.ad.catalog.forms :as forms-demo]))

(def data-path [:ad-catalog :tab-panel])

(def init-db
  {:tab-panel (tab-panel/mk-tab-panel-data data-path :demo/layouts)})

(re-frame/reg-sub
  :db/ad-catalog
  (fn [db _]
    (:ad-catalog db)))

(re-frame/reg-sub
  :ad-catalog/tab-panel
  :<- [:db/ad-catalog]
  (fn [ad-catalog]
    (:tab-panel ad-catalog)))

(re-frame/reg-sub
  :tab-panel/selected-tab
  :<- [:ad-catalog/tab-panel]
  (fn [tab-panel]
    (:value tab-panel)))

(defn page
  "Top-level AD Catalog page"
  []
  [layout/page {:extra-classes :ad-catalog}
   [flex/flex-panel {:height "calc(100vh - 2rem)"}
    [flex/flex-top
     [layout/page-header {:extra-classes :ad-catalog}
      [layout/page-title "AD Catalog"]]

     [tab-panel/tab-bar {:extra-classes               :ad-catalog
                         :subscribe-to-component-data [:ad-catalog/tab-panel]}
      [buttons/tab-button {:panel-id :demo/layouts} "Layout"]
      [buttons/tab-button {:panel-id :demo/containers} "Containers"]
      [buttons/tab-button {:panel-id :demo/icons} "Icons / Images"]
      [buttons/tab-button {:panel-id :demo/buttons} "Buttons"]
      [buttons/tab-button {:panel-id :demo/forms} "Forms"]]]


    [layout/page-body {:extra-classes :ad-catalog}
     [tab-panel/tab-panel {:extra-classes             :ad-catalog
                           :subscribe-to-selected-tab [:tab-panel/selected-tab]}

      [tab-panel/sub-panel {:panel-id :demo/layouts}
       [layout-demo/catalog]]

      [tab-panel/sub-panel {:panel-id :demo/containers}
       [containers-demo/catalog]]

      [tab-panel/sub-panel {:panel-id :demo/icons}
       [icons-demo/catalog]]

      [tab-panel/sub-panel {:panel-id :demo/buttons}
       [buttons-demo/catalog]]

      [tab-panel/sub-panel {:panel-id :demo/forms}
       [forms-demo/catalog]]]]]])


