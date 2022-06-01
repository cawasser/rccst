(ns bh.rccst.ui-component.atom.bh.data-table
  (:require [bh.rccst.ui-component.utils.example-data :as ex]
            [bh.rccst.ui-component.utils.helpers :as h]
            [re-com.core :as rc]
            [re-frame.core :as re-frame]
            [reagent.core :as r]
            [taoensso.timbre :as log]

            ["fixed-data-table-2" :refer [Table Column DataCell]]))

(log/info "bh.rccst.ui-component.atom.bh.data-table")

(log/info "bh.rccst.ui-component.atom.bh.data-table" Table)

(def sample-data ex/tabular-data)

(def data ["string 1" "string 2" "string 3"])


(defn table [& {:keys [data config-data]}]

  (let [d (h/resolve-value data)
        c (h/resolve-value config-data)]
    [:> Table {:rowHeight 50 :rowsCount 0 :width 500 :height 500 :headerHeight 50}
     [:> Column {:header (fn [] (r/as-element [:> DataCell "Col 1"]))
                 :cell (fn [] (r/as-element [:> DataCell "Column 1 static content"]))
                 :width 200}]
     [:> Column {:header (fn [] (r/as-element [:> DataCell "Col 2"]))
                 :cell (fn [idx _]
                        (let [{:keys [rowIndex]} (js->clj idx :keywordize-keys true)]
                         (r/as-element [:> DataCell (nth data rowIndex)])))
                 :width 200}
       [:div "data table"
        [:p (str @d)]
        [:p (str @c)]]]]))

