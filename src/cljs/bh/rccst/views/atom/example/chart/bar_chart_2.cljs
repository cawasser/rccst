(ns bh.rccst.views.atom.example.chart.bar-chart-2
  (:require [bh.rccst.ui-component.atom.chart.bar-chart-2 :as chart]
            [bh.rccst.ui-component.atom.chart.utils :as chart-utils]
            [bh.rccst.ui-component.molecule.example :as example]
            [bh.rccst.ui-component.utils :as utils]
            [re-com.core :as rc]
            [reagent.core :as r]
            [taoensso.timbre :as log]))


(log/info "bh.rccst.views.atom.example.chart.bar-chart-2")


(defonce data (r/atom chart/sample-data))


(defn- data-update-example [& {:keys [data container-id component-id] :as params}]
  (log/info "data-update-example" params)

  [rc/v-box :src (rc/at)
   :gap "10px"
   :children [[chart/component
               :data data
               :component-id component-id
               :container-id container-id
               :component* chart/component-panel*
               :component-panel chart/component
               :data-panel chart-utils/meta-tabular-data-panel
               :config-panel chart/config-panel]
              [rc/h-box :src (rc/at)
               :gap "10px"
               :style {:border     "1px solid" :border-radius "3px"
                       :box-shadow "5px 5px 5px 2px"
                       :margin     "5px" :padding "5px"}
               :children [[:button {:on-click #(reset! data [])} "Empty"]
                          [:button {:on-click #(reset! data chart/sample-data)} "Default"]
                          [:button {:on-click #(swap! data assoc-in [:data 0 :uv] 10000)} "A -> 10,000"]
                          [:button {:on-click #(swap! data
                                                 assoc :data
                                                 (conj (-> @data :data)
                                                   {:name "Page Q" :uv 1100
                                                    :pv   1100 :tv 1100 :amt 1100}))}
                           "Add 'Q'"]
                          [:button {:on-click #(swap! data assoc :data (drop-last 2
                                                                         (:data @data)))}
                           "Drop Last 2"]
                          [:button {:on-click #(reset! data (-> @data
                                                              (assoc-in [:metadata :fields :new-item] :number)
                                                              (assoc :data (map (fn [x]
                                                                                  (assoc x :new-item 1750))
                                                                             (:data @data)))))}
                           "Add :new-item"]]]]])


(defn example []
  (let [container-id "bar-chart-2-demo"
        component-id (utils/path->keyword container-id "bar-chart-2")]
    [example/component-example
     :title "Bar Chart 2"
     :description "A Bar Chart (2) built using [Recharts](https://recharts.org/en-US/api/BarChart). This example shows how
     charts can take [ratoms]() as input and re-render as the data changes."
     :data data                                             ;chart/sample-data
     :component data-update-example                         ;chart/component
     :extra-params {:component*   chart/component-panel*
                    :data-panel   chart-utils/meta-tabular-data-panel
                    :config-panel chart/config-panel}
     :container-id container-id
     :component-id component-id
     :source-code chart/source-code]))


(comment

  (reset! data [])
  (reset! data chart/sample-data)



  (assoc-in {:dummy '({:one 1})} [:dummy 0 :new] 1000)
  (assoc-in @data [:data 0 :uv] 10000)
  (-> @data
    (assoc-in [:metadata :fields :new] :number)
    (assoc :data (map #(assoc % :new 1750) (:data @data))))



  (swap! data assoc-in [:data 0 :uv] 10000)

  (swap! data
    assoc :data
    (conj (-> @data :data)
      {:name "Page Q" :uv 1100
       :pv   1100 :tv 1100 :amt 1100}))

  (swap! data assoc :data (drop-last 2
                            (:data @data)))

  (reset! data (-> @data
                 (assoc-in [:metadata :fields :new-item] :number)
                 (assoc :data (map #(assoc % :new-item 1750)
                                (:data @data)))))



  ())
