(ns bh.rccst.db
  (:require [woolybear.packs.tab-panel :as tab-panel]))

(def default-db
  {:name "re-frame"
   :version ""
   :logged-in? false
   :sources {:number 0
             :string "empty"}
   :nav-bar {:tab-panel (tab-panel/mk-tab-panel-data [:nav-bar :tab-panel] :nav-bar/login)}})
