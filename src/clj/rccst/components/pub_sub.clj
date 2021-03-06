(ns rccst.components.pub-sub
  (:require [rccst.components.system :as system]
            [clojure.set :refer [union]]
            [clojure.tools.logging :as log]
            [com.stuartsierra.component :as component]))


(defn publish!
  "
  "
  [socket subscribers [data-tag {:keys [id]} :as message]]

  (log/info "publish!" data-tag
    "//" id
    "//" (get-in @subscribers [:sources id]))

  (doseq [sub (get-in @subscribers [:sources id])]
    (log/info "publishing" message
      "//" sub
      "//" (:chsk-send! socket))
    ((:chsk-send! socket) sub message)))


(defn subscribe
  "
  "
  [subscriptions lookup publish! uid data-tags]

  ;
  ; register the subscription(s) first
  ;
  (reset! subscriptions
    (reduce (fn [subman tag]
              (-> subman
                (assoc-in [:sources tag]
                  (union (get-in subman [:sources tag]) #{uid}))
                (assoc-in [:subscribers uid]
                  (union (get-in subman [:subscribers uid]) #{tag}))))
      @subscriptions data-tags))

  ;
  ; now, (if this is the first subscription to a tag...)
  ;    we need to start the "data-source provider" and pass it the :publish! function
  ;
  (let [source-fn (lookup (first data-tags))]
    (log/info "subscribe source-fn" lookup "//" data-tags "//" source-fn)
    (when source-fn
      (source-fn publish!))))


(defn cancel
  "
  "
  [subscriptions uid data-tag]
  (let [current-sources     (get-in @subscriptions [:sources data-tag])
        current-subscribers (get-in @subscriptions [:subscribers uid])]

    ;
    ; if we remove the last subscriber to a source
    ;    we should stop the "provider"
    ;

    (reset! subscriptions
      (-> @subscriptions
        (assoc-in [:sources data-tag] (set (disj current-sources uid)))
        (assoc-in [:subscribers uid] (set (disj current-subscribers data-tag)))))))


(defn cancel-all
  "
  "
  [subscriptions uid]
  (let [current-subscriptions (get-in @subscriptions [:subscribers uid])]

    ;
    ; if we remove the last subscriber to a source
    ;    we should stop the "provider"
    ;

    (reset! subscriptions
      (-> @subscriptions
        (dissoc :subscribers uid)
        (assoc :sources (->> current-subscriptions
                          (map (fn [s]
                                 {s (disj (get-in @subscriptions [:sources s]) uid)}))
                          (into {})))))))


(defrecord PubSub [pub-sub socket data-sources]
  component/Lifecycle

  (start [component]
    (log/info ";; Subscribers")
    (let [subscriptions (atom {:subscribers {}
                               :sources     {}})]
      (assoc component
        :pub-sub subscriptions
        :subscriptions subscriptions
        :lookup (:lookup data-sources)
        :subscribe (partial subscribe
                     subscriptions
                     (:lookup data-sources)
                     (partial publish! socket subscriptions))
        :cancel (partial cancel subscriptions)
        :cancel-all (partial cancel-all subscriptions)
        :publish! (partial publish! socket subscriptions))))

  (stop [component]
    (log/info ";; Stopping rccst.components.pub-sub")
    (assoc component
      :pub-sub nil
      :subscribers nil
      :subscriptions nil
      :subscribe nil
      :cancel nil
      :cancel-all nil)))


(defn new-pub-sub
  []
  (map->PubSub {}))




; test subscriptions, subscribe, cancel and cancel-all
(comment
  (do
    (def subscriptions (atom {:subscribers {} :sources {}}))

    (def current-sources #{})
    (def current-subscribers #{})
    (def uid "alpha")
    (def data-tags #{:1 :2}))



  ; okay, this needs a reducer
  ;
  ; {:rccst.components.pub-sub {} :sources {}}
  ;
  ;    -> sub alpha [:1 :2]
  ;
  ; {:rccst.components.pub-sub { "alpha" #{:1 :2}}
  ;  :sources {:1 #{"alpha"} :2 #{"alpha"}}
  ;
  ;    -> sub bravo [:1]
  ;
  ; {:rccst.components.pub-sub {"alpha" #{:1 :2} "bravo" #{:1}}
  ;  :sources {:1 #{"alpha" "bravo"} :2 #{"alpha"}}
  ;
  ;    -> sub charlie [:1 :3]
  ;
  ; {:rccst.components.pub-sub {"alpha" #{:1 :2} "bravo" #{:1} "charlie" #{:1 :3}}
  ;  :sources {:1 #{"alpha" "bravo" "charlie"} :2 #{"alpha"} :3 #{"charlie"}}
  ;
  ;



  (def subscriptions (atom {:subscribers {} :sources {}}))

  (let [uid "alpha"]
    (reset! subscriptions
      (reduce (fn [subman tag]
                (-> subman
                  (assoc-in [:sources tag]
                    (union (get-in subman [:sources tag]) #{uid}))
                  (assoc-in [:subscribers uid]
                    (union (get-in subman [:subscribers uid]) #{tag}))))
        @subscriptions #{:1 :2})))

  (let [uid "bravo"]
    (reset! subscriptions
      (reduce (fn [subman tag]
                (-> subman
                  (assoc-in [:sources tag]
                    (union (get-in subman [:sources tag]) #{uid}))
                  (assoc-in [:subscribers uid]
                    (union (get-in subman [:subscribers uid]) #{tag}))))
        @subscriptions #{:1})))

  (let [uid "charlie"]
    (reset! subscriptions
      (reduce (fn [subman tag]
                (-> subman
                  (assoc-in [:sources tag]
                    (union (get-in subman [:sources tag]) #{uid}))
                  (assoc-in [:subscribers uid]
                    (union (get-in subman [:subscribers uid]) #{tag}))))
        @subscriptions #{:1 :3})))


  ; let's try it now
  (def subscriptions (atom {:subscribers {} :sources {}}))

  (subscribe subscriptions "alpha" #{:1 :2})
  (subscribe subscriptions "bravo" #{:1})
  (subscribe subscriptions "charlie" #{:1 :3})


  ())


(comment

  ; subscribe
  (subscribe subscriptions "dummy" #{:dummy})
  (subscribe subscriptions "alpha" #{:dummy :something-new})
  (subscribe subscriptions "brave" #{:dummy})
  (subscribe subscriptions "dummy" #{:something-new})

  ; cancel
  (cancel subscriptions "dummy" :dummy)
  (cancel subscriptions "alpha" :dummy)
  (cancel subscriptions "alpha" :something-new)
  (cancel subscriptions "dummy" :something-new)

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


; test sending to rccst.components.pub-sub via the socket
(comment
  (def publish-fn (get-in @system/system [:pub-sub :publish!]))
  (def subscribers (get-in @system/system [:pub-sub :subscriptions]))

  (get-in @subscribers [:sources :source/satellites])

  (publish-fn [:publish/data-update {:id :nobody :value 100}])

  (publish-fn [:publish/data-update {:id :number :value 100}])
  (publish-fn [:publish/data-update {:id :number :value 999}])

  (get-in @system/system [:pub-sub :publish!])
  (get-in @system/system [:pub-sub :lookup])

  ((get-in @system/system [:pub-sub :lookup]) :source/targets)

  (publish-fn [:publish/data-update
               {:id    :string
                :value "a new value for the string"}])


  (publish-fn [:publish/data-update
               {:id    :source/targets
                :value [{:id "one" :data "TARGET DATA"}
                        {:id "two" :data "TWO TWO TWO"}
                        {:id "three" :data "TARGET DATA"}
                        {:id "four" :data "TARGET DATA"}]}])
  (publish-fn [:publish/data-update
               {:id    :source/satellites
                :value [{:id "one" :data "SATELLITE DATA"}
                        {:id "two" :data "SATELLITE DATA"}
                        {:id "three" :data "SATELLITE DATA"}
                        {:id "four" :data "SATELLITE DATA"}]}])
  (publish-fn [:publish/data-update
               {:id    :source/coverages
                :value [{:id "COVERAGE DATA" :shapes []}
                        {:id "COVERAGE DATA" :shapes []}
                        {:id "COVERAGE DATA" :shapes []}
                        {:id "COVERAGE DATA" :shapes []}]}])


  (def id :source/targets)
  (get-in @subscribers [:sources id])

  ())

