(ns rccst.views.atom.example.layout.frame
  (:require [woolybear.ad.catalog.utils :as acu]
            [woolybear.ad.layout :as layout]))


(defn example []
  (acu/demo "Frame"
    "Use a frame container to enclose other components inside a bordered frame
    and a drop-shadow."
    [layout/frame
     [layout/text-block "Here is a text block"]
     [layout/text-block acu/lorem]]
    '[layout/frame
      [layout/text-block "Here is a text block"]
      [layout/text-block acu/lorem]]))
