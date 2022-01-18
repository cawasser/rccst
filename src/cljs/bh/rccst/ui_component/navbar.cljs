(ns bh.rccst.ui-component.navbar
  (:require [taoensso.timbre :as log]
            [re-frame.core :as re-frame]
            [woolybear.packs.tab-panel :as tab-panel]
            [woolybear.ad.buttons :as buttons]))


(def data-path [:nav-bar :tab-panel])

(def init-db
  {:tab-panel (tab-panel/mk-tab-panel-data data-path :nav-bar/login)})


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


(defn navbar
  "uses woolybear/tab-bar to put controls on the UI that activate different
  pages of the UI.

  ---

  - children : (vector of pairs) each pair consists of: `[ panel-id label ]`
  - subscription : (re-frame subscription vector) subscription to the correct state for this tabb-panel collection

  > See also:
  >
  > [Woolybear/tab-bar](https://github.com/cawasser/woolybear/blob/a7f820dfb2f51636122d56d1500baefe5733eb25/src/cljs/woolybear/packs/tab_panel.cljs#L61)
  "
  [children subcription]
  (log/info "nav-bar")
  (->> children
    (map (fn [[id label]]
           [buttons/tab-button {:panel-id id} label]))
    (into [tab-panel/tab-bar {:extra-classes               :rccst
                              :subscribe-to-component-data subcription}])))

