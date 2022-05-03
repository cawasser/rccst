(ns bh.rccst.views.atom.example.chart.alt.config-ratom-example
  (:require [bh.rccst.ui-component.atom.chart.utils :as chart-utils]
            [bh.rccst.ui-component.molecule.example :as example]
            [bh.rccst.ui-component.utils :as utils]
            [re-com.core :as rc]
            [reagent.core :as r]
            [taoensso.timbre :as log]))


(log/info "bh.rccst.views.atom.example.chart.alt.config-ratom-example")


;(def default-config-data {:brush false
;                          :uv    {:include true, :fill "#ff0000", :stackId ""}
;                          :pv    {:include true, :fill "#00ff00", :stackId ""}
;                          :tv    {:include true, :fill "#0000ff", :stackId ""}
;                          :amt   {:include true, :fill "#745ea5", :stackId ""}})
;(defonce config-data (r/atom default-config-data))



(defn- show-config [config-data]
  [rc/h-box :src (rc/at)
   :gap "10px"
   :style {:border     "1px solid" :border-radius "3px"
           :background "#808080"
           :box-shadow "5px 5px 5px 2px"
           :margin     "5px" :padding "5px"}
   :children [[:p {:style {:color "white"}}
               (str @config-data)]]])


(defn- config-tools [config-data default-config-data]
  [rc/h-box :src (rc/at)
   :gap "10px"
   :style {:border     "1px solid" :border-radius "3px"
           :box-shadow "5px 5px 5px 2px"
           :margin     "5px" :padding "5px"}
   :children [[:label.h5 "Config:"]
              [rc/button :on-click #(reset! config-data default-config-data) :label "Default"]
              [rc/button :on-click #(swap! config-data update-in [:brush] not) :label "!Brush"]
              [rc/button :on-click #(swap! config-data update-in [:uv :include] not) :label "! uv data"]
              [rc/button :on-click #(swap! config-data update-in [:tv :include] not) :label "! tv data"]
              [chart-utils/color-config config-data ":amt :fill" [:amt :fill] :above-center]
              [rc/button :on-click #(reset! config-data (-> @config-data
                                                          (assoc-in [:uv :stackId] "a")
                                                          (assoc-in [:tv :stackId] "a")))
               :label "stack"]
              [rc/button :on-click #(reset! config-data (-> @config-data
                                                          (assoc-in [:uv :stackId] "")
                                                          (assoc-in [:tv :stackId] "")))
               :label "un-stack"]]])


(defn- config-update-example [component default-config-data
                              & {:keys [data config-data container-id component-id] :as params}]
  ;(log/info "config-update-example (params)" params)

  [rc/v-box :src (rc/at)
   :gap "10px"
   :width "100%"
   :height "100%"
   :children [[:div.chart-part {:style {:width "100%" :height "90%"}}
               [component
                :data data
                :config-data config-data
                :component-id component-id
                :container-id container-id]]

              [show-config config-data]

              [:div.config-tools-part {:style {:width "100%"}}
               [config-tools config-data default-config-data]]]])


(defn example [& {:keys [container-id
                         title description
                         sample-data default-config-data
                         source-code
                         component]}]
  (let [component-id (utils/path->keyword container-id "chart")
        data (r/atom sample-data)]

    [example/component-example
     :title title
     :description description
     :data data
     :extra-params {:config-data (r/atom default-config-data)}
     :component (partial config-update-example component default-config-data)
     :container-id container-id
     :component-id component-id
     :source-code source-code]))


