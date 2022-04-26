(ns bh.rccst.ui-component.atom.astrouxds.table
  (:require ["@astrouxds/react" :refer (RuxTable RuxTableHeader RuxTableHeaderRow RuxTableHeaderCell RuxTableBody RuxTableRow RuxTableCell)]))

(defn table []
   [:> RuxTable
    [:> RuxTableHeader
     [:> RuxTableHeaderRow
       (doall (for [v ["name" "kp" "uv" "pv" "amt"]]
                [:> RuxTableHeaderCell v]))]]
    [:> RuxTableBody
     [:> RuxTableRow {:selected false}
      (doall (for [v ["Page A" "2000" "4000" "2400" "2400"]]
               [:> RuxTableCell v]))]
     [:> RuxTableRow {:selected false}
      (doall (for [v ["Page B" "2000" "3000" "5598" "2210"]]
               [:> RuxTableCell v]))]
     [:> RuxTableRow {:selected false}
      (doall (for [v ["Page C" "2000" "2000" "9800" "2290"]]
               [:> RuxTableCell v]))]
     [:> RuxTableRow {:selected false}
      (doall (for [v ["Page D" "2000" "2780" "3908" "2000"]]
               [:> RuxTableCell v]))]
     [:> RuxTableRow {:selected false}
      (doall (for [v ["Page E" "2000" "1890" "4800" "2181"]]
               [:> RuxTableCell v]))]]])

