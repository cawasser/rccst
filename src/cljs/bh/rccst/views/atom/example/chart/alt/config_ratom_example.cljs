(ns bh.rccst.views.atom.example.chart.alt.config-ratom-example
  (:require [bh.rccst.ui-component.atom.chart.utils :as chart-utils]
            [bh.rccst.ui-component.molecule.example :as example]
            [bh.rccst.ui-component.utils :as utils]
            [bh.rccst.views.atom.example.chart.alt.show-data :as sd]
            [re-com.core :as rc]
            [reagent.core :as r]
            [taoensso.timbre :as log]))


(log/info "bh.rccst.views.atom.example.chart.alt.config-ratom-example")


(defn- config-update-example [component default-config-data
                              & {:keys [data config-data config-tools container-id component-id] :as params}]
  ;(log/info "config-update-example (params)" params)

  [rc/v-box :src (rc/at)
   :gap "10px"
   :width "100%"
   :height "100%"
   :children [[:div.chart-part {:style {:width "100%" :height "70%"}}
               [component
                :data data
                :config-data config-data
                :component-id component-id
                :container-id container-id]]

              [rc/v-box
               :gap "5px"
               :style {:width "100%" :height "30%"}
               :children [[sd/show-data config-data]
                          [config-tools config-data default-config-data]]]]])


(defn example [& {:keys [container-id
                         title description
                         sample-data default-config-data
                         config-tools
                         source-code
                         component]}]
  (let [component-id (utils/path->keyword container-id "chart")
        data (r/atom sample-data)]

    [example/component-example
     :title title
     :description description
     :data data
     :extra-params {:config-data (r/atom default-config-data)
                    :config-tools config-tools}
     :component (partial config-update-example component default-config-data)
     :container-id container-id
     :component-id component-id
     :source-code source-code]))


