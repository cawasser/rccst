(ns bh.rccst.ui-component.full-table
  (:require [taoensso.timbre :as log]
            [re-com.core :as rc]
            [re-com.util    :refer [px]]
            [reagent.core :as r]))

(def dataset (r/atom nil))
(def editing-cell (r/atom nil))
(def editing-cell-content (r/atom nil))

(defn cell-click [rowidx colidx k v]
  (reset! editing-cell {:row rowidx :col colidx k v})
  (reset! editing-cell-content (str v)))

(defn check-click []
  (do
    (swap! dataset assoc-in [(:row @editing-cell) (key (last @editing-cell))] @editing-cell-content)
    (reset! editing-cell nil)
    (reset! editing-cell-content nil)))



(defn edit-comp []
  [:div#editable {:style {:display     "inline-block"
                          :align-items "center"
                          :padding (px 1)
                          :width (px 195)
                          :height (px 30)}}
   [:div#input {:style {:display     "inline-block"
                        :padding (px 1)}}
     [rc/input-text :src (rc/at)
      :model editing-cell-content
      :width (px 130)
      :height (px 20)
      :on-change #(reset! editing-cell-content %)]]
   [:div#check {:style {:display     "inline-block"
                        :padding (px 2)}}
    [rc/md-icon-button :src (rc/at)
                      :style {:display "inline-block"}
                       :md-icon-name "zmdi-check"
                       :size :smaller
                       :on-click #(check-click)]]
   [:div#cancel {:style {:display     "inline-block"
                         :padding (px 2)}}
     [rc/md-icon-button :src (rc/at)
                        :style {:display "inline-block"}
                        :md-icon-name "zmdi-delete"
                        :size :smaller
                        :on-click #(do (reset! editing-cell-content nil)
                                       (reset! editing-cell nil))]]])


(defn span-with-border
  [{:keys [rowidx colidx colname name background height width font-size]}]
  (let [is-editing? (r/atom (= [(:row @editing-cell) (:col @editing-cell)][rowidx colidx]))]
    (if @is-editing? [edit-comp]
      [:span {:style {:position           "static"
                      :width              (px width)
                      :height             (px height)
                      :border-radius      "2px"
                      :border             "solid white 2px"
                      :vertical-align     "middle"
                      :background-color   background
                      :display            "inline-block"
                      :text-align         "center"
                      :white-space        "nowrap"
                      :overflow           "hidden"
                      :text-overflow      "ellipsis"
                      :color              "white"
                      :font-size          font-size
                      :cursor             "pointer"}
              :on-click #(cell-click rowidx colidx colname name)} (str name)])))

(defn build-header [vals]
    [:div {:class "headers"
           :style {:display     "inline-block"
                   :text-align "center"}}
     (doall
       (map-indexed
         (fn [idx v]
           ^{:key [idx]}[span-with-border {:name (str v)
                                           :rowidx -1
                                           :colidx -1
                                           :font-size 20
                                           :background "#60d898"
                                           :height 40 :width 195}]) vals))])

(defn build-row [row_index row]
      (let [values (vals row)
            ks    (into [] (keys row))]
        [:div {:class (str "row" row_index)
               :style {:display     "inline-block"
                       :text-align "center"}}
         (doall
           (map-indexed
             (fn [idx v]
               ^{:key [row_index idx (get ks idx)]}[span-with-border {:rowidx row_index
                                                                      :colidx idx
                                                                      :colname (get ks idx)
                                                                      :name v
                                                                      :font-size 12
                                                                      :background "#d860a0" :height 30 :width 195}]) values))]))



(comment
  (def data (r/atom
              [{:id "Page A" :kp 2000 :uv 4000 :pv 2400 :amt 2400}
               {:id "Page B" :kp 2000 :uv 3000 :pv 5598 :amt 2210}
               {:id "Page C" :kp 2000 :uv 2000 :pv 9800 :amt 2290}
               {:id "Page D" :kp 2000 :uv 2780 :pv 3908 :amt 2000}
               {:id "Page E" :kp 2000 :uv 1890 :pv 4800 :amt 2181}
               {:id "Page F" :kp 2000 :uv 2390 :pv 3800 :amt 2500}
               {:id "Page G" :kp 2000 :uv 3490 :pv 4300 :amt 2100}]))

  (keys (get @data 2))
  (mapv #(merge {:id (:name %)} %) @data)
  (mapv #(clojure.set/rename-keys % {:name :id}) @data)

  (map (fn [k v] [box-with-border {:name (str v)
                                   :background "#d860a0"
                                   :height 30
                                   :width 50}]) (get @data 0))
  (build-row 1 (get @data 0))


  (def row {:id "Page A", :kp 2000, :uv 4000, :pv 2400, :amt 2400})
  (def values (vals row))
  (-> (into [] (map #(vector box-with-border {:name (str %) :background "#d860a0" :height 30 :width 500}) values))
      (with-meta {:key (rand-int 30)}))
  ^{:key (rand-int 30)}

  @is-editing?
  @dataset
  (assoc-in @dataset [(:row @editing-cell) (key (last @editing-cell))] @editing-cell-content)
  (swap! dataset update-in [(:row @editing-cell) (key (last @editing-cell))] @editing-cell-content)


  ())


(defn table
      [& {:keys [data width height]}]
    (do (reset! dataset @data))
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
                :model                   dataset

                ;; Data Rows (section 5)
                ;:row-renderer            (fn [_row_index, _row] [box-with-border {:name (str (:id _row)) :background light-blue :height row-height :width width-of-main-row-content}])
                :row-renderer            (partial build-row )
                :row-content-width       980
                :row-height              row-height
                :max-row-viewport-height total-row-height       ;; force a vertical scrollbar

                ;; row header/footer (sections 2,8)
                ;:row-header-renderer     (fn [_row-index, _row] [box-with-border {:name ":row-header-renderer " :background green :height unit-31 :width unit-121}])
                ;:row-footer-renderer     (fn [_row-index, _row] [box-with-border {:name ":row-footer-renderer"  :background green :height unit-31 :width unit-121}])

                ;; column header/footer (sections 4,6)
                :column-header-renderer  (fn [] [build-header (into (keys (get @data 0)))])
                :column-header-height    40
                ;:column-footer-renderer  (fn [] [edit-comp])
                ;:column-footer-height    unit-50

                ;; 4 corners (sections 1,3,7,9)
                ;:top-left-renderer       (fn [] [box-with-border {:name ":top-left-renderer"     :background white  :height unit-50 :width unit-121}])
                ;:bottom-left-renderer    (fn [] [box-with-border {:name ":bottom-left-renderer"  :background white  :height unit-50 :width unit-121}])
                ;:top-right-renderer      (fn [] [box-with-border {:name ":top-right-renderer"    :background white  :height unit-50 :width unit-121}])
                ;:bottom-right-renderer   (fn [] [box-with-border {:name ":bottom-right-renderer" :background white  :height unit-50 :width unit-121}])
                ])))

