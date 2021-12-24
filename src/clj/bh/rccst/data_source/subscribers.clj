(ns bh.rccst.data-source.subscribers
  (:require [clojure.tools.logging :as log]
            [com.stuartsierra.component :as component]
            [clojure.core.async :as async :refer [go-loop <!]]))


(defn publish! [socket subscribers [data-tag & _ :as message]]
  (doseq [subs (get-in @subscribers [:sources data-tag])]
    ()))


(defn subscribe [subscriptions data-tag uid]
  (let [current-sources (get-in @subscriptions [:sources data-tag])
        current-subscribers (get-in @subscriptions [:subscribers uid])]
    (reset! subscriptions
      (-> @subscriptions
        (assoc-in [:sources data-tag] (set (conj current-sources uid)))
        (assoc-in [:subscribers uid] (set (conj current-subscribers data-tag)))))))


(defn cancel [subscriptions data-tag uid]
  (let [current-sources (get-in @subscriptions [:sources data-tag])
        current-subscribers (get-in @subscriptions [:subscribers uid])]
    (reset! subscriptions
      (-> @subscriptions
        (assoc-in [:sources data-tag] (set (disj current-sources uid)))
        (assoc-in [:subscribers uid] (set (disj current-subscribers data-tag)))))))


(defn cancel-all [subscriptions uid]
  (let [current-subscriptions (get-in @subscriptions [:subscribers uid])]
    (reset! subscriptions
      (-> @subscriptions
        (dissoc :subscribers uid)
        (assoc :sources (->> current-subscriptions
                          (map (fn [s]
                                 {s (disj (get-in @subscriptions [:sources s]) uid)}))
                          (into {})))))))


(defrecord Subscribers [subscribers socket]
  component/Lifecycle

  (start [component]
    (log/info ";; Subscribers")
    (let [subscriptions (atom {:subscribers {}
                               :sources     {}})]
      (assoc component
        :subscribers (partial publish! socket subscribers)
        :subscriptions subscriptions
        :subscribe (partial subscribe subscriptions)
        :cancel (partial cancel subscriptions)
        :cancel-all (partial cancel subscriptions))))

  (stop [component]
    (log/info ";; Stopping subscribers")
    (assoc component
      :subscribers nil
      :subscriptions nil
      :subscribe nil
      :cancel nil
      :cancel-all nil)))


(defn new-subscribers
  []
  (map->Subscribers {}))




; test subscriptions, subscribe, cancel and cancel-all
(comment
  (def subscriptions (atom {:subscribers {}
                            :sources     {}}))


  ; subscribe
  (subscribe subscriptions :dummy "dummy")
  (subscribe subscriptions :dummy "alpha")
  (subscribe subscriptions :dummy "brave")
  (subscribe subscriptions :something-new "dummy")

  ; cancel
  (cancel subscriptions :dummy "dummy")
  (cancel subscriptions :dummy "alpha")
  (cancel subscriptions :something-new "alpha")
  (cancel subscriptions :something-new "dummy")

  ; cancel-all
  (def current-subscriptions (get-in @subscriptions [:subscribers "dummy"]))
  (map (fn [s]
         {:sources {s (disj (get-in @subscriptions [:sources s]) "dummy")}})
    current-subscriptions)
  (->> current-subscriptions
    (map (fn [s]
           {s (disj (get-in @subscriptions [:sources s]) "dummy")}))
    (into {}))

  (cancel-all subscriptions "dummy")


  ())