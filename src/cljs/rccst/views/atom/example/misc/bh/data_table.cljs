(ns rccst.views.atom.example.misc.bh.data-table
  (:require [bh.ui-component.atom.bh.data-table :as table]
            [re-com.core :as rc]
            [reagent.core :as r]
            [taoensso.timbre :as log]
            [woolybear.ad.catalog.utils :as acu]
            [woolybear.ad.layout :as layout]))

(log/info "rccst.views.atom.example.misc.bh.data-table")


(defonce data (r/atom table/sample-data))


(defn- data-tools []
  [rc/h-box :src (rc/at)
   :gap "10px"
   :style {:border     "1px solid" :border-radius "3px"
           :box-shadow "5px 5px 5px 2px"
           :margin     "5px" :padding "5px"}
   :children [[:label.h5 "Input Data:"]
              [rc/button :on-click #(reset! data []) :label "Empty"]
              [rc/button :on-click #(reset! data table/sample-data) :label "Default"]
              [rc/button :on-click #(swap! data assoc-in [0 :uv] 10000) :label "A -> 10,000"]
              [rc/button :on-click #(swap! data conj {:name "Page Q" :uv 1100
                                                      :pv   1100 :tv 1100 :amt 1100})
               :label "Add 'Q'"]
              [rc/button :on-click #(reset! data (into [] (drop-last 2 @data)))
               :label "Drop Last 2"]
              [rc/button :on-click #(reset! data (into []
                                                   (map (fn [x]
                                                          (assoc x :new-item 1750)) @data)))
               :label "Add :new-item"]]])


(defn- data-update-example [& {:keys [data config-data container-id component-id] :as params}]
  ;(log/info "data-update-example (params)" params)

  [rc/v-box :src (rc/at)
   :class "data-update-example"
   :gap "10px"
   :width "100%"
   :height "100%"
   :children [[:div.chart-part {:style {:width "100%" :height "90%"}}
               [table/table :data data :config-data config-data]]
              [:div.data-tools-part {:style {:width "100%"}}
               [data-tools]]]])


(defn example []
  (let [component-id "data-table-demo"]
    (acu/demo
      "Data Table"
      "Table using [fixed-data-table](https://github.com/schrodinger/fixed-data-table-2)  tags. data comes from a ratom

> No meta-data. Feel free to use the controls at the bottom to change the data and see how the Table responds."
      [layout/centered {:extra-classes :width-50}
       [data-update-example
        :data data
        :config-data (r/atom {:config "data"})]]

      '[table/table :data table/sample-data])))

