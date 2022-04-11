(ns bh.rccst.ui-component.atom.astrouxds.button
  (:require ["@astrouxds/react" :refer [RuxButton]]))

(defn button [& {:keys [label size]}]
  [:> RuxButton "button"])
