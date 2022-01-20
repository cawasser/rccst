(ns bh.rccst.ui-component.table
  (:require [taoensso.timbre :as log]
            [re-com.core :as rc]))


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



(defn table [& {:keys [data max-rows width height cell-style-fn
                       on-click-row-fn row-line-color]}]
  [rc/simple-v-table :src (rc/at)
   :model data
   :columns (table-column-headers @data 5 (or width 100) (or height))
   :max-rows (or max-rows (count @data))
   :table-row-line-color (or row-line-color "#00fff0")
   :on-click-row (or on-click-row-fn #())
   :cell-style (or cell-style-fn #())])