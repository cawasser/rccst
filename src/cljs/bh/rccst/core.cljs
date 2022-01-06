(ns bh.rccst.core
  (:require [reagent.dom :as rdom]
            [re-frame.core :as re-frame]
            [taoensso.timbre :as log]

            [bh.rccst.events :as events]
            [bh.rccst.events.login]
            [bh.rccst.config :as config]
            [bh.rccst.subscriptions]
            [bh.rccst.views :as views]))


(defn dev-setup []
  (when config/debug?
    (log/info "dev mode")))


(defn ^:dev/after-load mount-root []
  (re-frame/clear-subscription-cache!)
  (let [root-el (.getElementById js/document "app")]
    (rdom/unmount-component-at-node root-el)
    (rdom/render [#'views/view] root-el)))


(defn init []
  (log/info "rccts.core/init")

  (re-frame/dispatch-sync [::events/initialize-db])

  (dev-setup)
  (mount-root))


(comment


  ())