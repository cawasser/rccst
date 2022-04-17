(ns bh.rccst.views.atom.example.chart.bar-chart-2
  (:require [bh.rccst.ui-component.atom.chart.bar-chart-2 :as chart]
            [bh.rccst.ui-component.molecule.example :as example]
            [bh.rccst.ui-component.atom.chart.utils :as chart-utils]
            [bh.rccst.ui-component.utils :as utils]
            [reagent.core :as r]
            [taoensso.timbre :as log]))


(log/info "bh.rccst.views.atom.example.chart.bar-chart-2")


(defonce  data (r/atom chart/sample-data))

(defn example []
  (let [container-id "bar-chart-2-demo"]
    [example/component-example
     :title "Bar Chart 2"
     :description "An EMPTY Bar Chart (2) built using [Recharts](https://recharts.org/en-US/api/BarChart)"
     :data data ;chart/sample-data
     :component chart/component
     :extra-params {:component* chart/component-panel*
                    :config chart/config
                    :local-config chart/local-config
                    :data-panel chart-utils/meta-tabular-data-panel
                    :config-panel chart/config-panel}
     :container-id container-id
     :component-id (utils/path->keyword container-id "bar-chart-2")
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
       :pv 1100 :tv 1100 :amt 1100}))

  (swap! data assoc :data (drop-last 2
                            (:data @data)))

  (reset! data (-> @data
                 (assoc-in [:metadata :fields :new-item] :number)
                 (assoc :data (map #(assoc % :new-item 1750)
                                (:data @data)))))



  ())
