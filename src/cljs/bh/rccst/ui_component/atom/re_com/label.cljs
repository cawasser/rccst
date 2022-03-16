(ns bh.rccst.ui-component.atom.re-com.label
  (:require [bh.rccst.ui-component.utils.helpers :as h]
            [re-com.core :as rc]
            [taoensso.timbre :as log]))


(log/info "bh.rccst.ui-component.atom.re-com.label")


(defn label [& {:keys [value]}]
  ;(log/info "label" value "//" (str v))
  [rc/label :src (rc/at)
   :label @(h/resolve-value value)])


(defn label-sm [& {:keys [value]}]
  ;(log/info "label-sm" value "//" (str v))
  [rc/label :src (rc/at)
   :style {:font-size ".5em"}
   :label @(h/resolve-value value)])


(defn label-md [& {:keys [value]}]
  [rc/label :src (rc/at)
   :style {:font-size "1.5em"}
   :label @(h/resolve-value value)])


(defn label-lg [& {:keys [value]}]
  ;(log/info "label-lg" value "//" (str v) "//" (str @v) "//")
  [rc/label :src (rc/at)
   :style {:font-size "2em"}
   :label @(h/resolve-value value)])


(defn label-hg [& {:keys [value]}]
  ;(log/info "label-hg" value "//" (str v))
  [rc/label :src (rc/at)
   :style {:font-size "2.5em"}
   :label @(h/resolve-value value)])


(def meta-data {:rc/label    {:component label :ports {:value :port/sink}}
                :rc/label-sm {:component label-sm :ports {:value :port/sink}}
                :rc/label-md {:component label-md :ports {:value :port/sink}}
                :rc/label-lg {:component label-lg :ports {:value :port/sink}}
                :rc/label-hg {:component label-hg :ports {:value :port/sink}}})

