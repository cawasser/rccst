(ns bh.rccst.ui-component.atom.astrouxds.classification-marking
  (:require ["@astrouxds/react" :refer [RuxClassificationMarking]]))

(defn classification-marking [& {:keys [level footer?]}]
  (let [level-prop (if (exists? :level) {:classification level})
        footer-prop (if (and (exists? :footer?) footer?) {:footer true})]
    [:> RuxClassificationMarking (merge level-prop footer-prop)]))
