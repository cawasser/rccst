(ns bh.rccst.views.atom.example.chart.alt.data-ratom-example
  (:require [bh.rccst.ui-component.molecule.example :as example]
            [bh.rccst.ui-component.utils :as utils]
            [bh.rccst.views.atom.example.chart.alt.show-data :as sd]
            [re-com.core :as rc]
            [reagent.core :as r]
            [taoensso.timbre :as log]))


(log/info "bh.rccst.views.atom.example.chart.alt.data-ratom-example")


(defn- data-update-example [component default-data
                            & {:keys [data config-data container-id component-id
                                      data-tools data-panel config-panel] :as params}]

  ;(log/info "data-update-example (params)" params)
  ;(log/info "data-update-example" default-data)

  [rc/v-box :src (rc/at)
   :class "data-update-example"
   :gap "10px"
   :width "100%"
   :height "100%"
   :children [[:div.chart-part {:style {:width "100%" :height "70%"}}
               [component
                :data data
                :config-data config-data
                :component-id component-id
                :container-id container-id
                :data-panel data-panel
                :config-panel config-panel]]
              [rc/v-box
               :gap "5px"
               :style {:width "100%" :height "30%"}
               :children [[sd/show-data data]
                          [data-tools data default-data]]]]])


(defn example [& {:keys [container-id
                         title description
                         sample-data data-tools
                         source-code
                         component data-panel config-panel] :as params}]
  (let [component-id (utils/path->keyword container-id "chart")
        data         (r/atom sample-data)
        input-params (assoc params
                       :data data
                       :component-id component-id
                       :component (partial data-update-example component sample-data))

        ret (reduce into [example/component-example] (seq input-params))]

    (log/info "example" ret)
      ;"//////" params
      ;"//////" input-params)

    ret))
    ;[example/component-example
    ; :title title
    ; :description description
    ; :data data
    ; :component (partial data-update-example component sample-data)
    ; :extra-params {:data-tools data-tools
    ;                :data-panel data-panel
    ;                :config-panel config-panel}
    ; :container-id container-id
    ; :component-id component-id
    ; :source-code source-code]))

(comment
  (def input-params {:dummy "dummy"})
  (reduce into [example/component-example] (seq input-params))

  ())





