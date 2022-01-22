(ns bh.rccst.views.catalog.example.two-d-globe
  (:require [woolybear.ad.layout :as layout]
            [woolybear.ad.catalog.utils :as acu]))


(defn example []
  (acu/demo "2D Globe"
    "A 2D globe built using an HTML [table](https://www.w3schools.com/html/html_tables.asp) over the top of an image background."
    [layout/centered {:extra-classes :width-50}
     [:div "A 2D Globe"]]
    '[]))