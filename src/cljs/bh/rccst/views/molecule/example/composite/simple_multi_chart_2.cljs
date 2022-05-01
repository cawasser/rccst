(ns bh.rccst.views.molecule.example.composite.simple-multi-chart-2
  (:require [bh.rccst.ui-component.atom.chart.utils :as chart-utils]
            [bh.rccst.ui-component.molecule.composite.simple-multi-chart-2 :as widget]
            [bh.rccst.ui-component.molecule.grid-widget :as grid]
            [bh.rccst.ui-component.utils :as ui-utils]
            [bh.rccst.ui-component.utils.helpers :as h]
            [re-com.core :as rc]
            [re-frame.core :as re-frame]
            [reagent.core :as r]
            [taoensso.timbre :as log]
            [woolybear.ad.catalog.utils :as acu]
            [woolybear.ad.layout :as layout]))


(log/info "bh.rccst.views.molecule.example.composite.simple-multi-chart-2")


(defn- data-tools [data]
  (let [old-data (ui-utils/subscribe-local data [])]

    ;(log/info "data-tools" data "//" @old-data)

    (fn []
      [rc/h-box :src (rc/at)
       :gap "10px"
       :style {:border     "1px solid" :border-radius "3px"
               :box-shadow "5px 5px 5px 2px"
               :margin     "5px" :padding "5px"}
       :children [[:label.h5 "Input Data:"]

                  [rc/button :on-click #(h/handle-change-path data [] []) :label "Empty"]

                  [rc/button :on-click #(h/handle-change-path data [] widget/sample-data)
                   :label "Default"]

                  [rc/button :on-click #(h/handle-change-path data []
                                          (assoc-in @old-data [:data 0 :uv] 10000))
                   :label "A -> 10000"]

                  [rc/button :on-click #(h/handle-change-path data []
                                          (assoc @old-data :data
                                            (conj (:data @old-data)
                                              {:name "Page Q" :uv 1100
                                               :pv   1100 :tv 1100 :amt 1100})))
                   :label "Add 'Q'"]

                  [rc/button :on-click #(h/handle-change-path data []
                                          (assoc @old-data :data
                                            (into [] (drop-last 2 (:data @old-data)))))
                   :label "Drop Last 2"]

                  [rc/button :on-click #(h/handle-change-path data []
                                          (-> @old-data
                                            (assoc-in [:metadata :fields :new-item] :number)
                                            (assoc :data (into []
                                                           (map (fn [x]
                                                                  (assoc x :new-item (rand-int 7000)))
                                                             (:data @old-data))))))
                   :label "Add :new-item"]]])))


(defn- config-tools [config-data]
  (let [brush? (ui-utils/subscribe-local config-data [:brush])
        uv?    (ui-utils/subscribe-local config-data [:uv :include])
        tv?    (ui-utils/subscribe-local config-data [:tv :include])]

    ;(log/info "config-tools" config-data "//" @brush? "//" @uv?)

    (fn []
      [rc/h-box :src (rc/at)
       :gap "10px"
       :style {:border     "1px solid" :border-radius "3px"
               :box-shadow "5px 5px 5px 2px"
               :margin     "5px" :padding "5px"}
       :children [[:label.h5 "Config:"]
                  [rc/button :on-click #(h/handle-change-path config-data [] widget/default-config-data) :label "Default"]
                  [rc/button :on-click #(h/handle-change-path config-data [:brush] (not @brush?))
                   :label "!Brush"]
                  [rc/button :on-click #(h/handle-change-path config-data [:uv :include] (not @uv?))
                   :label "! uv data"]
                  [rc/button :on-click #(h/handle-change-path config-data [:tv :include] (not @tv?))
                   :label "! tv data"]

                  [chart-utils/color-config config-data ":amt :fill" [:amt :fill] :above-center]

                  [rc/button :on-click #((h/handle-change-path config-data [:uv :stackId] "b")
                                         (h/handle-change-path config-data [:pv :stackId] "b"))
                   :label "stack uv/pv"]
                  [rc/button :on-click #((h/handle-change-path config-data [:uv :stackId] "")
                                         (h/handle-change-path config-data [:pv :stackId] ""))
                   :label "!stack uv/pv"]
                  [rc/button :on-click #((h/handle-change-path config-data [:tv :stackId] "a")
                                         (h/handle-change-path config-data [:amt :stackId] "a"))
                   :label "stack tv/amt"]
                  [rc/button :on-click #((h/handle-change-path config-data [:tv :stackId] "")
                                         (h/handle-change-path config-data [:amt :stackId] ""))
                   :label "!stack tv/amt"]]])))


