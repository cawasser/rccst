(ns rccst.views.atom.example.layout.page-title
  (:require [woolybear.ad.catalog.utils :as acu]
            [woolybear.ad.layout :as layout]))


(defn example []
  (acu/demo "Page Title"
    "Use the page-title component instead of an h1 tag to contain the main
    title at the top of each page."
    [layout/page
     [layout/page-header
      [layout/page-title "Demonstration Page"]]]
    '[layout/page
      [layout/page-header
       [layout/page-title "Demonstration Page"]]]))
