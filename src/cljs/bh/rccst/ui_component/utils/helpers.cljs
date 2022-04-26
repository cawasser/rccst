(ns bh.rccst.ui-component.utils.helpers
  (:require [bh.rccst.ui-component.navbar :as navbar]
            [cljs-uuid-utils.core :as uuid]
            [day8.re-frame.tracing :refer-macros [fn-traced]]
            [re-com.core :as rc]
            [re-frame.core :as re-frame]
            [reagent.core :as r]
            [taoensso.timbre :as log]
            [woolybear.packs.tab-panel :as tab-panel]))


(defn config-tab-panel [chart-id]
  {:tab-panel {:value     (keyword chart-id "config")
               :data-path [:widgets (keyword chart-id) :tab-panel]}})


(defn component-id []
  (-> (uuid/make-random-uuid)
    uuid/uuid-string))


(defn chart-config [[config data panel tab] data-panel config-panel]
  ;(log/info "chart-config" config data panel tab)
  (let [data-or-config [[config "config"]
                        [data "data"]]]
    [:div.chart-config {:style {:width "100%" :height "100%"}}
     [navbar/navbar data-or-config [panel]]
     [rc/scroller :src (rc/at)
      :v-scroll :auto
      :height "95%"
      :child [tab-panel/tab-panel {:extra-classes             :is-fluid
                                   :subscribe-to-selected-tab [tab]}

              [tab-panel/sub-panel {:panel-id config}
               config-panel]

              [tab-panel/sub-panel {:panel-id data}
               data-panel]]]]))


(defn path->string [& path]
  (->> path
    flatten
    (remove nil?)
    (map str)
    (map #(clojure.string/replace % #":" ""))
    (map #(clojure.string/replace % #"/" "."))
    (map #(clojure.string/replace % #" " "-"))
    (clojure.string/join ".")))


(defn path->keyword [& path]
  (->> path
    path->string
    keyword))


(defn string->keyword [s]
  (-> s
    str
    (clojure.string/replace #":" "")
    keyword))


(defn- resolve-subscription
  "resolve a subscription.

  there are 2 types if subscriptions: REMOTE and LOCAL

  REMOTE subscriptions are designed to reach across the network and query data from the Server, while
  LOCAL subscriptions are designed to reach into the Re-frame 'APP-DB' at a certain path
  "
  [subs opts]
  (let [[target & _] subs]
    ;(log/info "resolve-subscription" subs "//" target)
    (if (= target :bh.rccst.subs/source)
      (re-frame/subscribe (reduce conj subs opts))
      (re-frame/subscribe (reduce conj [(path->keyword subs)] opts)))))


(defn resolve-value [value & opts]
  ;(log/info "resolve-value" value "//" opts
  ;  "// (path-kw)" (reduce conj [(path->keyword value)] opts)
  ;  "// (path-sub)" (reduce conj [(path->keyword value)] opts))

  (let [ret (cond
              (keyword? value) (re-frame/subscribe (reduce conj [(path->keyword value)] opts))
              (and (coll? value)
                (not (empty? value))
                (every? keyword? value)) (resolve-subscription value opts)
              (instance? reagent.ratom.RAtom value) value
              (instance? Atom value) value
              :else (r/atom value))]
    ;(log/info "resolve-value" value "//" opts "//" ret "//" (str @ret))
    ret))



(defn handle-change [value new-value]
  ;(log/info "handle-change" value "//" new-value)
  (cond
    (or (coll? value)
      (keyword? value)
      (string? value)) (re-frame/dispatch (conj value new-value))
    (instance? reagent.ratom.RAtom value) (reset! value new-value)
    (instance? Atom value) (reset! value new-value)
    :else ()))


(defn handle-change-path [value path new-value]
  ;(log/info "handle-change-path" value "//" path "//" new-value)

  (cond
    (or (coll? value)
      (keyword? value)
      (string? value)) (let [update-event (conj [(path->keyword value path)] new-value)]
                         ;(log/info "handle-change-path (update event)" update-event)
                         (re-frame/dispatch update-event))
    (instance? reagent.ratom.RAtom value) (swap! value assoc-in path new-value)
    (instance? Atom value) (swap! value assoc-in path new-value)
    :else ()))


(comment
  (def path [:uv :fill])
  (def value [:dummy])

  (path->keyword value path)
  (conj [(path->keyword value path)] "#000000")

  (->>
    (re-frame/subscribe [:coverage-plan-demo.component.blackboard.topic.current-time])
    deref
    str)

  ())



(comment
  (path->string "one" "two" "three/dummy")
  (path->keyword "one" "two" "three/dummy")

  (path->keyword :area-chart-demo.area-chart :grid nil)
  (path->string :area-chart-demo.area-chart :grid nil)


  (path->keyword :topic/layers)
  (path->keyword [:topic/layers])

  (apply conj [:widgets]
    (map path->keyword [:blackboard :topic/layers]))

  ())



