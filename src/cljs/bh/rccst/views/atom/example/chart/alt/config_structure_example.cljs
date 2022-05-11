(ns bh.rccst.views.atom.example.chart.alt.config-structure-example
  (:require [bh.rccst.ui-component.molecule.example :as example]
            [bh.rccst.ui-component.utils :as utils]
            [bh.rccst.ui-component.utils.helpers :as h]
            [bh.rccst.views.atom.example.chart.alt.show-data :as sd]
            [re-com.core :as rc]
            [reagent.core :as r]
            [taoensso.timbre :as log]))


(log/info "bh.rccst.views.atom.example.chart.bar-chart.config-structure-example")



(defn- config-example [component
                       & {:keys [data config-data container-id component-id] :as params}]
  ;(log/info "config-example (params)" params)

  [rc/v-box :src (rc/at)
   :class "config-example"
   :gap "10px"
   :width "100%"
   :height "100%"
   :children [[:div.chart-part {:style {:width "100%" :height "90%"}}
               [component
                :data data
                :config-data config-data
                :component-id component-id
                :container-id container-id]]

              [sd/show-config config-data]]])


(defn example [& {:keys [container-id
                         title description
                         sample-data default-config-data
                         source-code
                         component]}]
  (let [component-id (utils/path->keyword container-id "chart")
        data         (r/atom sample-data)]

    [example/component-example
     :title title
     :description description
     :data data
     :extra-params {:config-data default-config-data}
     :component (partial config-example component)
     :container-id container-id
     :component-id component-id
     :source-code source-code]))


