(ns bh.rccst.ui-component.atom.re-com.slider
  (:require [bh.rccst.ui-component.utils.helpers :as h]
            [re-com.core :as rc]
            [taoensso.timbre :as log]))


(log/info "bh.rccst.ui-component.atom.re-com.slider")


(defn slider [& {:keys [value range width disabled?]}]

  (let [[min max] @(h/resolve-value range)]

    ;(log/info "slider" value "//" (str @v) "//" range "// [" min " " max "]")

    [rc/slider :src (rc/at)
     :model (h/resolve-value value)
     :min min
     :max max
     ;:width (or width "100%")
     :on-change #(h/handle-change value (js/parseInt %))
     :disabled? (or disabled? false)]))



(def meta-data {:rc/slider {:component slider
                            :ports {:value :port/source-sink
                                    :range :port/sink}}})


