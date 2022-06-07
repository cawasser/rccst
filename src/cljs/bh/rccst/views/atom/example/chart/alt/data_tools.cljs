(ns bh.rccst.views.atom.example.chart.alt.data-tools
  (:require [bh.rccst.ui-component.utils :as ui-utils]
            [bh.rccst.ui-component.utils.helpers :as h]
            [re-com.core :as rc]
            [taoensso.timbre :as log]))


(log/info "bh.rccst.views.atom.example.chart.alt.data-tools")


(defn meta-tabular-data-ratom-tools [data default-data]
  [rc/h-box :src (rc/at)
   :gap "10px"
   :style {:border     "1px solid" :border-radius "3px"
           :box-shadow "5px 5px 5px 2px"
           :margin     "5px" :padding "5px"}
   :children [[:label.h5 "Input Data:"]
              [rc/button :label "Empty" :on-click #(reset! data []) :label "Empty"]
              [rc/button :label "Default" :on-click #(reset! data default-data)]
              [rc/button :label "A(uv) -> 10,000" :on-click #(swap! data assoc-in [:data 0 :uv] 10000)]
              [rc/button :label "Add 'Q'"
               :on-click #(swap! data assoc :data
                            (conj (-> @data :data)
                              {:name "Page Q" :uv 1100
                               :pv   1100 :tv 1100 :amt 1100}))]

              [rc/button :label "Drop Last 2"
               :on-click #(swap! data assoc :data (into [] (drop-last 2 (:data @data))))]

              [rc/button :label "Add :new-item"
               :on-click #(reset! data (-> @data
                                         (assoc-in [:metadata :fields :new-item] :number)
                                         (assoc :data (into []
                                                        (map (fn [x]
                                                               (assoc x :new-item (rand-int 7000)))
                                                          (:data @data))))))]]])


(defn meta-tabular-data-sub-tools [data default-data]

  (let [old-data (ui-utils/subscribe-local data [:data])
        old-meta (ui-utils/subscribe-local data [])]

    (fn []
      [rc/h-box :src (rc/at)
       :gap "10px"
       :style {:border     "1px solid" :border-radius "3px"
               :box-shadow "5px 5px 5px 2px"
               :margin     "5px" :padding "5px"}
       :children [[:label.h5 "Input Data:"]

                  [rc/button :label "Empty" :on-click #(h/handle-change-path data [] [])]

                  [rc/button :label "Default"
                   :on-click #(h/handle-change-path data [] default-data)]

                  [rc/button :label "A(uv) -> 10000"
                   :on-click #(h/handle-change-path data [:data]
                                (assoc-in @old-data [0 :uv] 10000))]

                  [rc/button :label "Add 'Q'"
                   :on-click #(h/handle-change-path data [:data]
                                (conj @old-data
                                  {:name "Page Q" :uv 1100
                                   :pv   1100 :tv 1100 :amt 1100}))]

                  [rc/button :label "Drop Last 2"
                   :on-click #(h/handle-change-path data [:data]
                                (into [] (drop-last 2 @old-data)))]

                  [rc/button :label "Add :new-item"
                   :on-click #(h/handle-change-path data []
                                (-> @old-meta
                                  (assoc-in [:metadata :fields :new-item] :number)
                                  (assoc :data (into []
                                                 (map (fn [x]
                                                        (assoc x :new-item (rand-int 7000)))
                                                   @old-data)))))]]])))


(defn dag-data-ratom-tools [data default-data]
  [rc/h-box :src (rc/at)
   :gap "10px"
   :style {:border     "1px solid" :border-radius "3px"
           :box-shadow "5px 5px 5px 2px"
           :margin     "5px" :padding "5px"}
   :children [[:label.h5 "Input Data:"]

              [rc/button :label "Empty" :on-click #(reset! data [])]

              [rc/button :label "Default" :on-click #(reset! data default-data)]

              [rc/button :label "! Redirect" :on-click #(log/info "Add a 'Redirect' node and link to 'Detail-Favourite'")]

              [rc/button :label "! Dummy->New-thing"
               :on-click #(log/info "Add 'Dummy' & 'New-thing' nodes with link between")]

              [rc/button :label "Change link value"
               :on-click #(log/info "Change link value")]]])


(defn dag-data-sub-tools [data default-data]
  [rc/h-box :src (rc/at)
   :gap "10px"
   :style {:border     "1px solid" :border-radius "3px"
           :box-shadow "5px 5px 5px 2px"
           :margin     "5px" :padding "5px"}
   :children [[:label.h5 "Input Data:"]

              [rc/button :label "Empty" :on-click #(h/handle-change-path data [] [])]

              [rc/button :label "Default" :on-click #(h/handle-change-path data [] default-data)]

              [rc/button :label "! Redirect" :on-click #(log/info "Add a 'Redirect' node and link")]

              [rc/button :label "! Dummy->New-thing"
               :on-click #(log/info "Add 'Dummy' & 'New-thing' nodes with link between")]

              [rc/button :label "Change link value"
               :on-click #(log/info "Change link value")]]])



(comment
  (def data [:area-chart-2-data-sub-demo :blackboard :topic.sample-data])
  (def old-data (ui-utils/subscribe-local data [:data]))


  (h/handle-change-path data [:data]
    (assoc-in @(ui-utils/subscribe-local data [:data]) [0 :uv] 10000))
  (h/handle-change-path data [:data]
    (assoc-in @(ui-utils/subscribe-local data [:data]) [0 :pv] 7000))


  ())
