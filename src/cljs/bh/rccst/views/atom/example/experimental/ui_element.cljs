(ns bh.rccst.views.atom.example.experimental.ui-element
  (:require [bh.rccst.ui-component.atom.experimental.ui-element :as elements]
            [bh.rccst.ui-component.utils :as utils]
            [taoensso.timbre :as log]
            [woolybear.ad.catalog.utils :as acu]
            [woolybear.ad.layout :as layout]))


(log/info "bh.rccst.views.atom.example.experimental.ui-element")


(defn selectable-table []
  (let [container-id "experimental-selectable-table-demo"]
    (acu/demo
      "Experimental Selectable Table"
      "Demonstration of how we could build UI components that can be composed at run=time using
      a DSL"
      [layout/centered {:extra-classes :width-50}
       [elements/selectable-table
        :data [:topic/target-data]
        :selection [(utils/path->keyword container-id :blackboard :topic/selection)]
        :container-id container-id
        :component-id (utils/path->keyword container-id "selectable-table")]])))


(defn three-d-globe []
  (let [container-id "experimental-three-d-globe-demo"]
    (acu/demo
      "Experimental 3D Globe"
      "Demonstration of how we could build UI components that can be composed at run=time using
      a DSL"
      [layout/centered {:extra-classes :width-50}
       [elements/three-d-globe
        :layers [(utils/path->keyword container-id :blackboard :topic/layers)]
        :current-time [(utils/path->keyword container-id :blackboard :topic/current-time)]
        :container-id container-id
        :component-id (utils/path->keyword container-id "three-d-globe")]])))


(defn slider []
  (let [container-id "experimental-slider-demo"]
    (acu/demo
      "Experimental Slider"
      "Demonstration of how we could build UI components that can be composed at run=time using
      a DSL"
      [layout/centered {:extra-classes :width-50}
       [elements/slider
        :value [:topic/coverage-data]
        :range [(utils/path->keyword container-id :blackboard :topic/range)]
        :container-id container-id
        :component-id (utils/path->keyword container-id "slider")]])))


(defn label []
  (let [container-id "experimental-label-demo"]
    (acu/demo
      "Experimental Label"
      "Demonstration of how we could build UI components that can be composed at run=time using
      a DSL"
      [layout/centered {:extra-classes :width-50}
       [elements/slider
        :value [(utils/path->keyword container-id :blackboard :topic/current-time)]
        :container-id container-id
        :component-id (utils/path->keyword container-id "label")]])))

