(ns bh.rccst.views.catalog.example.colored-pie-chart
  (:require [woolybear.ad.catalog.utils :as acu]
            [woolybear.ad.layout :as layout]
            ["recharts" :refer [PieChart Pie Cell
                                Tooltip Legend]]))

(defn example []
  (let [data (atom [{:name "Group A" :value 400}
                    {:name "Group B" :value 300}
                    {:name "Group C" :value 300}
                    {:name "Group D" :value 200}
                    {:name "Group E" :value 278}
                    {:name "Group F" :value 189}])
        colors ["#ffff00" "#ff0000" "#00ff00" "#0000ff" "#009999" "#ff00ff"]]

    (acu/demo "Colored Pie Chart"
      "Pie Chart with different colors for each slice. This requires embedding `Cell` elements
inside the `Pie` element.

> Note: Recharts supports embedding `Cell` in a variety of other chart types, for example BarChart"
      [layout/centered {:extra-classes :width-50}
       [:> PieChart {:width 400 :height 400 :label true}
        [:> Tooltip]
        [:> Legend]
        [:> Pie {:data @data :label true}
         (map-indexed (fn [idx _]
                        [:> Cell {:key (str "cell-" idx) :fill (get colors idx)}])
           @data)]]]
      '[layout/centered {:extra-classes :width-50}
        [:> PieChart {:width 400 :height 400 :label true}
         [:> Tooltip]
         [:> Legend]
         [:> Pie {:data @data :label true}
          (map-indexed (fn [idx _]
                         [:> Cell {:key (str "cell-" idx) :fill (get colors idx)}])
            @data)]]])))

