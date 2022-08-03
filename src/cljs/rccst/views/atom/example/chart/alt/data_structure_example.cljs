(ns rccst.views.atom.example.chart.alt.data-structure-example
  (:require [rccst.views.atom.utils :as example]
            [bh.ui-component.utils :as utils]
            [reagent.core :as r]
            [taoensso.timbre :as log]))


(log/info "rccst.views.atom.example.chart.alt.data-structure-example")


(defn example [& {:keys [container-id sample-data] :as params}]
  (let [component-id (utils/path->keyword container-id "chart")
        input-params (assoc params :data sample-data
                                   :component-id component-id)]

    (reduce into [example/component-example] (seq input-params))))
