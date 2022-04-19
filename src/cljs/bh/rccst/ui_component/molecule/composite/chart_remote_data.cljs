(ns bh.rccst.ui-component.molecule.composite.chart-remote-data)


(def ui-definition {:title        "Chart with remote Data"
                    :component-id :chart-remote-data
                    :components   {:ui/bar-chart       {:type :ui/component :name :rechart/bar-2}
                                   :topic/measurements {:type :source/remote :name :source/measurements}}
                    :links        {:topic/measurements {:data {:ui/bar-chart :data}}}
                    :grid-layout  [{:i :ui/bar-chart :x 0 :y 0 :w 9 :h 17 :static true}]})
