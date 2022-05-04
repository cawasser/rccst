(ns bh.rccst.views.atom.example.chart.alt.data-ratom-example
  (:require [bh.rccst.ui-component.molecule.example :as example]
            [bh.rccst.ui-component.utils :as utils]
            [re-com.core :as rc]
            [reagent.core :as r]
            [taoensso.timbre :as log]))


(log/info "bh.rccst.views.atom.example.chart.alt.data-ratom-example")


(defn- data-tools [data default-data]
  [rc/h-box :src (rc/at)
   :gap "10px"
   :style {:border     "1px solid" :border-radius "3px"
           :box-shadow "5px 5px 5px 2px"
           :margin     "5px" :padding "5px"}
   :children [[:label.h5 "Input Data:"]
              [rc/button :on-click #(reset! data []) :label "Empty"]
              [rc/button :on-click #(reset! data default-data) :label "Default"]
              [rc/button :on-click #(swap! data assoc-in [:data 0 :uv] 10000) :label "A -> 10,000"]
              [rc/button :on-click #(swap! data assoc :data
                                      (conj (-> @data :data)
                                        {:name "Page Q" :uv 1100
                                         :pv   1100 :tv 1100 :amt 1100}))
               :label "Add 'Q'"]
              [rc/button :on-click #(swap! data assoc :data (into [] (drop-last 2 (:data @data))))
               :label "Drop Last 2"]
              [rc/button :on-click #(reset! data (-> @data
                                                   (assoc-in [:metadata :fields :new-item] :number)
                                                   (assoc :data (into []
                                                                  (map (fn [x]
                                                                         (assoc x :new-item (rand-int 7000)))
                                                                    (:data @data))))))
               :label "Add :new-item"]]])


(defn- data-update-example [component data-panel config-panel default-data
                            & {:keys [data config-data container-id component-id] :as params}]

  ;(log/info "data-update-example (params)" params)
  ;(log/info "data-update-example" default-data)

  [rc/v-box :src (rc/at)
   :class "data-update-example"
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
              [:div.data-tools-part {:style {:width "100%"}}
               [data-tools data default-data]]]])


(defn example [& {:keys [container-id
                         title description
                         sample-data source-code
                         component data-panel config-panel]}]
  (let [component-id (utils/path->keyword container-id "chart")
        data         (r/atom sample-data)]

    [example/component-example
     :title title
     :description description
     :data data
     :component (partial data-update-example component data-panel config-panel sample-data)
     :container-id container-id
     :component-id component-id
     :source-code source-code]))





