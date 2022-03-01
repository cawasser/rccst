(ns bh.rccst.ui-component.full-table
  (:require [taoensso.timbre :as log]
            [re-com.core :as rc]
            [re-com.util    :refer [px]]
            [reagent.core :as r]))


(defn table-column-headers [data rows width height]
  (let [d (apply set (map keys data))
        col-count (count d)
        col-width (max 80 (/ (or width 400) col-count))
        row-height (max 50 (/ (or height 400) (+ 2 (or rows 5))))]
    (->> d
      (map (fn [k]
             {:id    k :header-label (name k) :row-label-fn k
              :width col-width :height row-height}))
      (into []))))

;(defn box-with-border
;      [{:keys [name background height width]}]
;      [rc/v-box :src (rc/at)
;       :height height
;       :width   width
;       :style  {:color "white" :background-color background :padding "3px" :border "solid white 1px"}
;       :align  :center
;       :justify :center
;       :children [[rc/label :src (rc/at) :label name :style {:font-size 11 :font-weight "bold"}]]])

(defn row-edit [data row]
      )

(comment

  (def data (r/atom
              [{:id "Page A" :kp 2000 :uv 4000 :pv 2400 :amt 2400}
               {:id "Page B" :kp 2000 :uv 3000 :pv 5598 :amt 2210}
               {:id "Page C" :kp 2000 :uv 2000 :pv 9800 :amt 2290}
               {:id "Page D" :kp 2000 :uv 2780 :pv 3908 :amt 2000}
               {:id "Page E" :kp 2000 :uv 1890 :pv 4800 :amt 2181}
               {:id "Page F" :kp 2000 :uv 2390 :pv 3800 :amt 2500}
               {:id "Page G" :kp 2000 :uv 3490 :pv 4300 :amt 2100}]))

  (vals (get @data 2))
  (mapv #(merge {:id (:name %)} %) @data)
  (mapv #(clojure.set/rename-keys % {:name :id}) @data)

  (map (fn [k v] [box-with-border {:name (str v)
                    :background "#d860a0"
                    :height 30
                    :width 50}]) (get @data 0))
  (build-row 1 (get @data 0))
  ())



;(defn table [& {:keys [data max-rows width height cell-style-fn
;                       on-click-row-fn row-line-color]}]
;
;  [rc/v-table :src (rc/at)
;   :model (mapv #(merge {:id (:name %)} %) @data)
;   :row-renderer (fn [_row_index, _row] [box-with-border {:name (:name _row) :background "#d860a0" :height 10 :width 20}])
;   :row-content-width 100
;   :row-height 10
;
;
;   :column-header-renderer (table-column-headers @data 5 (or width 100) (or height))])




(defn box-with-border
      [{:keys [name background height width]}]
      ^{:key (rand-int 30)}[rc/v-box :src (rc/at)
                   :height (px  height)
                   :width   (if width (px width) "1 0 auto")
                   :style  {:color "white" :background-color background :padding "3px" :border "solid white 1px"}
                   :align  :center
                   :justify :center
                   :children [[rc/label :src (rc/at) :label name :style {:font-size 11 :font-weight "bold"}]]])

(defn build-row [row_index row]
      (let [values (vals row)]
        [:div {:class (str "row" row_index)}
         (into [] (map #(vector box-with-border {:name (str %) :background "#d860a0" :height 30 :width 500}) values))]))


(comment
  (def row {:id "Page A", :kp 2000, :uv 4000, :pv 2400, :amt 2400})
  (def values (vals row))
  (-> (into [] (map #(vector box-with-border {:name (str %) :background "#d860a0" :height 30 :width 500}) values))
      (with-meta {:key (rand-int 30)}))


  ())


(defn table
      [& {:keys [data width height]}]
      (let [light-blue        "#d860a0"
            blue              "#60A0D8"
            gold              "#d89860"
            green             "#60d898"
            white             "#ffffff"

            fib-ratio         0.618             ;; fibonacci ratios to make the visuals look pretty
            unit-50           50                ;; base for fibonacci calulations
            unit-121          (js/Math.round (/ unit-50 fib-ratio fib-ratio))
            unit-31           (js/Math.round (* unit-50 fib-ratio))

            num-rows          5
            row-height        unit-31
            total-row-height  (* num-rows row-height)

            width-of-main-row-content (js/Math.round (/ total-row-height fib-ratio))
            dummy-rows                (r/atom (mapv #(hash-map :id %1) (range num-rows)))]
           (fn []
               [rc/v-table :src (rc/at)
                :model                   data

                ;; Data Rows (section 5)
                ;:row-renderer            (fn [_row_index, _row] [box-with-border {:name (str (:id _row)) :background light-blue :height row-height :width width-of-main-row-content}])
                :row-renderer            (partial build-row )
                :row-content-width       width-of-main-row-content
                :row-height              row-height
                :max-row-viewport-height total-row-height       ;; force a vertical scrollbar

                ;; row header/footer (sections 2,8)
                ;:row-header-renderer     (fn [_row-index, _row] [box-with-border {:name ":row-header-renderer " :background green :height unit-31 :width unit-121}])
                ;:row-footer-renderer     (fn [_row-index, _row] [box-with-border {:name ":row-footer-renderer"  :background green :height unit-31 :width unit-121}])

                ;; column header/footer (sections 4,6)
                :column-header-renderer  (fn [] [box-with-border {:name ":column-header-renderer" :background gold :height unit-50 :width width-of-main-row-content}])
                :column-header-height    unit-50
                ;:column-footer-renderer  (fn [] [box-with-border {:name ":column-footer-renderer" :background "#d8d460" :height unit-50 :width width-of-main-row-content}])
                ;:column-footer-height    unit-50

                ;; 4 corners (sections 1,3,7,9)
                ;:top-left-renderer       (fn [] [box-with-border {:name ":top-left-renderer"     :background white  :height unit-50 :width unit-121}])
                ;:bottom-left-renderer    (fn [] [box-with-border {:name ":bottom-left-renderer"  :background white  :height unit-50 :width unit-121}])
                ;:top-right-renderer      (fn [] [box-with-border {:name ":top-right-renderer"    :background white  :height unit-50 :width unit-121}])
                ;:bottom-right-renderer   (fn [] [box-with-border {:name ":bottom-right-renderer" :background white  :height unit-50 :width unit-121}])
                ])))

