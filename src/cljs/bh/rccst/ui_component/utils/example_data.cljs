(ns bh.rccst.ui-component.utils.example-data
  (:require [cljs-uuid-utils.core :as uuid]
            [cljs.spec.alpha :as spec]
            [bh.rccst.subs :as subs]
            [re-frame.core :as re-frame]
            [taoensso.timbre :as log]))


(log/info "bh.rccst.ui-component.utils.example-data")

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; SOME EXAMPLE DATA
;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def default-coc [{:coc/step      :generated
                   :coc/by        "bh.rccst.ui-component.atom.bh.table"
                   :coc/version   (or @(re-frame/subscribe [::subs/version]) "no version")
                   :coc/at        (str (js/Date.))
                   :coc/signature (uuid/uuid-string (uuid/make-random-uuid))}])

(spec/def :coc/step (spec/and #{:generated :updated :deleted}))
(spec/def :coc/by string?)
(spec/def :coc/version string?)
(spec/def :coc/at string?)
(spec/def :coc/signature string?)
(spec/def :coc/entry (spec/keys :req [:coc/step :coc/by :coc/version
                                      :coc/at :coc/signature]))
(spec/def :coc/coc (spec/coll-of :coc/entry))

(comment
  (spec/valid? :coc/entry {:coc/step      :generated
                           :coc/by        "bh.rccst.ui-component.atom.bh.table"
                           :coc/version   (or @(re-frame/subscribe [:bh.rccst.subs/version]) "no version")
                           :coc/at        (str (js/Date.))
                           :coc/signature (uuid/uuid-string (uuid/make-random-uuid))})
  (spec/valid? :coc/entry {:coc/step      :generated
                           :coc/by        "bh.rccst.ui-component.atom.bh.table"
                           :coc/version   "no version"
                           :coc/at        (js/Date.)
                           :coc/signature (uuid/make-random-uuid)})
  (spec/explain :coc/entry {:coc/step      :generated
                            :coc/by        "bh.rccst.ui-component.atom.bh.table"
                            :coc/version   "no version"
                            :coc/at        (js/Date.)
                            :coc/signature (uuid/make-random-uuid)})
  (spec/valid? :coc/coc default-coc)

  ())


(def tabular-data [{:name "Page A" :uv 4000 :pv 2400 :amt 2400}
                   {:name "Page B" :uv 3000 :pv 1398 :amt 2210}
                   {:name "Page C" :uv 2000 :pv 9800 :amt 2290}
                   {:name "Page D" :uv 2780 :pv 3908 :amt 2000}
                   {:name "Page E" :uv 1890 :pv 4800 :amt 2181}
                   {:name "Page F" :uv 2390 :pv 3800 :amt 2500}
                   {:name "Page G" :uv 3490 :pv 4300 :amt 2100}])

(spec/def :tabular-data/entry map?)
(spec/def :tabular-data/data (spec/coll-of :tabular-data/entry))


(def tabular-data-org [{:name "Page A" :org "Alpha" :uv 4000 :pv 2400 :amt 2400}
                       {:name "Page B" :org "Bravo" :uv 3000 :pv 1398 :amt 2210}
                       {:name "Page C" :org "Charlie" :uv 2000 :pv 9800 :amt 2290}
                       {:name "Page D" :org "Delta" :uv 2780 :pv 3908 :amt 2000}
                       {:name "Page E" :org "Echo" :uv 1890 :pv 4800 :amt 2181}
                       {:name "Page F" :org "Foxtrot" :uv 2390 :pv 3800 :amt 2500}
                       {:name "Page G" :org "Gamma" :uv 3490 :pv 4300 :amt 2100}])


(comment
  (spec/valid? :tabular-data/data tabular-data)
  (spec/valid? :tabular-data/data tabular-data-org)

  ())


(def meta-tabular-data
  {:metadata {:type   :data/tabular
              :id     :name
              :title  "Tabular Data with Metadata"
              :fields {:name :string :uv :number :pv :number :tv :number :amt :number}}
   :data     [{:name "Page A" :uv 4000 :pv 2400 :tv 1500 :amt 2400}
              {:name "Page B" :uv 3000 :pv 1398 :tv 1500 :amt 2210}
              {:name "Page C" :uv 2000 :pv 9800 :tv 1500 :amt 2290}
              {:name "Page D" :uv 2780 :pv 3908 :tv 1500 :amt 2000}
              {:name "Page E" :uv 1890 :pv 4800 :tv 1500 :amt 2181}
              {:name "Page F" :uv 2390 :pv 3800 :tv 1500 :amt 2500}
              {:name "Page G" :uv 3490 :pv 4300 :tv 1500 :amt 2100}]})

(spec/def :data/type #{:data/tabular :data/entity})
(spec/def :data/id keyword?)
(spec/def :data/title string?)
(spec/def :data/fields (spec/map-of keyword? #{:string :number}))
(spec/def :data/metadata (spec/keys :req-un [:data/type :data/fields] :opt-un [:data/id :data/title]))
(spec/def :data/data :tabular-data/data)
(spec/def :tabular-data/meta-data (spec/keys :req-un [:data/metadata :data/data]))


(comment
  (spec/valid? :tabular-data/meta-data meta-tabular-data)


  ())


(def some-other-tabular [{:id "Page A" :a 4000 :b 2400 :c 2400}
                         {:id "Page B" :a 3000 :b 1398 :c 2210}
                         {:id "Page C" :a 2000 :b 9800 :c 2290}
                         {:id "Page D" :a 2780 :b 3908 :c 2000}
                         {:id "Page E" :a 1890 :b 4800 :c 2181}
                         {:id "Page F" :a 2390 :b 3800 :c 2500}
                         {:id "Page G" :a 3490 :b 4300 :c 2100}])


(def paired-data [{:name "Group A" :value 400}
                  {:name "Group B" :value 300}
                  {:name "Group C" :value 300}
                  {:name "Group D" :value 200}
                  {:name "Group E" :value 278}
                  {:name "Group F" :value 189}])


(def triplet-data [{:x 100 :y 200 :z 200}
                   {:x 110 :y 280 :z 200}
                   {:x 120 :y 100 :z 260}
                   {:x 140 :y 250 :z 280}
                   {:x 150 :y 400 :z 500}
                   {:x 170 :y 300 :z 400}])


(def hierarchy-data [{:name     "axis"
                      :children [{:name "Axis" :size 24593}
                                 {:name "Axes" :size 1302}
                                 {:name "AxisGridLine" :size 652}
                                 {:name "AxisLabel" :size 636}
                                 {:name "CartesianAxes" :size 6703}]}
                     {:name     "controls"
                      :children [{:name "TooltipControl" :size 8435}
                                 {:name "SelectionControl" :size 7862}
                                 {:name "PanZoomControl" :size 5222}
                                 {:name "HoverControl" :size 4896}
                                 {:name "ControlList" :size 4665}
                                 {:name "ClickControl" :size 3824}
                                 {:name "ExpandControl" :size 2832}
                                 {:name "DragControl" :size 2649}
                                 {:name "AnchorControl" :size 2138}
                                 {:name "Control" :size 1353}
                                 {:name "IControl" :size 763}]}
                     {:name     "data"
                      :children [{:name "Data" :size 20544}
                                 {:name "NodeSprite" :size 19382}
                                 {:name "DataList" :size 19788}
                                 {:name "DataSprite" :size 10349}
                                 {:name "EdgeSprite" :size 3301}
                                 {:name     "render"
                                  :children [{:name "EdgeRenderer" :size 5569}
                                             {:name "ShapeRenderer" :size 2247}
                                             {:name "ArrowType" :size 698}
                                             {:name "IRenderer" :size 353}]}
                                 {:name "ScaleBinding" :size 11275}
                                 {:name "TreeBuilder" :size 9930}
                                 {:name "Tree" :size 7147}]}
                     {:name     "events"
                      :children [{:name "DataEvent" :size 7313}
                                 {:name "SelectionEvent" :size 6880}
                                 {:name "TooltipEvent" :size 3701}
                                 {:name "VisualizationEvent" :size 2117}]}
                     {:name     "legend"
                      :children [{:name "Legend" :size 20859}
                                 {:name "LegendRange" :size 10530}
                                 {:name "LegendItem" :size 4614}]}
                     {:name     "operator"
                      :children [{:name     "distortion"
                                  :children [{:name "Distortion" :size 6314}
                                             {:name "BifocalDistortion" :size 4461}
                                             {:name "FisheyeDistortion" :size 3444}]}
                                 {:name     "encoder"
                                  :children [{:name "PropertyEncoder" :size 4138}
                                             {:name "Encoder" :size 4060}
                                             {:name "ColorEncoder" :size 3179}
                                             {:name "SizeEncoder" :size 1830}
                                             {:name "ShapeEncoder" :size 1690}]}
                                 {:name     "filter"
                                  :children [{:name "FisheyeTreeFilter" :size 5219}
                                             {:name "VisibilityFilter" :size 3509}
                                             {:name "GraphDistanceFilter" :size 3165}]}
                                 {:name "IOperator" :size 1286}
                                 {:name     "label"
                                  :children [{:name "Labeler" :size 9956}
                                             {:name "RadialLabeler" :size 3899}
                                             {:name "StackedAreaLabeler" :size 3202}]}
                                 {:name     "layout"
                                  :children [{:name "RadialTreeLayout" :size 12348}
                                             {:name "NodeLinkTreeLayout" :size 12870}
                                             {:name "CirclePackingLayout" :size 12003}
                                             {:name "CircleLayout" :size 9317}
                                             {:name "TreeMapLayout" :size 9191}
                                             {:name "StackedAreaLayout" :size 9121}
                                             {:name "Layout" :size 7881}
                                             {:name "AxisLayout" :size 6725}
                                             {:name "IcicleTreeLayout" :size 4864}
                                             {:name "DendrogramLayout" :size 4853}
                                             {:name "ForceDirectedLayout" :size 8411}
                                             {:name "BundledEdgeRouter" :size 3727}
                                             {:name "IndentedTreeLayout" :size 3174}
                                             {:name "PieLayout" :size 2728}
                                             {:name "RandomLayout" :size 870}]}
                                 {:name "OperatorList" :size 5248}
                                 {:name "OperatorSequence" :size 4190}
                                 {:name "OperatorSwitch" :size 2581}
                                 {:name "Operator" :size 2490}
                                 {:name "SortOperator" :size 2023}]}])


(def dag-data {:nodes [{:name "Visit"}
                       {:name "Direct-Favourite"}
                       {:name "Page-Click"}
                       {:name "Detail-Favourite"}
                       {:name "Lost"}]
               :links [{:source 0 :target 1 :value 3728.3}
                       {:source 0 :target 2 :value 354170}
                       {:source 2 :target 3 :value 62429}
                       {:source 2 :target 4 :value 291741}]})


