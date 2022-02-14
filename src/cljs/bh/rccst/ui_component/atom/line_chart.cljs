(ns bh.rccst.ui-component.atom.line-chart
  (:require [bh.rccst.ui-component.atom.chart.utils :as utils]
            [bh.rccst.ui-component.atom.chart.wrapper :as c]
            [bh.rccst.ui-component.utils :as ui-utils]

            ["recharts" :refer [LineChart Line Brush]]
            [re-com.core :as rc]

            [reagent.core :as r]
            [taoensso.timbre :as log]
            [woolybear.ad.layout :as layout]))


(def sample-data
  "the Line Chart works best with \"tabular data\" so we return the tabular-data from utils"
  (r/atom utils/tabular-data))


; TODO: need to build the config from the columns of the tabular data itself, so we know how many there are!
;
(def local-config
  "provides both the definition and the initial default values for various properties that
  allow user to customize the visualization of the chart.

> See Also:
>
> [Recharts/line-chart](https://recharts.org/en-US/api/LineChart)
  "
  {:brush     false
   :line-uv   {:include true :stroke "#8884d8" :fill "#8884d8"}
   :line-pv   {:include true :stroke "#82ca9d" :fill "#82ca9d"}
   :line-amt  {:include false :stroke "#ff00ff" :fill "#ff00ff"}})


(defn- config
  "constructs the configuration panel for the chart's configurable properties. This is specific to
  this being a line-chart component (see [[local-config]]).

  Merges together the configuration needed for:

  1. line charts
  2. pub/sub between components of a container
  3. `default-config` for all Rechart-based types
  4. the `tab-panel` for view/edit configuration properties and data
  5. sets properties of the default-config (local config properties are just set inside [[local-config]])
  6. sets meta-data for properties this component publishes (`:pub`) or subscribes (`:sub`)

  ---

  - chart-id : (string) unique id of the chart
  "
  [chart-id]
  (-> ui-utils/default-pub-sub
    (merge
      utils/default-config
      {:type      "line-chart"
       :tab-panel {:value     (keyword chart-id "config")
                   :data-path [:widgets (keyword chart-id) :tab-panel]}}
      local-config)
    (assoc-in [:x-axis :dataKey] :name)
    (assoc-in [:pub] :name)
    (assoc-in [:sub] :something-selected)))


(defn- line-config [widget-id label path position]
  [rc/v-box :src (rc/at)
   :gap "5px"
   :children [[utils/boolean-config widget-id label (conj path :include)]
              [rc/h-box :src (rc/at)
               :gap "5px"
               :children [[utils/color-config widget-id ":stroke" (conj path :stroke) position]
                          [utils/color-config widget-id ":fill" (conj path :fill) position]]]]])


(defn- config-panel
  "the panel of configuration controls

  ---

  - data : (atom) data to display (may be used by the standard configuration components for thins like axes, etc.
  - config : (atom) holds all the configuration settings made by the user
  "
  [data chart-id]

  ;(log/info "config-panel" widget-id)

  [rc/v-box :src (rc/at)
   :gap "10px"
   :width "100%"
   :style {:padding          "15px"
           :border-top       "1px solid #DDD"
           :background-color "#f7f7f7"}
   :children [[utils/standard-chart-config data chart-id]
              [rc/line :src (rc/at) :size "2px"]
              [rc/h-box :src (rc/at)
               :gap "10px"
               :children [[line-config chart-id "line (uv)" [:line-uv] :above-right]
                          [line-config chart-id "line (pv)" [:line-pv] :above-right]
                          [line-config chart-id "line (amt)" [:line-amt] :above-center]]]
              [utils/boolean-config chart-id ":brush?" [:brush]]]])


;;;;;;;;;;;;;
;
; these more into ui-utils...
;
;;;;;;;;;;;;;
;; region
(defn build-subs [widget-id local-config]
  ; 1. process-locals
  ; 2. map over the result and call ui-utils/subscribe-local
  ; 3. put the result into a hash-map
  ())



(defn resolve-sub [sub]
  (deref sub))


(comment
  (ui-utils/process-locals [] nil {:brush     false
                                   :line-uv   {:include true :stroke "#8884d8" :fill "#8884d8"}
                                   :line-pv   {:include true :stroke "#82ca9d" :fill "#82ca9d"}
                                   :line-amt  {:include false :stroke "#ff00ff" :fill "#ff00ff"}})

  ())
;;endregion

(defn- component-panel
  "the chart to draw, taking cues from the settings of the configuration panel

  the component creates its own ID (a random-uuid) to hold the local state. This way multiple charts
  can be placed inside the same outer container/composite

  ---

  - data : (atom) any data shown by the component's ui
  - chart-id : (string) unique identifier for this chart instance within this container
  - container-id : (string) name of the container this chart is inside of
  "
  [data chart-id]
  (let [container (ui-utils/subscribe-local chart-id [:container])
        subscription (build-subs chart-id local-config)

        line-uv? (ui-utils/subscribe-local chart-id [:line-uv :include])
        line-uv-stroke (ui-utils/subscribe-local chart-id [:line-uv :stroke])
        line-uv-fill (ui-utils/subscribe-local chart-id [:line-uv :fill])
        line-pv? (ui-utils/subscribe-local chart-id [:line-pv :include])
        line-pv-stroke (ui-utils/subscribe-local chart-id [:line-pv :stroke])
        line-pv-fill (ui-utils/subscribe-local chart-id [:line-pv :fill])
        line-amt? (ui-utils/subscribe-local chart-id [:line-amt :include])
        line-amt-stroke (ui-utils/subscribe-local chart-id [:line-amt :stroke])
        line-amt-fill (ui-utils/subscribe-local chart-id [:line-amt :fill])
        isAnimationActive? (ui-utils/subscribe-local chart-id [:isAnimationActive])
        brush? (ui-utils/subscribe-local chart-id [:brush])]

    (fn []
      ;(log/info "component-panel" widget-id)

      (ui-utils/publish-to-container @container [chart-id :brush] @brush?)
      (ui-utils/publish-to-container @container [chart-id :line-uv] @line-uv?)
      (ui-utils/publish-to-container @container [chart-id :line-pv] @line-pv?)
      (ui-utils/publish-to-container @container [chart-id :line-amt] @line-amt?)

      [:> LineChart {:width 400 :height 400 :data @data}

       (utils/standard-chart-components chart-id)

       (when (resolve-sub brush?) [:> Brush]) ;(when @brush? [:> Brush])

       (when @line-uv? [:> Line {:type              "monotone" :dataKey :uv
                                 :isAnimationActive @isAnimationActive?
                                 :stroke            @line-uv-stroke
                                 :fill              @line-uv-fill}])

       (when @line-pv? [:> Line {:type              "monotone" :dataKey :pv
                                 :isAnimationActive @isAnimationActive?
                                 :stroke            @line-pv-stroke
                                 :fill              @line-pv-fill}])

       (when @line-amt? [:> Line {:type              "monotone" :dataKey :amt
                                  :isAnimationActive @isAnimationActive?
                                  :stroke            @line-amt-stroke
                                  :fill              @line-amt-fill}])])))


(def source-code '[:> LineChart {:width 400 :height 400 :data @data}])


(defn component
  ([data chart-id]
   [component data chart-id ""])


  ([data chart-id container-id]

   (let [id (r/atom nil)]

     (fn []
       (when (nil? @id)
         (reset! id chart-id)
         (ui-utils/init-widget @id (config @id))
         (ui-utils/dispatch-local @id [:container] container-id))

       ;(log/info "component" @id)

       [c/configurable-chart
        :data data
        :id @id
        :config-panel config-panel
        :component component-panel]))))

