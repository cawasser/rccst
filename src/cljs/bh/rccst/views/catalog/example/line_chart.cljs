(ns bh.rccst.views.catalog.example.line-chart
  (:require [taoensso.timbre :as log]
            [woolybear.ad.layout :as layout]
            ["recharts" :refer [LineChart Line
                                XAxis YAxis CartesianGrid
                                Tooltip Legend]]
            [reagent.core :as r]
            [reagent.ratom :refer-macros [reaction]]
            [re-com.core :as rc]

            [bh.rccst.views.catalog.utils :as bcu]
            [bh.rccst.ui-component.table :as table]))


(defonce data (r/atom [{:name "Page A" :uv 4000 :pv 2400 :amt 2400}
                       {:name "Page B" :uv 3000 :pv 1398 :amt 2210}
                       {:name "Page C" :uv 2000 :pv 9800 :amt 2290}
                       {:name "Page D" :uv 2780 :pv 3908 :amt 2000}
                       {:name "Page E" :uv 1890 :pv 4800 :amt 2181}
                       {:name "Page F" :uv 2390 :pv 3800 :amt 2500}
                       {:name "Page G" :uv 3490 :pv 4300 :amt 2100}]))


(defonce config (r/atom {:isAnimationActive true
                         :grid              {:over?           false
                                             :include         true
                                             :strokeDasharray {:value "3 3" :omit? false :editing? (atom false) :type :text}}
                         :x-axis            {:over?       false
                                             :include     true
                                             :orientation {:value :bottom :omit? false :editing? (atom false) :type :text :text "Box1"}
                                             :scale       {:value "auto" :omit? false :editing? (atom false) :type :auto :px "50px" :ratio "3" :gsb "1 1 0px"}}
                         :y-axis            {:over?   false
                                             :include true}
                         :tooltip           {:over?   false
                                             :include true}
                         :legend            {:over?   false
                                             :include true}
                         :line-uv           {:over?   false
                                             :include true}
                         :line-pv           {:over?   false
                                             :include true}
                         :line-amt          {:over?   false
                                             :include false
                                             :isAnimationActive true}}))



(comment
  (swap! config assoc-in [:grid :include] false)
  (swap! config assoc-in [:grid :include] true)


  (swap! config assoc-in [:y-axis :include] true)
  (swap! config assoc-in [:y-axis :include] false)

  (swap! config assoc-in [:line-uv :include] true)
  (swap! config assoc-in [:line-uv :include] false)

  (swap! config assoc-in [:line-pv :include] true)
  (swap! config assoc-in [:line-pv :include] false)


  (swap! config assoc-in [:line-amt :include] true)
  (swap! config assoc-in [:line-amt :include] false)


  (swap! config assoc :isAnimationActive true)
  (swap! config assoc :isAnimationActive false)


  ())


(defn string-editor [])


(defn- config-panel
  "the panel of configuration controls

  ---

  - config : (atom) holds all the configuration settings made by the user
  "
  [config]

  [rc/v-box :src (rc/at)
   :children [;[boolean-config config "animate?" [:isAnimationActive]]
              [:div "grid (bool strokeDasharray)"]
              [:div "x-axis (bool orientation scale)"]]])


(defn- configurable-chart
  "the chart to draw, taking cues from the settings of the configuration panel

  ---

  - data : (atom) any data used by the component's ui
  - config : (atom) configuration settings made by the user using the config-panel
  "
  [data config]
  (let [grid? (reaction (get-in @config [:grid :include]))
        x-axis? (reaction (get-in @config [:x-axis :include]))
        y-axis? (reaction (get-in @config [:y-axis :include]))
        tooltip? (reaction (get-in @config [:tooltip :include]))
        legend? (reaction (get-in @config [:legend :include]))
        line-uv? (reaction (get-in @config [:line-uv :include]))
        line-pv? (reaction (get-in @config [:line-pv :include]))
        line-amt? (reaction (get-in @config [:line-amt :include]))
        isAnimationActive? (reaction (:isAnimationActive @config))]

    (fn []
      (log/info "configurable-chart" @config)

      [:> LineChart {:width 400 :height 400 :data @data}

       (if @grid? [:> CartesianGrid {:strokeDasharray "3 3"}])

       (if @x-axis? [:> XAxis {:dataKey :name}])

       (if @y-axis? [:> YAxis])

       (if @tooltip? [:> Tooltip])

       (if @legend? [:> Legend])

       (if @line-uv? [:> Line {:type "monotone" :dataKey :uv
                               :isAnimationActive @isAnimationActive?
                               :stroke "#8884d8" :fill "#8884d8"}])

       (if @line-pv? [:> Line {:type "monotone" :dataKey :pv
                               :isAnimationActive @isAnimationActive?
                               :stroke "#82ca9d" :fill "#82ca9d"}])

       (if @line-amt? [:> Line {:type "monotone" :dataKey :amt
                                :isAnimationActive @isAnimationActive?
                                :stroke "#ff00ff"
                                :fill "#ff00ff"}])])))




(defn- data-editor [data]
  [table/table
   :data data
   :max-rows 5
   :width 300])



(defn example []
  (bcu/configurable-demo "Line Chart"
    "Line Charts (working on adding a configuration tool for this)"
    [data-editor data]
    [config-panel config]
    [configurable-chart data config]
    '[layout/centered {:extra-classes :width-50}]))



(comment
  @data

  (reset! data [{:name "Page A" :uv 4000 :pv 2400 :amt 2400}
                {:name "Page B" :uv 3000 :pv 1598 :amt 2210}
                {:name "Page C" :uv 2000 :pv 9800 :amt 2290}
                {:name "Page D" :uv 2780 :pv 3908 :amt 2000}
                {:name "Page E" :uv 1890 :pv 4800 :amt 2181}
                {:name "Page F" :uv 2390 :pv 3800 :amt 2500}
                {:name "Page G" :uv 3490 :pv 4300 :amt 2100}])




  ; original value
  (reset! data [{:name "Page A" :uv 4000 :pv 2400 :amt 2400}
                {:name "Page B" :uv 3000 :pv 1398 :amt 2210}
                {:name "Page C" :uv 2000 :pv 9800 :amt 2290}
                {:name "Page D" :uv 2780 :pv 3908 :amt 2000}
                {:name "Page E" :uv 1890 :pv 4800 :amt 2181}
                {:name "Page F" :uv 2390 :pv 3800 :amt 2500}
                {:name "Page G" :uv 3490 :pv 4300 :amt 2100}])
  ())