(ns bh.rccst.views.catalog.example.pie-chart
  (:require [woolybear.ad.catalog.utils :as acu]
            [woolybear.ad.layout :as layout]
            ["recharts" :refer [PieChart Pie
                                Tooltip Legend]]))


(defn example []
  (let [data (atom [{:name "Group A" :value 400}
                    {:name "Group B" :value 300}
                    {:name "Group C" :value 300}
                    {:name "Group D" :value 200}
                    {:name "Group E" :value 278}
                    {:name "Group F" :value 189}])]

    (acu/demo "Pie Chart"
      "Pie Chart with a default fill for each slice. Each slice is sized correctly and labeled with the value, but
      they are all the same color.

> See `Colored Pie Chart` for an example of how to get the slices to be different colors."
      [layout/centered {:extra-classes :width-50}
       [:> PieChart {:width 400 :height 400 :label true}
        [:> Tooltip]
        [:> Legend]
        [:> Pie {:dataKey "value" :data @data :fill "#8884d8" :label true}]]]
      '[layout/centered {:extra-classes :width-50}
        [:> PieChart {:width 400 :height 400}
         [:> Tooltip]
         [:> Legend]
         [:> Pie {:dataKey "value" :data @data :fill "#8884d8"}]]])))
