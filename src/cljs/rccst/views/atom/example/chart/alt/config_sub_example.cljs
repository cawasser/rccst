(ns rccst.views.atom.example.chart.alt.config-sub-example
  (:require [bh.ui-component.molecule.example :as e]
            [bh.ui-component.utils :as ui-utils]
            [bh.ui-component.utils.helpers :as h]
            [rccst.views.atom.example.chart.alt.show-data :as sd]
            [re-com.core :as rc]
            [reagent.core :as r]
            [taoensso.timbre :as log]))

(log/info "rccst.views.atom.example.chart.bar-chart.data-sub-example")


(defn- config [config-data]
  {:blackboard {:config-data config-data}
   :container  ""})


(defn- config-update-example [& {:keys [component config-data config-tools default-config-data] :as params}]
  ;(log/info "config-update-example (params)" params)

  [rc/v-box :src (rc/at)
   :gap "10px"
   :width "100%"
   :height "100%"
   :children [[:div.chart-part {:style {:width "100%" :height "70%"}}
               (reduce into [component] (seq params))]

              [rc/v-box
               :gap "5px"
               :style {:width "100%" :height "30%"}
               :children [[sd/show-data config-data]
                          [config-tools config-data default-config-data]]]]])


(defn- dummy-container [component default-config-data
                        & {:keys [component-id container-id] :as params}]

  ;(log/info "component" default-config-data "//" params)

  (let [id (r/atom nil)
        input-params (assoc params :component-id (h/path->keyword component-id "chart"))]

    (fn []
      (when (nil? @id)
        (reset! id component-id)
        (ui-utils/init-container-locals @id (config default-config-data))
        (ui-utils/dispatch-local @id [:container] container-id))

      (reduce into [config-update-example :component component] (seq input-params)))))


(defn example [& {:keys [container-id
                         sample-data default-config-data
                         component] :as params}]

  ;(log/info "example" params)

  (let [input-params (assoc params :data (r/atom sample-data)
                                   :component (partial dummy-container component default-config-data)
                                   :container-id ""
                                   :component-id container-id)]
    (reduce into [e/component-example] (seq input-params))))


