(ns rccst.views.atom.example.re-com.plain-table
  (:require [reagent.core :as r]
            [re-com.core :as rc]
            [taoensso.timbre :as log]
            [woolybear.ad.catalog.utils :as acu]
            [woolybear.ad.layout :as layout]))


(def sample-data {:metadata {:type   :tabular
                             :id     :name
                             :title  "Tabular Data with Metadata"
                             :fields {:name :string :uv :number :pv :number :kp :number :amt :number}}
                  :data [{:name "Page A" :kp 2000 :uv 4000 :pv 2400 :amt 2400}
                         {:name "Page B" :kp 2000 :uv 3000 :pv 5598 :amt 2210}
                         {:name "Page C" :kp 2000 :uv 2000 :pv 9800 :amt 2290}
                         {:name "Page D" :kp 2000 :uv 2780 :pv 3908 :amt 2000}
                         {:name "Page E" :kp 2000 :uv 1890 :pv 4800 :amt 2181}
                         {:name "Page F" :kp 2000 :uv 2390 :pv 3800 :amt 2500}
                         {:name "Page G" :kp 2000 :uv 3490 :pv 4300 :amt 2100}]})
(defonce data (r/atom sample-data))


(defn cell-styling-fn [{:keys [amt uv pv] :as row} {:keys [id] :as column}]
  ;(log/info "cell-styling-fn")
  (cond
    (= :uv id)
    {:background-color (cond
                         (> 2000 uv) "#FF4136"
                         (> 5000 uv 2000) "#FFDC00"
                         (> 7500 uv 5000) "#01FF70"
                         (> uv 7500) "#2ECC40")}
    (= :pv id)
    {:background-color (cond
                         (> 2000 pv) "#FF4136"
                         (> 5000 pv 2000) "#FFDC00"
                         (> 7500 pv 5000) "#01FF70"
                         (> pv 7500) "#2ECC40")}
    (= :amt id)
    {:background-color (cond
                         (> 2000 amt) "#FF4136"
                         (> 5000 amt 2000) "#FFDC00"
                         (> 7500 amt 5000) "#01FF70"
                         (> amt 7500) "#2ECC40")}))


(defn table-column-headers [data rows width height]
  (let [d          (apply set (map keys data))
        col-count  (count d)
        col-width  (max 80 (- (/ (or width 400) col-count) 5))
        row-height (max 50 (/ (or height 400) (+ 2 (or rows 5))))]
    (->> d
      (map (fn [k]
             {:id    k :header-label (name k) :row-label-fn k
              :width col-width :height row-height}))
      (into []))))


(defn- show [data]
  [rc/h-box :src (rc/at)
   :gap "10px"
   :style {:border     "1px solid" :border-radius "3px"
           :background "#808080"
           :box-shadow "5px 5px 5px 2px"
           :margin     "5px" :padding "5px"}
   :children [[:p {:style {:color "white"}}
               (str @data)]]])


(defn- data-tools []
  [rc/h-box :src (rc/at)
   :gap "10px"
   :style {:border     "1px solid" :border-radius "3px"
           :box-shadow "5px 5px 5px 2px"
           :margin     "5px" :padding "5px"}
   :children [[:label.h5 "Input Data:"]
              [rc/button :on-click #(reset! data (assoc sample-data :data [])) :label "Empty"]
              [rc/button :on-click #(reset! data sample-data) :label "Default"]
              [rc/button :on-click #(swap! data assoc-in [:data 0 :uv] 10000) :label "A -> 10,000"]
              [rc/button :on-click #(swap! data assoc :data
                                      (conj (:data @data)
                                        {:name "Page Q" :uv 1100
                                         :pv   1100 :tv 1100 :amt 1100}))
               :label "Add 'Q'"]
              [rc/button :on-click #(swap! data assoc :data
                                      (into [] (drop-last 2 (:data @data))))
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

  (fn []
    [rc/v-box :src (rc/at)
     :class "data-update-example"
     :gap "10px"
     :width "100%"
     :height "100%"
     :children [[:div.chart-part {:style {:width "100%" :height "90%"}}
                 [rc/simple-v-table :src (rc/at)
                  :model (r/atom (:data @data))
                  :columns (table-column-headers (:data @data) 5 200 25)
                  :max-rows (count (:data @data))
                  :table-row-line-color "#00fff0"
                  :cell-style cell-styling-fn
                  :parts {:simple-wrapper {:style {:border false}}}]]
                [:div.data-tools-part {:style {:width "100%"}}
                 [show data]
                 [data-tools]]]]))



(defn example []
  (acu/demo "Raw Re-com Table (WIP)"
    "Trying to debug re-renders on data change

> Looks like the component _may_ be broken..."

    [layout/centered {:extra-classes :width-50}
     [data-update-example :data data]]))




(comment
  (reset! data [])

  (reset! data sample-data)

  (swap! data assoc-in [0 :uv] 10000)

  (swap! data conj {:name "Page Q" :uv 1100
                    :pv   1100 :tv 1100 :amt 1100})

  (reset! data (into [] (drop-last 2 @data)))

  (reset! data (into []
                 (map (fn [x]
                        (assoc x :new-item (rand-int 5000)))
                   @data)))

  ())


