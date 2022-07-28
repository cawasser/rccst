(ns rccst.views.atom.example.misc.bh.meta-bh-table
  (:require [bh.ui-component.atom.bh.table :as table]
            [bh.ui-component.utils :as ui-utils]
            [bh.ui-component.utils.helpers :as h]
            [reagent.core :as r]
            [re-com.core :as rc]
            [taoensso.timbre :as log]
            [woolybear.ad.catalog.utils :as acu]
            [woolybear.ad.layout :as layout]))


(log/info "rccst.views.atom.example.misc.bh.meta-bh-table")


(defonce data (r/atom table/sample-meta-data))


(defn- data-tools []
  [rc/h-box :src (rc/at)
   :gap "10px"
   :style {:border     "1px solid" :border-radius "3px"
           :box-shadow "5px 5px 5px 2px"
           :margin     "5px" :padding "5px"}
   :children [[:label.h5 "Input Data:"]
              [rc/button :on-click (rc/handler-fn (reset! data [])) :label "Empty"]
              [rc/button :on-click #(reset! data table/sample-meta-data) :label "Default"]
              [rc/button :on-click #(reset! data (table/random-data-meta)) :label "Random"]
              [rc/button :on-click #(swap! data assoc-in [:data 0 :uv] 10000) :label "A -> 10,000"]
              [rc/button :on-click #(swap! data assoc :data (conj (:data @data)
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


(defn- data-update-example [& {:keys [data container-id component-id] :as params}]
  ;(log/info "data-update-example (params)" params)

  (let [d (h/resolve-value data)]
    (fn []
      [rc/v-box :src (rc/at)
       :class "data-update-example"
       :gap "10px"
       :width "100%"
       :height "100%"
       :children [[:div.chart-part {:style {:width "100%" :height "90%"}}
                   [table/table :data d]]
                  [:div.data-tools-part {:style {:width "100%"}}
                   [data-tools]]]])))



(defn example []
  (let [component-id "table-with-meta-demo"]
    (acu/demo "Basic Table with meta-data"
      "Table using HTML tags. Data is provided in a ratom.

Now with meta-data!

> This example also takes advantage of the component's ability to present data that it gets
> from it's parent either
> directly, via a Clojure [atom](https://clojure.org/reference/atoms), or via a
> re-frame [subscription](https://day8.github.io/re-frame/subscriptions/) to state held
> by the parent (container).

> Feel free to use the controls at the bottom to change the data and see how the Table responds."

      [layout/centered {:extra-classes :width-50}
       [data-update-example :data data]]

      '[table/table :data table/sample-meta-data])))

