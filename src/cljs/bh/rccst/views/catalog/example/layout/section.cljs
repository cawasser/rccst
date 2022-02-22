(ns bh.rccst.views.catalog.example.layout.section
  (:require [woolybear.ad.catalog.utils :as acu]
            [woolybear.ad.layout :as layout]))


(defn example []
  (acu/demo "Section"
    "Use sections within a page to center the contents (space permitting).
    Multiple sections on a page will be separated by vertical spacing as
    well, to create a clear distinction between sections."
    [:div
     [layout/section "This is Section One. " acu/lorem]
     [layout/section "This is Section Two. " acu/lorem]]
    '[:div
      [layout/section "This is Section One. " acu/lorem]
      [layout/section "This is Section Two. " acu/lorem]]))
