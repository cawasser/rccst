(ns bh.rccst.core
  (:require
   [reagent.dom :as rdom]
   [re-frame.core :as re-frame]
   [taoensso.timbre :as log]
   [bh.rccst.events :as events]
   [bh.rccst.views :as views]
   [bh.rccst.config :as config]
   [bh.rccst.subscriptions :as pub-sub]))


(defn dev-setup []
  (when config/debug?
    (log/info "dev mode")))


(defn ^:dev/after-load mount-root []
  (re-frame/clear-subscription-cache!)
  (let [root-el (.getElementById js/document "app")]
    (rdom/unmount-component-at-node root-el)
    (rdom/render [views/main-panel] root-el)))


(defn init []
  (log/info "rccts.core/init")

  (re-frame/dispatch-sync [::events/initialize-db])
  (re-frame/dispatch [::pub-sub/start])

  (dev-setup)
  (mount-root))


(comment


  ())