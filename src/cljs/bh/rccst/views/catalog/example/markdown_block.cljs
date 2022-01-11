(ns bh.rccst.views.catalog.example.markdown-block
  (:require [woolybear.ad.catalog.utils :as acu]
            [woolybear.ad.layout :as layout]))

(defn example []
  (acu/demo "Markdown-Block"
    "Use Markdown blocks for [Markdown](https://en.wikipedia.org/wiki/Markdown) content."
    [layout/frame]

    '[layout/frame]))