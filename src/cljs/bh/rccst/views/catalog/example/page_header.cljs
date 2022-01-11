(ns bh.rccst.views.catalog.example.page-header
  (:require [woolybear.ad.catalog.utils :as acu]
            [woolybear.ad.layout :as layout]))


(defn example []
  (acu/demo "Page Header"
    "The page header component is intended for use as the top section of a
    page, containing the page title, navigation, bread crumbs, etc. Use
    inside a [layout/page] component."
    [layout/page
     [layout/page-header "Put header stuff here."]]
    '[layout/page
      [layout/page-header "Put header stuff here."]]))