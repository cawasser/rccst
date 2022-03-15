(ns bh.rccst.ui-component.atom.re-com.label
  (:require [re-com.core :as rc]
            [re-frame.core :as re-frame]))


(defn label [& {:keys [value]}]
  (let [v (if (coll? value) (re-frame/subscribe value) value)]
    [rc/label :src (rc/at)
     :label @v]))


(defn label-md [& {:keys [value]}]
  (let [v (if (coll? value) (re-frame/subscribe value) value)]
    [rc/label :src (rc/at)
     :style {:font-size "1.5em"}
     :label @v]))


(defn label-sm [& {:keys [value]}]
  (let [v (if (coll? value) (re-frame/subscribe value) value)]
    [rc/label :src (rc/at)
     :style {:font-size ".5em"}
     :label @v]))


(defn label-lg [& {:keys [value]}]
  (let [v (if (coll? value) (re-frame/subscribe value) value)]
    [rc/label :src (rc/at)
     :style {:font-size "2em"}
     :label @v]))


(defn label-hg [& {:keys [value]}]
  (let [v (if (coll? value) (re-frame/subscribe value) value)]
    [rc/label :src (rc/at)
     :style {:font-size "2.5em"}
     :label @v]))


(def meta-data {:rc-label/label    {:component label :ports {:value :port/sink}}
                :rc-label/label-sm {:component label-sm :ports {:value :port/sink}}
                :rc-label/label-md {:component label-md :ports {:value :port/sink}}
                :rc-label/label-lg {:component label-lg :ports {:value :port/sink}}
                :rc-label/label-hg {:component label-hg :ports {:value :port/sink}}})

