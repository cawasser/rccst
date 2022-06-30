(ns bh.rccst.views.atom.example.chart.alt.data-tools
  (:require [bh.rccst.ui-component.utils :as ui-utils]
            [bh.rccst.ui-component.utils.helpers :as h]
            [re-com.core :as rc]
            [taoensso.timbre :as log]
            [bh.rccst.ui-component.utils.example-data :as ex]))


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
              ; TODO: need to pass a "meaningful" random-data-set builder function into the tools
              [rc/button :label "Random" :on-click #(reset! data (ex/random-meta-tabular-data))]
              [rc/button :label "A(uv) -> 10,000" :on-click #(swap! data assoc-in [:data 0 :uv] 10000)]
              [rc/button :label "Add 'Q'"
               :on-click #(swap! data assoc :data
                            (conj (-> @data :data)
                              {:name "Page Q" :uv (rand-int 5000)
                               :pv   (rand-int 5000) :tv (rand-int 5000) :amt (rand-int 5000)}))]

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

                  [rc/button :label "Random" :on-click #(h/handle-change-path data [] (ex/random-meta-tabular-data))]

                  [rc/button :label "A(uv) -> 10000"
                   :on-click #(h/handle-change-path data [:data]
                                (assoc-in @old-data [0 :uv] 10000))]

                  [rc/button :label "Add 'Q'"
                   :on-click #(h/handle-change-path data [:data]
                                (conj @old-data
                                  {:name "Page Q" :uv (rand-int 5000)
                                   :pv   (rand-int 5000) :tv (rand-int 5000) :amt (rand-int 5000)}))]

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


(defn- add-node [data new-node]
  (let [original   (h/resolve-value data)
        next-index (->> @original
                     :nodes
                     (map :index)
                     (apply max)
                     inc)]
    (-> data
      h/resolve-value
      deref
      (assoc :nodes (conj (:nodes @original) {:name new-node :index next-index})))))


(defn- add-nodes-and-link [data source target value]
  (let [original   (h/resolve-value data)
        next-index (->> @original
                     :nodes
                     (map :index)
                     (apply max)
                     inc)]
    (-> data
      h/resolve-value
      deref
      (assoc :nodes (conj (:nodes @original)
                      {:name source :index next-index}
                      {:name target :index (inc next-index)})
             :links (conj (:links @original)
                      {:source source :target target :value value})))))


(defn- add-link [data source target value]
  (-> data
    h/resolve-value
    deref
    (update :links conj {:source source :target target :value value})))


(defn- update-link [data source target new-value]
  (let [original-data (h/resolve-value data)
        original-link (->> @original-data
                        :links
                        (filter #(and (= source (:source %)) (= target (:target %))))
                        first)]
    (-> data
      h/resolve-value
      deref
      (update :links disj original-link)
      (update :links conj {:source source :target target :value new-value}))))


(defn dag-data-ratom-tools [data default-data]
  [rc/h-box :src (rc/at)
   :gap "10px"
   :style {:border     "1px solid" :border-radius "3px"
           :box-shadow "5px 5px 5px 2px"
           :margin     "5px" :padding "5px"}
   :children [[:label.h5 "Input Data:"]

              [rc/button :label "Empty" :on-click #(reset! data [])]

              [rc/button :label "Default" :on-click #(reset! data default-data)]

              [rc/button :label "+ Redirect (1)"
               :on-click #(reset! data (add-node data :Redirect))]

              [rc/button :label "+ Visit->Redirect (2)"
               :on-click #(reset! data (add-link data :Visit :Redirect 24987))]

              [rc/button :label "Redirect = 50000 (3)"
               :on-click #(reset! data (update-link data :Visit :Redirect 50000))]

              [rc/button :label "+ Dummy->New-thing"
               :on-click #(reset! data (add-nodes-and-link data :Dummy :New-thing 124987))]]])


(defn dag-data-sub-tools [data default-data]
  [rc/h-box :src (rc/at)
   :gap "10px"
   :style {:border     "1px solid" :border-radius "3px"
           :box-shadow "5px 5px 5px 2px"
           :margin     "5px" :padding "5px"}
   :children [[:label.h5 "Input Data:"]

              [rc/button :label "Empty" :on-click #(h/handle-change-path data [] [])]

              [rc/button :label "Default" :on-click #(h/handle-change-path data [] default-data)]

              [rc/button :label "+ Redirect (1)"
               :on-click #(h/handle-change-path data [] (add-node data :Redirect))]

              [rc/button :label "+ Visit->Redirect (2)"
               :on-click #(h/handle-change-path data [] (add-link data :Visit :Redirect 24987))]

              [rc/button :label "Redirect = 50000 (3)"
               :on-click #(h/handle-change-path data [] (update-link data :Visit :Redirect 50000))]

              [rc/button :label "+ Dummy->New-thing"
               :on-click #(h/handle-change-path data [] (add-nodes-and-link data :Dummy :New-thing 124987))]]])





(comment
  (def data [:area-chart-2-data-sub-demo :blackboard :topic.sample-data])
  (def old-data (ui-utils/subscribe-local data [:data]))


  (h/handle-change-path data [:data]
    (assoc-in @(ui-utils/subscribe-local data [:data]) [0 :uv] 10000))
  (h/handle-change-path data [:data]
    (assoc-in @(ui-utils/subscribe-local data [:data]) [0 :pv] 7000))


  ())


(comment
  (do
    (def source :Visit)
    (def target :Page-Click)
    (def data {:nodes #{{:name :Visit :index 0}
                        {:name :Direct-Favourite :index 1}
                        {:name :Page-Click :index 2}
                        {:name :Detail-Favourite :index 3}
                        {:name :Lost :index 4}}
               :links #{{:source :Visit :target :Direct-Favourite :value 37283}
                        {:source :Visit :target :Page-Click :value 354170}
                        {:source :Page-Click :target :Detail-Favourite :value 62429}
                        {:source :Page-Click :target :Lost :value 291741}}})

    (def original-data (h/resolve-value data)))

  (def next-index (->> @original-data
                    :nodes
                    (map :index)
                    (apply max)
                    inc))


  (def original-link (->> @original-data
                       :links
                       (filter #(and (= source (:source %)) (= target (:target %))))
                       first))

  (disj (:link data) original-link)

  ())
