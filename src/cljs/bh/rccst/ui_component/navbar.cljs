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



(defn navbar []
  (log/info "nav-bar")
  [tab-panel/tab-bar {:extra-classes               :nav-bar
                      :subscribe-to-component-data [:nav-bar/tab-panel]}
   [buttons/tab-button {:panel-id :nav-bar/login} "Login"]
   [buttons/tab-button {:panel-id :nav-bar/widget-ish} "Widget-ish"]
   [buttons/tab-button {:panel-id :nav-bar/catalog} "Catalog"]])


(comment
  (def db @re-frame.db/app-db)


  ())