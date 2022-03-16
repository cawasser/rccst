(ns bh.rccst.ui-component.table
  (:require [bh.rccst.ui-component.utils.helpers :as h]
            [re-com.core :as rc]
            [reagent.core :as r]
            [taoensso.timbre :as log]))


(log/info "bh.rccst.ui-component.table")


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

  (let [remote (h/resolve-value data)]
    (fn []
      (log/info "table" data "//" @remote)

      [rc/simple-v-table :src (rc/at)
       :model remote
       :columns (table-column-headers @remote 5 (or width 200) (or height))
       :max-rows (or max-rows (count @remote))
       :table-row-line-color (or row-line-color "#00fff0")
       :on-click-row (or on-click-row-fn #())
       :cell-style (or cell-style-fn #())])))


(defn meta-table [& {:keys [data max-rows width height cell-style-fn
                            on-click-row-fn row-line-color]}]

  (let [d (r/atom (or (:data @(h/resolve-value data)) []))]
    (fn []
      (log/info "meta-table" data "//" @d "//" (count @d))

      [rc/simple-v-table :src (rc/at)
       :model d
       :columns (table-column-headers @d 5 (or width 200) (or height))
       :max-rows (or max-rows (count @d))
       :table-row-line-color (or row-line-color "#00fff0")
       :on-click-row (or on-click-row-fn #())
       :cell-style (or cell-style-fn #())])))


(def meta-data {:real-table/table      {:component table
                                        :ports     {:data :port/sink}}
                :real-table/meta-table {:component meta-table
                                        :ports     {:data :port/sink}}})


(comment
  (def data [:bh.rccst.subs/source :source/targets])

  ())
