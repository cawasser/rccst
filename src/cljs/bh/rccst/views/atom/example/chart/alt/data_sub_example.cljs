(ns bh.rccst.views.atom.example.chart.alt.data-sub-example
  (:require [bh.rccst.ui-component.molecule.example :as e]
            [bh.rccst.ui-component.utils :as ui-utils]
            [bh.rccst.ui-component.utils.helpers :as h]
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


(defn- show-data [data]
  (let [d (h/resolve-value data)]
    (fn []

      ;(log/info "show-data" data "//" d "//" @d)

      [rc/h-box :src (rc/at)
       :gap "10px"
       :style {:border     "1px solid" :border-radius "3px"
               :background "#808080"
               :box-shadow "5px 5px 5px 2px"
               :margin     "5px" :padding "5px"}
       :children [[:p {:style {:color "white"}}
                   (str @d)]]])))


(defn- data-tools [data default-data]
  (let [old-data (ui-utils/subscribe-local data [:data])
        old-meta (ui-utils/subscribe-local data [])]

    (fn []
      [rc/h-box :src (rc/at)
       :gap "10px"
       :style {:border     "1px solid" :border-radius "3px"
               :box-shadow "5px 5px 5px 2px"
               :margin     "5px" :padding "5px"}
       :children [[:label.h5 "Input Data:"]

                  [rc/button :on-click #(h/handle-change-path data [] []) :label "Empty"]

                  [rc/button :on-click #(h/handle-change-path data [] default-data)
                   :label "Default"]

                  [rc/button :on-click #(h/handle-change-path data [:data]
                                          (assoc-in @old-data [0 :uv] 10000))
                   :label "A -> 10000"]

                  [rc/button :on-click #(h/handle-change-path data [:data]
                                          (conj @old-data
                                            {:name "Page Q" :uv 1100
                                             :pv   1100 :tv 1100 :amt 1100}))
                   :label "Add 'Q'"]

                  [rc/button :on-click #(h/handle-change-path data [:data]
                                          (into [] (drop-last 2 @old-data)))
                   :label "Drop Last 2"]

                  [rc/button :on-click #(h/handle-change-path data []
                                          (-> @old-meta
                                            (assoc-in [:metadata :fields :new-item] :number)
                                            (assoc :data (into []
                                                           (map (fn [x]
                                                                  (assoc x :new-item (rand-int 7000)))
                                                             @old-data)))))
                   :label "Add :new-item"]]])))


(defn- data-update-example [& {:keys [data component
                                      config-data data-panel
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

              [show-data data]

              [:div.config-tools-part {:style {:width "100%"}}
               [data-tools data default-data]]]])


(defn- component-wedge [component data-panel config-panel default-data
                        & {:keys [data component-id container-id] :as params}]
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
       :component component
       :component-id (h/path->keyword component-id "chart")
       :container-id component-id
       :data-panel data-panel
       :config-panel config-panel])))


(defn example [& {:keys [container-id
                         title description
                         sample-data default-data source-code
                         component data-panel config-panel]}]

  ;(log/info "example" container-id)

  [e/component-example
   :title title
   :description description
   :data sample-data
   :component (partial component-wedge component data-panel config-panel default-data)
   :container-id ""
   :component-id container-id
   :source-code source-code
   :extra-params {:default-data default-data
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