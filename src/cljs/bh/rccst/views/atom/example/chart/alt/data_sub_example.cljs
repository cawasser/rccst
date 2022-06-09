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
  ; notice how we need to use '.' instead of '/' for this :topic?
  ; which causes an issue with subscriptions and resolve-value
  {:blackboard {:topic.sample-data data}
   :container  ""})


(defn- data-update-example [& {:keys [data default-data component data-tools] :as params}]
  ;(log/info "data-update-example (params)" params)
  ;(log/info "data-update-example (component)" component "//" data-panel "//" config-panel "//" default-data)

  [rc/v-box :src (rc/at)
   :gap "10px"
   :width "100%"
   :height "100%"
   :children [[:div.chart-part {:style {:width "100%" :height "90%"}}
               (reduce into [component] (seq params))]

              [rc/v-box
               :gap "5px"
               :style {:width "100%" :height "30%"}
               :children [[sd/show-data data]
                          [data-tools data default-data]]]]])


(defn- dummy-container [component default-data
                        & {:keys [component-id container-id] :as params}]
  (let [id           (r/atom nil)
        input-params (assoc params :component-id (h/path->keyword component-id "chart")
                                   :container-id component-id)]

    ;(log/info "dummy-container" component "//" params)

    (fn []
      (when (nil? @id)
        (reset! id component-id)
        (ui-utils/init-container-locals @id (config default-data))
        (ui-utils/dispatch-local @id [:container] container-id))

      (reduce into [data-update-example :component component] (seq input-params)))))


(defn example [& {:keys [container-id
                         sample-data default-data
                         component] :as params}]

  ;(log/info "example" container-id params)

  (let [input-params (assoc params :data sample-data
                                   :component-id container-id
                                   :container-id ""
                                   :component (partial dummy-container component default-data)
                                   :default-data default-data)]

    (reduce into [e/component-example] (seq input-params))))



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
  (def data [:area-chart-2-data-sub-demo :blackboard :topic.sample-data])
  (def old-data (ui-utils/subscribe-local data [:data]))


  (h/handle-change-path data [:data]
    (assoc-in @(ui-utils/subscribe-local data [:data]) [0 :uv] 10000))


  ())
