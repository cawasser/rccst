(ns bh.rccst.views.atom.example.layout.page
  (:require [woolybear.ad.catalog.utils :as acu]
            [woolybear.ad.layout :as layout]))


(defn example []
  (acu/demo "Page"
    "The page component is a simple chart designed to contain an entire
    \"page\" (not including the site header and footer). Use this container
    to wrap each page, then edit the AD page component whenever you want to
    make changes that apply to the page as a whole."
    [layout/page
     [:div "Something much more complex would go here."]]
    '[layout/page
      [:div "Something much more complex would go here."]]))
