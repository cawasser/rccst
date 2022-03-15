(ns bh.rccst.ui-component.utils.helpers
  (:require [bh.rccst.ui-component.navbar :as navbar]
            [cljs-uuid-utils.core :as uuid]
            [day8.re-frame.tracing :refer-macros [fn-traced]]
            [re-com.core :as rc]
            [re-frame.core :as re-frame]
            [re-frame.core :as re-frame]
            [reagent.core :as r]
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
    [:div.chart-config {:style {:width "100%"}}
     [navbar/navbar data-or-config [panel]]

     [rc/scroller
      :v-scroll :auto
      :height "500px"
      :child [tab-panel/tab-panel {:extra-classes             :rccst
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


(defn resolve-value [value]
  (cond
    (coll? value) (re-frame/subscribe value)
    (instance? reagent.ratom.RAtom value) value
    (instance? Atom value) value
    :else (r/atom value)))





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



