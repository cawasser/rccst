(ns bh.rccst.views.atom.example.misc.two-d-globe
  (:require [woolybear.ad.catalog.utils :as acu]
            [woolybear.ad.layout :as layout]))


(defn example []
  (acu/demo "2D Globe"
    "A 2D globe built using an HTML [table](https://www.w3schools.com/html/html_tables.asp) over the top of an image background."
    [layout/centered {:extra-classes :width-50}
     [:div "A 2D Globe"]]
    '[]))
