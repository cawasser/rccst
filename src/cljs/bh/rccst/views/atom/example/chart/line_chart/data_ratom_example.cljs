(ns bh.rccst.views.atom.example.chart.line-chart.data-ratom-example
  (:require [bh.rccst.ui-component.atom.chart.line-chart-2 :as chart]
            [bh.rccst.ui-component.atom.chart.utils :as chart-utils]
            [bh.rccst.ui-component.molecule.example :as example]
            [bh.rccst.ui-component.utils :as utils]
            [re-com.core :as rc]
            [reagent.core :as r]
            [taoensso.timbre :as log]))

(defonce data (r/atom chart/sample-data))

(defn- data-tools []
  [rc/h-box :src (rc/at)
   :gap "10px"
   :style {:border     "1px solid" :border-radius "3px"
           :box-shadow "5px 5px 5px 2px"
           :margin     "5px" :padding "5px"}
   :children [[rc/button :on-click #(reset! data []) :label "Empty"]
              [rc/button :on-click #(reset! data chart/sample-data) :label "Default"]
              [rc/button :on-click #(swap! data assoc-in [:data 0 :uv] 10000) :label "A -> 10,000"]
              [rc/button :on-click #(swap! data assoc :data
                                           (conj (-> @data :data)
                                             {:name "Page Q" :uv 1100
                                              :pv   1100 :tv 1100 :amt 1100}))
               :label "Add 'Q'"]
              [rc/button :on-click #(swap! data assoc :data (into [] (drop-last 2 (:data @data))))
               :label "Drop Last 2"]
              [rc/button :on-click #(reset! data (-> @data
                                                     (assoc-in [:metadata :fields :new-item] :number)
                                                     (assoc :data (into []
                                                                    (map (fn [x]
                                                                           (assoc x :new-item 1750))
                                                                         (:data @data))))))
               :label "Add :new-item"]]])

(defn- data-update-example [& {:keys [data config-data container-id component-id] :as params}]
  ;(log/info "data-update-example (params)" params)

  [rc/v-box :src (rc/at)
   :class "data-update-example"
   :gap "10px"
   :width "100%"
   :height "100%"
   :children [[:div.chart-part {:style {:width "100%" :height "90%"}}
               [chart/component
                :data data
                :config-data config-data
                :component-id component-id
                :container-id container-id
                :component-panel chart/component
                :data-panel chart-utils/meta-tabular-data-panel
                :config-panel chart/config-panel]]
              [:div.data-tools-part {:style {:width "100%"}}
               [data-tools]]]])

(defn example []
  (let [container-id "line-chart-2-data-ratom-demo"
        component-id (utils/path->keyword container-id "line-chart-2")]
    [example/component-example
     :title "Line Chart 2 (Live Data - ratom)"
     :description "A Line Chart (2) built using [Recharts](https://recharts.org/en-US/api/BarChart). This example shows how
   charts can take [ratoms](http://reagent-project.github.io/docs/master/reagent.ratom.html) as input and re-render as the data changes.

   > In _this_ case, we are using a ratom for the data.
   >
   > You can use the buttons below to change some of the data and see how the chart responds."
     :data data
     :component data-update-example
     :container-id container-id
     :component-id component-id
     :source-code chart/source-code]))
