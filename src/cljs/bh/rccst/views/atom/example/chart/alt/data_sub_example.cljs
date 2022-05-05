(ns bh.rccst.views.atom.example.chart.alt.data-sub-example
  (:require [bh.rccst.ui-component.molecule.example :as e]
            [bh.rccst.ui-component.utils :as ui-utils]
            [bh.rccst.ui-component.utils.helpers :as h]
            [bh.rccst.views.atom.example.chart.alt.show-data :as sd]
            [re-com.core :as rc]
            [re-frame.core :as re-frame]
            [reagent.core :as r]
            [taoensso.timbre :as log]))


(log/info "bh.rccst.views.atom.example.chart.alt.data-sub-example")


;(def default-data chart/sample-data)


(defn- config [data]
  ; TODO: notice how we need to use '.' instead of '/' for this :topic? how can we fix this
  ; which causes an issue with subscriptions and resolve-value
  {:blackboard {:topic.sample-data data}
   :container  ""})






(defn- data-update-example [& {:keys [data component
                                      config-data data-panel
                                      data-tools
                                      config-panel default-data
                                      container-id component-id] :as params}]
  ;(log/info "data-update-example (params)" params)
  ;(log/info "data-update-example (component)" component "//" data-panel "//" config-panel "//" default-data)

  [rc/v-box :src (rc/at)
   :gap "10px"
   :width "100%"
   :height "100%"
   :children [[:div.chart-part {:style {:width "100%" :height "90%"}}
               [component
                :data data
                :config-data config-data
                :component-id component-id
                :container-id container-id
                :data-panel data-panel
                :config-panel config-panel]]

              [rc/v-box
               :gap "5px"
               :style {:width "100%" :height "30%"}
               :children [[sd/show-data data]
                          [data-tools data default-data]]]]])


(defn- dummy-container [component default-data
                        & {:keys [data component-id container-id data-panel config-panel data-tools] :as params}]
  (let [id (r/atom nil)]

    ;(log/info "component" params)

    (fn []
      (when (nil? @id)
        (reset! id component-id)
        (ui-utils/init-widget @id (config default-data))
        (ui-utils/dispatch-local @id [:container] container-id))

      [data-update-example
       :data data
       :default-data default-data
       :data-tools data-tools
       :component component
       :component-id (h/path->keyword component-id "chart")
       :container-id component-id
       :data-panel data-panel
       :config-panel config-panel])))


(defn example [& {:keys [container-id
                         title description
                         sample-data default-data
                         data-tools
                         source-code
                         component data-panel config-panel]}]

  ;(log/info "example" container-id)

  [e/component-example
   :title title
   :description description
   :data sample-data
   :component (partial dummy-container component default-data)
   :container-id ""
   :component-id container-id
   :source-code source-code
   :extra-params {:default-data default-data
                  :data-tools data-tools
                  :data-panel data-panel
                  :config-panel config-panel}])



; mess with the subscription chain
(comment
  (re-frame/subscribe [:bar-chart-2-data-sub-demo])
  (re-frame/subscribe [:bar-chart-2-data-sub-demo.blackboard])
  (re-frame/subscribe [:bar-chart-2-data-sub-demo.blackboard.topic.sample-data])
  (re-frame/subscribe [:bar-chart-2-data-sub-demo.blackboard.topic.sample-data.metadata])
  (re-frame/subscribe [:bar-chart-2-data-sub-demo.blackboard.topic.sample-data.data])



  (re-frame/subscribe [:bar-chart-2-data-sub-demo :blackboard.topic.sample-data])


  (= @(re-frame/subscribe [:bar-chart-2-data-sub-demo])
    @(re-frame/subscribe [:bar-chart-2-data-sub-demo :blackboard :topic.sample-data]))


  ())


; mess with the data at the subscribed location
(comment
  (def data [:bar-chart-2-data-sub-demo :blackboard :topic.sample-data])

  (def old-data (ui-utils/subscribe-local data [:data]))
  (h/handle-change-path data [:data] (assoc-in @old-data [0 :uv] 10000))


  ())
