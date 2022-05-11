(ns bh.rccst.ui-component.atom.chart.radial-bar-chart-2
  (:require [bh.rccst.ui-component.atom.chart.utils :as utils]
            [bh.rccst.ui-component.atom.re-com.configure-toggle :as ct]
            [bh.rccst.ui-component.utils :as ui-utils]
            [bh.rccst.ui-component.utils.color :as color]
            [bh.rccst.ui-component.utils.example-data :as example-data]
            [bh.rccst.ui-component.utils.helpers :as h]
            [bh.rccst.ui-component.utils.locals :as l]
            [bh.rccst.ui-component.atom.chart.wrapper-2 :as wrapper]
            [re-com.core :as rc]
            [reagent.core :as r]
            [taoensso.timbre :as log]
            [woolybear.ad.layout :as layout]
            ["recharts" :refer [ResponsiveContainer RadialBarChart RadialBar Legend Tooltip Cell]]))

(log/info "bh.rccst.ui-component.atom.chart.radial-bar-chart-2")

(def source-code '[])
(def sample-data example-data/meta-tabular-data)
;(def sample-data (r/atom [{:name "18-24", :uv 31.47, :pv 2400, :fill "#8884d8"}
;                          {:name "25-29", :uv 26.69, :pv 4567, :fill "#83a6ed"}
;                          {:name "30-34", :uv -15.69, :pv 1398, :fill "#8dd1e1"}
;                          {:name "35-39", :uv 8.22, :pv 9800, :fill "#82ca9d"}
;                          {:name "40-49", :uv -8.63, :pv 3908, :fill "#a4de6c"}
;                          {:name "50+", :uv -2.63, :pv 4800, :fill "#d0ed57"}
;                          {:name "unknow", :uv 6.67, :pv 4800, :fill "#ffc658"}]))


(defn local-config [data]

  ;(log/info "local-config" @data)

  (let [ret (merge
              {:brush false}
              (->> (get-in @data [:metadata :fields])
                   (filter (fn [[k v]] (= :number v)))
                   keys
                   (map-indexed (fn [idx a]
                                  {a {:include (if (= a :uv) true false) ;;just show uv to make it cleaner at start
                                      :fill    (color/get-color idx)
                                      :stackId ""}}))
                   (into {})))]
    ;(log/info "local-config" ret)
    ret))


(defn config
  "constructs the configuration data structure for the widget. This is specific to this being a radar-chart component.

  ---

  - component-id : (string) id of the widget, in this specific case
  "
  [component-id data]
  (-> ui-utils/default-pub-sub
      (merge
        utils/default-config
        {:tab-panel {:value     (keyword component-id "config")
                     :data-path [:containers (keyword component-id) :tab-panel]}}
        (local-config data))))


(defn- radial-config [component-id label path position]
  [rc/v-box :src (rc/at)
   :gap "5px"
   :children [[utils/boolean-config component-id label (conj path :include)]]])

(defn- make-radial-bar-config [component-id data]

  (->> (get-in @data [:metadata :fields])
        (filter (fn [[k v]] (= :number v)))
        keys
        (map-indexed (fn [idx a]
                       [radial-config component-id a [a] :above-right]))
        (into [])))


(defn config-panel
  "the panel of configuration controls

  ---

  - data : (atom) data to display (may be used by the standard configuration components for thins like axes, etc.\n  - config : (atom) holds all the configuration settings made by the user
  "
  [data component-id]

  [rc/v-box :src (rc/at)
   :gap "10px"
   :width "100%"
   :style {:padding          "15px"
           :border-top       "1px solid #DDD"
           :background-color "#f7f7f7"}
   :children [[utils/non-gridded-chart-components component-id]
              [rc/line :src (rc/at) :size "2px"]
              [rc/h-box :src (rc/at)
               :gap "10px"
               :children (make-radial-bar-config component-id data)]]])


(defn- make-radial-bar-display [data subscriptions isAnimationActive?]
  (log/info "make-radial-bar-display: " subscriptions)

  (let [ret (->> (get-in data [:metadata :fields])
                 (filter (fn [[_ v]] (= :number v)))
                 keys
                 (map-indexed (fn [idx a]
                                (log/info "indexed radial bar loop: " a " color: "(ui-utils/resolve-sub subscriptions [a :fill]))
                        (if (ui-utils/resolve-sub subscriptions [a :include])
                          [:> RadialBar {:minAngle   15
                                         ;:label      {:fill "#666", :position "insideStart"}
                                         :background {:clockWise true}
                                         :dataKey    a}
                           [:> Cell {:key  (str "cell-" idx)
                                     :fill (or (ui-utils/resolve-sub subscriptions [a :fill])
                                               (color/get-color 0))}]]
                          [])))
                 (remove empty?)
                 (into [:<>]))]
    ;(log/info "ret" ret)

    ret))


(defn- component* [& {:keys [data component-id container-id
                             subscriptions isAnimationActive?]
                      :as params}]
  (let [d (if (empty? data) [] (get data :data))]
    (log/info "radial component* data: " d)
    [:> ResponsiveContainer
     [:> RadialBarChart {:innerRadius "10%"
                         :outerRadius "80%"
                         :data        d
                         :startAngle  180
                         :endAngle    0}

      (utils/non-gridded-chart-components component-id {})

      (make-radial-bar-display data subscriptions isAnimationActive?)
      [:> Legend {:iconSize 10 :width 120 :height 140 :layout "vertical" :verticalAlign "middle" :align "right"}]
      [:> Tooltip]]]))


(defn component [& {:keys [data config-data component-id container-id
                           data-panel config-panel] :as params}]

  [wrapper/base-chart
   :data data
   :config-data config-data
   :component-id component-id
   :container-id container-id
   :component* component*
   :component-panel wrapper/component-panel
   :data-panel data-panel
   :config-panel config-panel
   :config config
   :local-config local-config])



(def meta-data {:rechart/radial-bar-2 {:component component
                                       :ports     {:data   :port/sink
                                                   :config :port/sink}}})


(comment

  (def data (r/atom example-data/meta-tabular-data))

  (config "comp-id" data)


  ;{[:tv :fill] #object[reagent.ratom.Reaction {:val "#82ca9d"}],
  ; [:uv :fill] #object[reagent.ratom.Reaction {:val "#8884d8"}],
  ; [:pv] #object[reagent.ratom.Reaction {:val {:include false, :fill "#ffc107", :stackId ""}}],
  ; [:pv :stackId] #object[reagent.ratom.Reaction {:val ""}],
  ; [:tv] #object[reagent.ratom.Reaction {:val {:include false, :fill "#82ca9d", :stackId ""}}],
  ; [:amt :stackId] #object[reagent.ratom.Reaction {:val ""}],
  ; [:pv :include] #object[reagent.ratom.Reaction {:val nil}],
  ; [:amt :fill] #object[reagent.ratom.Reaction {:val "#ff00ff"}],
  ; [:uv :include] #object[reagent.ratom.Reaction {:val true}],
  ; [:brush] #object[reagent.ratom.Reaction {:val nil}],
  ; [:tv :include] #object[reagent.ratom.Reaction {:val nil}],
  ; [:amt] #object[reagent.ratom.Reaction {:val {:include false, :fill "#ff00ff", :stackId ""}}],
  ; [:pv :fill] #object[reagent.ratom.Reaction {:val "#ffc107"}],
  ; [:uv :stackId] #object[reagent.ratom.Reaction {:val ""}],
  ; [:tv :stackId] #object[reagent.ratom.Reaction {:val ""}],
  ; [:uv] #object[reagent.ratom.Reaction {:val {:include true, :fill "#8884d8", :stackId ""}}],
  ; [:amt :include] #object[reagent.ratom.Reaction {:val nil}]}


  ())