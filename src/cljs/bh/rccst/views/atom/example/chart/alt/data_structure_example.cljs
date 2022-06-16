(ns bh.rccst.views.atom.example.chart.alt.data-structure-example
  (:require [bh.rccst.ui-component.molecule.example :as example]
            [bh.rccst.ui-component.utils :as utils]
            [reagent.core :as r]
            [taoensso.timbre :as log]))


(log/info "bh.rccst.views.atom.example.chart.alt.data-structure-example")


(defn example [& {:keys [container-id sample-data] :as params}]
  (let [component-id (utils/path->keyword container-id "chart")
        input-params (assoc params :data sample-data
                                   :component-id component-id)]

    (reduce into [example/component-example] (seq input-params))))
