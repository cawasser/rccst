(ns bh.rccst.views.atom.example.re-com.splitter
  (:require [re-com.core :as rc]
            [reagent.core :as r]
            [taoensso.timbre :as log]
            [woolybear.ad.catalog.utils :as acu]
            [woolybear.ad.layout :as layout]))


(def rounded-panel (merge (rc/flex-child-style "1")
                     {:background-color "#fff4f4"
                      :border           "1px solid lightgray"
                      :border-radius    "4px"
                      :padding          "0px 20px 0px 20px"}))


(defn- splitter-panel-title
  [text]
  [rc/title :src (rc/at)
   :label text
   :level :level3
   :style {:margin-top "20px"}])


(defn- panel-one
  []
  [rc/box :src (rc/at)
   :size "auto"
   :child [:div {:style rounded-panel}
           [splitter-panel-title [:code ":panel-1"]]]])


(defn panel-two
  []
  [rc/box :src (rc/at)
   :size "auto"
   :child [:div {:style rounded-panel}
           [splitter-panel-title [:code ":panel-2"]]]])


(defn horizontal-example []
  (acu/demo "Horizontal Splitter"
    "Arranges two components horizontally with a splitter control between them. The
    user can then change the width of the two components by dragging the splitter bar.
    The component share the same overall width (minus the width of the splitter).

> See also:
>
> [Re-com splitter demo](https://re-com.day8.com.au/#/splits)
"
    [rc/v-box :src (rc/at)
     :size "auto"
     :gap "10px"
     :height "500px"
     :children [[rc/h-split :src (rc/at)
                 :panel-1 [panel-one]
                 :panel-2 [panel-two]
                 :size "300px"
                 :initial-split "25%"
                 :parts {:top    {:style {:overflow :hidden}}
                         :bottom {:style {:overflow :hidden}}}]]]


    '[]))


(defn vertical-example []
  (acu/demo "Vertical Splitter"
    "Arranges two components vertically with a splitter control between them. The
    user can then change the height of the two components by dragging the splitter bar.
    The component share the same overall height (minus the width of the splitter).

> See also:
>
> [re-com/splitter demo](https://re-com.day8.com.au/#/splits)
"
    [rc/v-box :src (rc/at)
     :size "auto"
     :gap "10px"
     :height "500px"
     :children [[rc/v-split :src (rc/at)
                 :panel-1 [panel-one]
                 :panel-2 [panel-two]
                 :size "300px"
                 :initial-split "25%"
                 :parts {:top    {:style {:overflow :hidden}}
                         :bottom {:style {:overflow :hidden}}}]]]


    '[]))


(defn example []
  [rc/v-box :src (rc/at)
   :size "auto"
   :gap "10px"
   :height "500px"
   :children [[rc/h-split :src (rc/at)
               :panel-1 [panel-one]
               :panel-2 [panel-two]
               :size "300px"
               :initial-split "25%"
               :parts {:top    {:style {:overflow :hidden}}
                       :bottom {:style {:overflow :hidden}}}]]])
