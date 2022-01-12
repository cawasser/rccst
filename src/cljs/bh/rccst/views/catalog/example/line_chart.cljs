(ns bh.rccst.views.catalog.example.line-chart
  (:require [woolybear.ad.catalog.utils :as acu]
            [woolybear.ad.layout :as layout]
            ["recharts" :refer [LineChart Line
                                XAxis YAxis CartesianGrid
                                Tooltip Legend]]))


(defn example []
  (let [data (atom [{:name "Page A" :uv 4000 :pv 2400 :amt 2400}
                    {:name "Page B" :uv 3000 :pv 1398 :amt 2210}
                    {:name "Page C" :uv 2000 :pv 9800 :amt 2290}
                    {:name "Page D" :uv 2780 :pv 3908 :amt 2000}
                    {:name "Page E" :uv 1890 :pv 4800 :amt 2181}
                    {:name "Page F" :uv 2390 :pv 3800 :amt 2500}
                    {:name "Page G" :uv 3490 :pv 4300 :amt 2100}])]

    (acu/demo "Line Chart"
      "Line Charts (this would be really cool with support for changing options live)"
      [layout/centered {:extra-classes :width-50}
       [:> LineChart {:width 400 :height 400 :data @data}
        [:> CartesianGrid {:strokeDasharray "3 3"}]
        [:> XAxis {:dataKey "title"}]
        [:> YAxis]
        [:> Tooltip]
        [:> Legend]
        [:> Line {:type "monotone" :dataKey "uv" :stroke "#8884d8" :fill "#8884d8"}]
        [:> Line {:type "monotone" :dataKey "pv" :stroke "#040408" :fill "#040408"}]]]
      '[layout/centered {:extra-classes :width-50}
        [:> LineChart {:width 400 :height 400 :data @data}
         [:> CartesianGrid {:strokeDasharray "3 3"}]
         [:> XAxis {:dataKey "title"}]
         [:> YAxis]
         [:> Tooltip]
         [:> Legend]
         [:> Line {:type "monotone" :dataKey "uv" :stroke "#8884d8" :fill "#8884d8"}]
         [:> Line {:type "monotone" :dataKey "pv" :stroke "#040408" :fill "#040408"}]]])))
