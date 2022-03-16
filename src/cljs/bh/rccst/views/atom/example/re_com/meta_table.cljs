(ns bh.rccst.views.atom.example.re-com.meta-table
  (:require [bh.rccst.ui-component.atom.re-com.table :as table]
            [reagent.core :as r]
            [re-com.core :as rc]
            [taoensso.timbre :as log]
            [woolybear.ad.catalog.utils :as acu]
            [woolybear.ad.layout :as layout]))


(def data (r/atom
            {:title "Sample Data with meta-data"
             :metadata {:type :tabular
                        :id :name
                        :fields {:name :string :uv :number :pv :number :tv :number :amt :number}}
             :data [{:name "Page A" :kp 2000 :uv 4000 :pv 2400 :amt 2400}
                    {:name "Page B" :kp 2000 :uv 3000 :pv 5598 :amt 2210}
                    {:name "Page C" :kp 2000 :uv 2000 :pv 9800 :amt 2290}
                    {:name "Page D" :kp 2000 :uv 2780 :pv 3908 :amt 2000}
                    {:name "Page E" :kp 2000 :uv 1890 :pv 4800 :amt 2181}
                    {:name "Page F" :kp 2000 :uv 2390 :pv 3800 :amt 2500}
                    {:name "Page G" :kp 2000 :uv 3490 :pv 4300 :amt 2100}]}))


(defn cell-styling-fn [{:keys [amt uv pv] :as row} {:keys [id] :as column}]
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


(defn example []
  (acu/demo "Table (with meta-data)"
    "We're using the `simple-v-table` from [Re-com](https://github.com/Day8/re-com)

> This version ***DOES*** include meta-data."

    [layout/centered {:extra-classes :width-50}
     [rc/v-box :src (rc/at)
      :gap "5px"
      :children [[:h3 (:title @data)]
                 [table/meta-table
                  :data data
                  :max-rows 5
                  :width 600
                  :on-click-row #(log/info "on-click-row")
                  :cell-style-fn cell-styling-fn]]]]

    '[layout/centered {:extra-classes :width-50}
      [table/meta-table
       :data data
       :max-rows 5
       :width 600
       :table-row-line-color "#0fff00"
       :on-click-row #(log/info "on-click-row")
       :cell-style-fn cell-styling-fn]]))