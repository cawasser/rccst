(ns bh.rccst.views.molecule.example.composite.with-fn-example
  (:require [bh.rccst.ui-component.atom.chart.utils :as chart-utils]
            [bh.rccst.ui-component.molecule.composite.with-fn :as widget]
            [bh.rccst.ui-component.molecule.grid-container :as grid]
            [bh.rccst.ui-component.utils :as ui-utils]
            [bh.rccst.ui-component.utils.helpers :as h]
            [re-com.core :as rc]
            [re-frame.core :as re-frame]
            [reagent.core :as r]
            [taoensso.timbre :as log]
            [woolybear.ad.catalog.utils :as acu]
            [woolybear.ad.layout :as layout]))


(log/info "bh.rccst.views.molecule.example.composite.with-fn-example")


(defn- data-tools [data]
  (let [old-data (ui-utils/subscribe-local data [])]

    ;(log/info "data-tools" data "//" @old-data)

    (fn []
      [rc/h-box :src (rc/at)
       :gap "10px"
       :style {:border     "1px solid" :border-radius "3px"
               :box-shadow "5px 5px 5px 2px"
               :margin     "5px" :padding "5px"}
       :children [[:label.h5 "Input Data:"]

                  [rc/button :on-click #(h/handle-change-path data [] []) :label "Empty"]

                  [rc/button :on-click #(h/handle-change-path data [] widget/sample-data)
                   :label "Default"]

                  [rc/button :on-click #(h/handle-change-path data []
                                          (assoc-in @old-data [:data 0 :uv] 10000))
                   :label "A -> 10000"]

                  [rc/button :on-click #(h/handle-change-path data []
                                          (assoc @old-data :data
                                                           (conj (:data @old-data)
                                                             {:name "Page Q" :uv 1100
                                                              :pv   1100 :tv 1100 :amt 1100})))
                   :label "Add 'Q'"]

                  [rc/button :on-click #(h/handle-change-path data []
                                          (assoc @old-data :data
                                                           (into [] (drop-last 2 (:data @old-data)))))
                   :label "Drop Last 2"]

                  [rc/button :on-click #(h/handle-change-path data []
                                          (-> @old-data
                                            (assoc-in [:metadata :fields :new-item] :number)
                                            (assoc :data (into []
                                                           (map (fn [x]
                                                                  (assoc x :new-item (rand-int 7000)))
                                                             (:data @old-data))))))
                   :label "Add :new-item"]]])))


(defn- data-config-update-example [& {:keys [widget component-id] :as params}]
  ;(log/info "config-update-example (params)" params)

  [rc/v-box :src (rc/at)
   :gap "10px"
   :width "100%"
   :height "100%"
   :children [[:div.chart-part {:style {:width "100%"}}     ;:height "90%"}}
               widget]

              [rc/v-box :src (rc/at)
               :gap "8px"
               :children [[data-tools [component-id :blackboard :topic.data]]]]]])


(defn example []
  (let [container-id "chart-with-fn"
        component-id (h/path->keyword container-id "widget")]
    (fn []
      (acu/demo "A Multiple Charts in a Widget (with 'computed' data)"
        "This example provides a 'widget' (collection of UI Components) organized into a digraph (Event Model) that
          describes the flow of data from sources (remote or local) into and out-of the UI.

Here, we show the original data in the line chart, but the bar chart shows the result of `(:uv * :pv) / 1000`
and `(:tv * :amt) / 1000`

"
        [layout/frame
         ;;
         ;; NOTE: the :height MUST be specified here since the ResponsiveContainer down in bowels of the chart needs a height
         ;; in order to actually draw the Recharts components. just saying "100%" doesn't work, since the
         ;; that really means "be as big as you need" and ResponsiveContainer then doesn't know what to do.
         ;;
         [:div {:style {:width "100%" :min-height "400px"}}
          [data-config-update-example
           :widget [grid/component
                    :data (r/atom widget/ui-definition)
                    :component-id component-id
                    :resizable true
                    :tools true]
           :component-id component-id]]]

        widget/source-code))))

