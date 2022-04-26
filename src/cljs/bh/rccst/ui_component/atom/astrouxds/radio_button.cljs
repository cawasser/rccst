(ns bh.rccst.ui-component.atom.astrouxds.radio-button
  (:require ["@astrouxds/react" :refer (RuxRadioGroup RuxRadio)]))


(defn radio-button []
  [:> RuxRadioGroup
   (doall (for [v ["One" "Two" "Three"]]
            [:> RuxRadio {:value v} v]))])
