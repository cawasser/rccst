(ns bh.rccst.views.atom.example.chart.alt.config-sub-example
  (:require [bh.rccst.ui-component.molecule.example :as e]
            [bh.rccst.ui-component.utils :as ui-utils]
            [bh.rccst.ui-component.utils.helpers :as h]
            [bh.rccst.views.atom.example.chart.alt.show-data :as sd]
            [re-com.core :as rc]
            [reagent.core :as r]
            [taoensso.timbre :as log]))

(log/info "bh.rccst.views.atom.example.chart.bar-chart.data-sub-example")


(defn- config [config-data]
  ; TODO: notice how we need to use '.' instead of '/' for this :topic? how can we fix this
  ; which causes an issue with subscriptions and resolve-value
  {:blackboard {:config-data config-data}
   :container  ""})


(defn- config-update-example [& {:keys [data component config-data config-tools default-config-data
                                        container-id component-id] :as params}]
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


(defn- dummy-container [component default-config-data
                        & {:keys [data config-data config-tools component-id container-id] :as params}]

  ;(log/info "component" default-config-data "//" params)

  (let [id (r/atom nil)]

    (fn []
      (when (nil? @id)
        (reset! id component-id)
        (ui-utils/init-widget @id (config default-config-data))
        (ui-utils/dispatch-local @id [:container] container-id))

      [config-update-example
       :data data
       :component component
       :config-data config-data
       :config-tools config-tools
       :default-config-data default-config-data
       :component-id (h/path->keyword component-id "chart")
       :container-id component-id])))


(defn example [& {:keys [container-id
                         title description
                         sample-data
                         config-data default-config-data
                         config-tools
                         source-code
                         component] :as params}]

  ;(log/info "example" params)

  [e/component-example
   :title title
   :description description
   :data (r/atom sample-data)
   :extra-params {:config-data  config-data
                  :config-tools config-tools}
   :component (partial dummy-container component default-config-data)
   :container-id ""
   :component-id container-id
   :source-code source-code])


