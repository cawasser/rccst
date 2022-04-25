(ns bh.rccst.ui-component.atom.astrouxds.radio-button
  (:require ["@astrouxds/react" :refer (RuxRadioGroup RuxRadio)]))


(defn radio-button [& {:keys [name value]}]
  [:> RuxRadioGroup
   [:> RuxRadio
    {:name name :value value}]])
