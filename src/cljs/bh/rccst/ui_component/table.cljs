(ns bh.rccst.ui-component.table
  (:require [re-com.core :as rc]
            [re-frame.core :as re-frame]
            [reagent.core :as r]))


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


(defn table [& {:keys [data max-rows width height cell-style-fn
                       on-click-row-fn row-line-color]}]

  ;; NOTE: this is kind of a hack (see https://stuartsierra.com/2015/06/10/clojure-donts-heisenparameter)
  (let [remote (if (vector? data) (re-frame/subscribe data) data)]
    [rc/simple-v-table :src (rc/at)
     :model remote
     :columns (table-column-headers @remote 5 (or width 200) (or height))
     :max-rows (or max-rows (count @remote))
     :table-row-line-color (or row-line-color "#00fff0")
     :on-click-row (or on-click-row-fn #())
     :cell-style (or cell-style-fn #())]))


(defn meta-table [& {:keys [data width height cell-style-fn
                            on-click-row-fn row-line-color]}]
  (let [d (r/atom (get @data :data))]
    [table
     :data d
     :width width
     :height height
     :cell-style-fn cell-style-fn
     :on-click-row-fn on-click-row-fn
     :row-line-color row-line-color]))


(def table-meta-data {:real-table/table {:component table
                                         :ports     {:data :port/sink}}})
