(ns bh.rccst.views.catalog.example.layout.text-block
  (:require [woolybear.ad.catalog.utils :as acu]
            [woolybear.ad.layout :as layout]))


(defn example []
  (acu/demo "Text Block"
    "Use text blocks for ordinary blocks of text and generic text content."
    [layout/text-block acu/lorem]
    '[layout/text-block acu/lorem]))