(defn- data-config-update-example [& {:keys [widget component-id] :as params}]
  ;(log/info "config-update-example (params)" params)

  [rc/v-box :src (rc/at)
   :gap "10px"
   :width "100%"
   :height "100%"
   :children [[:div.chart-part {:style {:width "100%"}}     ;:height "90%"}}
               widget]

              [rc/v-box :src (rc/at)
               :gap "8px"
               :children [[data-tools [component-id :blackboard :topic.data]]
                          [config-tools [component-id :blackboard :topic.config]]]]]])


(defn example []
  (let [container-id "simple-multi-chart-2"
        component-id (h/path->keyword container-id "widget")]
    (fn []
      (acu/demo "(A simple) Multiple Charts in a Widget (adding configuration)"
        "This example provides a 'widget' (collection of UI Components) organized into a digraph (Event Model) that
          describes the flow of data from sources (remote or local) into and out-of the UI.

> This example works on the entire 'data' item, rather than _reaching inside_ like
> `atom/example/chart/bar-chart/data-sub-example`
>
> See also `molecule/example/simple-multi-chart-2`
"
        [layout/frame
         ;;
         ;; NOTE: the :height MUST be specified here since the ResponsiveContainer down in bowels of the chart needs a height
         ;; in order to actually draw the Recharts components. just saying "100%" doesn't work, since the
         ;; that really means "be as big as you need" and ResponsiveContainer then doesn't know what to do.
         ;;
         [:div {:style {:width "100%" :height "800px"}}
          [data-config-update-example
           :widget [grid/component
                    ; TODO: why is this a RATOM????
                    :data (r/atom widget/ui-definition)
                    :component-id component-id]
           :component-id component-id]]]

        widget/source-code))))


(comment
  (do
    (def container-id "simple-multi-chart-2")
    (def component-id (h/path->keyword container-id "widget"))
    (def data [component-id :blackboard :topic.data])
    (def path [:data])
    (def old-data (ui-utils/resolve-subscribe-local data [:data]))

    (def value data)
    (def new-value (assoc-in @old-data [0 :uv] 10000)))

  (re-frame/subscribe [(h/path->keyword data)])


  (ui-utils/subscribe-local data [])
  (ui-utils/subscribe-local data [:data])
  (ui-utils/subscribe-local data [:data 0])
  (ui-utils/subscribe-local data [:data 0 :uv])


  (cond
    (or (coll? value)
      (keyword? value)
      (string? value)) (let [update-event (conj [(h/path->keyword value path)] new-value)]
                         ;(log/info "handle-change-path (update event)" update-event)
                         (re-frame/dispatch update-event))
    (instance? reagent.ratom.RAtom value) (swap! value assoc-in path new-value)
    (instance? Atom value) (swap! value assoc-in path new-value)
    :else ())


  (h/handle-change-path data [:data]
    (assoc-in @old-data [0 :uv] 10000))


  :simple-multi-chart.widget.blackboard.topic.data.data

  (:event @re-frame.registrar/kind->id->handler)

  (get-in @re-frame.registrar/kind->id->handler [:event :simple-multi-chart.widget.blackboard.topic.data])
  (get-in @re-frame.registrar/kind->id->handler [:event :simple-multi-chart.widget.blackboard.topic.data.data])


  ())

