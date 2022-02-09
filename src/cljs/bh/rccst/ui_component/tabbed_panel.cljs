(ns bh.rccst.ui-component.tabbed-panel
  (:require [taoensso.timbre :as log]

            [woolybear.ad.layout :as layout]
            [woolybear.packs.flex-panel :as flex]
            [woolybear.packs.tab-panel :as tab-panel]

            [bh.rccst.ui-component.navbar :as navbar]
            [bh.rccst.ui-component.tabbed-pane.utils :as tab-utils]))


(defn tabbed-panel
  "makes a large panel containing tabs for each item. Selecting a tab will make tha corresponding
  page show (and hide all the others)

  "
  [& {:keys [title short-name description children start-panel]}]

  (tab-utils/init-tabbed-panel short-name start-panel)

  [layout/page {:extra-classes :rccst}
   [flex/flex-panel {:height "calc(100vh - 2rem)"}
    [flex/flex-top
     [layout/page-header {:extra-classes :rccst}
      [layout/page-title title]
      [layout/markdown-block description]
      [layout/text-block ""]]
     [navbar/navbar children [(keyword short-name "tab-panel")]]]

    [layout/page-body {:extra-classes :rccst}
     (into
       [tab-panel/tab-panel {:extra-classes             :rccst
                             :subscribe-to-selected-tab [(keyword short-name "value")]}]

       (doall
         (map (fn [[tag _ page]]
                [tab-panel/sub-panel {:panel-id tag} page])
           children)))]]])