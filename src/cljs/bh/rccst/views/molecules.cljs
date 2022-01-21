(ns bh.rccst.views.molecules
  (:require [woolybear.ad.layout :as layout]
            [woolybear.packs.flex-panel :as flex]
            [woolybear.packs.tab-panel :as tab-panel]

            [bh.rccst.ui-component.tabbed-pane.utils :as tab-utils]
            [bh.rccst.ui-component.navbar :as navbar]
            [bh.rccst.views.molecule.general :as general]
            [bh.rccst.views.molecule.all :as all]))



(def navbar [[:molecules/general "General"]
             [:molecules/all "All"]])


(defn page
  "Page to explore the various 'molecules' (more complex UI elements)"
  []

  (tab-utils/init-tabbed-panel "molecules" :molecules/general)

  [layout/page {:extra-classes :rccst}
   [flex/flex-panel {:height "calc(100vh - 2rem)"}
    [flex/flex-top
     [layout/page-header {:extra-classes :rccst}
      [layout/page-title "'Molecule' Catalog"]
      [layout/markdown-block "Based upon [_Atomic Design_](https://bradfrost.com/blog/post/atomic-web-design/) by Brad Frost"]
      [layout/text-block ""]]
     [navbar/navbar navbar [:molecules/tab-panel]]]

    [layout/page-body {:extra-classes :rccst}
     [tab-panel/tab-panel {:extra-classes             :rccst
                           :subscribe-to-selected-tab [:molecules/selected-tab]}

      [tab-panel/sub-panel {:panel-id :molecules/general}
       [general/catalog]]

      [tab-panel/sub-panel {:panel-id :molecules/all}
       [all/catalog]]]]]])


