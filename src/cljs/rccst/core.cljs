(ns rccst.core
  (:require [reagent.dom :as rdom]
            [re-frame.core :as re-frame]
            [taoensso.timbre :as log]

            [rccst.events :as events]
            [rccst.events.login]
            [rccst.config :as config]
            [rccst.subscriptions]
            [rccst.views :as views]))


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