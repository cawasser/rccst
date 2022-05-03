(ns bh.rccst.views.atom.example.chart.alt.data-structure-example
  (:require [bh.rccst.ui-component.molecule.example :as example]
            [bh.rccst.ui-component.utils :as utils]
            [reagent.core :as r]
            [taoensso.timbre :as log]))


(log/info "bh.rccst.views.atom.example.chart.alt.data-structure-example")


(defn example [& {:keys [container-id
                         title description
                         sample-data source-code
                         component data-panel config-panel]}]
  (let [component-id (utils/path->keyword container-id "chart")
        data (r/atom sample-data)]

    [example/component-example
     :title title
     :description  description
     :data sample-data
     :component component
     :container-id container-id
     :component-id component-id
     :extra-params {:data-panel data-panel
                    :config-panel config-panel}
     :source-code source-code]))
