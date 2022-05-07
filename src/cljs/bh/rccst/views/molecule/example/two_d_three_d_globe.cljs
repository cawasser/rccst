(ns bh.rccst.views.molecule.example.two-d-three-d-globe
  (:require [bh.rccst.ui-component.molecule.example :as example]
            [bh.rccst.ui-component.molecule.two-d-three-d-globe :as globe]
            [bh.rccst.ui-component.utils :as utils]))


(defn example []
  (let [container-id "two-d-three-d-globe-demo"]
    [example/component-example
     :title "2D/3D Globe"
     :container-id container-id
     :description "A combination of the 2d and 3d globe components. Both show the same data."
     :data globe/sample-data
     :component globe/component
     :component-id (utils/path->keyword container-id "globe")
     :source-code globe/source-code]))
