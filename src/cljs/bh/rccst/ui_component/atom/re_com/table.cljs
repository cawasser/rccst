(ns bh.rccst.ui-component.atom.re-com.table
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


(defn- table* [& {:keys [data max-rows width height cell-style-fn
                         on-click-row-fn row-line-color]}]

  (log/info "table*" data)

  [:div {:style {:width (or width "300px") :height (or height "250px")}}
   (if (empty? data)

     [rc/alert-box :src (rc/at)
      :alert-type :info
      :heading "Waiting for data"]

     [rc/simple-v-table :src (rc/at)
      :model (r/atom data)
      :columns (table-column-headers data 5 (or width 200) (or height))
      :max-rows (or max-rows (count data))
      :table-row-line-color (or row-line-color "#00fff0")
      :on-click-row (or on-click-row-fn #())
      :cell-style (or cell-style-fn #())])])


(defn table [& {:keys [data max-rows width height cell-style-fn
                       on-click-row-fn row-line-color]}]

  (let [remote (h/resolve-value data)]
    (fn []
      (log/info "table" data "//" @remote)
      [table*
       :data @remote
       :max-rows max-rows
       :row-line-color row-line-color
       :on-click-row on-click-row-fn
       :cell-style-fn cell-style-fn])))


(defn meta-table [& {:keys [data max-rows width height cell-style-fn
                            on-click-row-fn row-line-color]}]

  (let [d (h/resolve-value data)]
    (fn []
      (log/info "meta-table" data "//" @d "//" (:data @d) "//" (count (:data @d)))
      [table*
       :data (:data @d)
       :max-rows (or max-rows (count (:data @d)))
       :row-line-color (or row-line-color "#00fff0")
       :on-click-row (or on-click-row-fn #())
       :cell-style-fn (or cell-style-fn #())])))


(def meta-data {:rc/table      {:component table
                                :ports     {:data :port/sink}}
                :rc/meta-table {:component meta-table
                                :ports     {:data :port/sink}}})


(comment
  (def data [:bh.rccst.subs/source :source/targets])


  (def some-code {:dummy {:one :port/sink :alpha :port/sink}
                  :dummy2 {:two :port/source}})

  (str some-code)

  (clojure.string/join "\n" (clojure.string/split (str some-code) #","))

  ())
