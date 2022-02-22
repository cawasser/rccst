(ns bh.rccst.ui-component.atom.globe.worldwind.geographic-text
  (:require ["worldwindjs" :as WorldWind]
            [taoensso.timbre :as log]))


(defn geographic-text [position text attributes]
  (let [text  (WorldWind/GeographicText. position text)]
    (set! (.-attributes text) attributes)
    text))
