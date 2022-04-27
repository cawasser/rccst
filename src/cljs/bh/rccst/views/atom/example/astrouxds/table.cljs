(ns bh.rccst.views.atom.example.astrouxds.table
  (:require [bh.rccst.ui-component.atom.astrouxds.table :as table]
            [re-com.core :as rc]
            [woolybear.ad.catalog.utils :as acu]))

(defn example []
  (acu/demo
    "Table"
    [rc/h-box :src (rc/at)
     :gap "50px"
     :children [[table/table ["name" "kp" "uv" "pv" "amt"]
                             ["Page A" "2000" "4000" "2400" "2400"]
                             ["Page B" "2000" "3000" "5598" "2210"]
                             ["Page C" "2000" "2000" "9800" "2290"]
                             ["Page D" "2000" "2780" "3908" "2000"]
                             ["Page E" "2000" "1890" "4800" "2181"]]]]))
