(ns bh.rccst.views.molecule.example.two-d-three-d-globe
  (:require [taoensso.timbre :as log]

            [bh.rccst.ui-component.molecule.two-d-three-d-globe :as globe]
            [bh.rccst.ui-component.molecule.example :as example]))


(defn example []
  (let [widget-id "two-d-three-d-globe-demo"]
    [example/component-example
     :title "2D/3D Globe"
     :widget-id widget-id
     :description "A combination of the 2d and 3d globe components. Both show the same data."
     :data globe/sample-data
     :component globe/component
     :component-id (str widget-id "/globe")
     :source-code globe/source-code]))
