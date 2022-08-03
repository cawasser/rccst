(ns rccst.core
  (:require [bh.subscriptions]
            [rccst.config :as config]
            [rccst.events :as events]
            [rccst.events.login]
            [rccst.views :as views]
            [re-frame.core :as re-frame]
            [reagent.dom :as rdom]
            [taoensso.timbre :as log]))


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