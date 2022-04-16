(ns bh.rccst.ui-component.atom.chart.bar-chart-2
  (:require [bh.rccst.ui-component.atom.chart.utils :as utils]
            [bh.rccst.ui-component.atom.re-com.configure-toggle :as ct]
            [bh.rccst.ui-component.utils :as ui-utils]
            [bh.rccst.ui-component.utils.color :as color]
            [bh.rccst.ui-component.utils.example-data :as example-data]
            [bh.rccst.ui-component.utils.helpers :as h]
            [bh.rccst.ui-component.utils.locals :as l]
            [re-com.core :as rc]
            [reagent.core :as r]
            [taoensso.timbre :as log]
            [woolybear.ad.layout :as layout]
            ["recharts" :refer [ResponsiveContainer BarChart Bar Brush
                                XAxis YAxis CartesianGrid Tooltip Legend]]))


(log/info "bh.rccst.views.atom.example.chart.bar-chart-2")


(def source-code '[])
(def sample-data example-data/meta-tabular-data)


(defn local-config [data]

  (log/info "local-config" @data)

  (let [ret (merge
              {:brush false}
              (->> (get-in @data [:metadata :fields])
                (filter (fn [[k v]] (= :number v)))
                keys
                (map-indexed (fn [idx a]
                               {a {:include true
                                   :fill    (color/get-color idx)
                                   :stackId ""}}))
                (into {})))]
    (log/info "local-config" ret)
    ret))


(defn config [chart-id data]

  (log/info "config" chart-id "//" @data)

  (let [ret (-> ui-utils/default-pub-sub
              (merge
                utils/default-config
                {:tab-panel {:value     (keyword chart-id "config")
                             :data-path [:widgets (keyword chart-id) :tab-panel]}}
                (local-config data))
              (assoc-in [:x-axis :dataKey] :name)
              ; TODO: this should be produced by a function that processes the data
              (assoc-in [:sub] [[:brush]
                                [:uv :include] [:uv :stroke] [:uv :fill]
                                [:pv :include] [:pv :stroke] [:pv :fill]]))]
    (log/info "config" ret)
    ret))


(defn- bar-config [component-id label path position]
  [rc/v-box :src (rc/at)
   :gap "5px"
   :children [[utils/boolean-config component-id label (conj path :include)]
              [utils/color-config component-id ":fill" (conj path :fill) position]
              [utils/text-config component-id ":stackId" (conj path :stackId)]]])


(defn- make-bar-config [component-id data]
  ;(log/info "make-bar-config" component-id "//" @data)

  (->> (get-in @data [:metadata :fields])
    (filter (fn [[k v]] (= :number v)))
    keys
    (map-indexed (fn [idx a]
                   [bar-config component-id a [a] :above-right]))
    (into [])))


(defn config-panel [data component-id]
  ;(log/info "config-panel" component-id "//" @data)

  [rc/v-box :src (rc/at)
   :gap "10px"
   :width "50%"
   :style {:padding          "15px"
           :border-top       "1px solid #DDD"
           :background-color "#f7f7f7"}
   :children [[utils/standard-chart-config data component-id]
              [rc/line :src (rc/at) :size "2px"]
              [rc/h-box :src (rc/at)
               :width "100%"
               :style ui-utils/h-wrap
               :gap "10px"
               :children (make-bar-config component-id data)]
              [rc/line :src (rc/at) :size "2px"]
              [utils/boolean-config component-id ":brush?" [:brush]]]])


(defn- make-bar-display [data subscriptions isAnimationActive?]
  ;(log/info "make-bar-display" data "//" subscriptions)

  (let [ret (->> (get-in data [:metadata :fields])
              (filter (fn [[_ v]] (= :number v)))
              keys
              (map (fn [a]
                     (if (ui-utils/resolve-sub subscriptions [a :include])
                       [:> Bar (merge {:type "monotone" :dataKey a
                                       ;:isAnimationActive @isAnimationActive?
                                       :fill (ui-utils/resolve-sub subscriptions [a :fill])}
                                 (when (seq (ui-utils/resolve-sub subscriptions [a :stackId]))
                                   {:stackId (ui-utils/resolve-sub subscriptions [a :stackId])}))]
                       [])))
              (remove empty?)
              (into [:<>]))]
    (log/info "ret" ret)

    ret))


(defn- component-panel* [& {:keys [data component-id container-id subscriptions isAnimationActive?]}]
  (let [d (if (empty? data) [] (get data :data))]

    ;(log/info "component-panel*" component-id "//" data "//" d)

    [:> ResponsiveContainer
     [:> BarChart {:data d}

      (utils/standard-chart-components component-id {})

      (when (ui-utils/resolve-sub subscriptions [:brush]) [:> Brush])

      (make-bar-display data subscriptions isAnimationActive?)]]))


(defn- component-panel [& {:keys [data component-id container-id]}]

  (let [d                  (h/resolve-value data)
        isAnimationActive? (ui-utils/subscribe-local component-id [:isAnimationActive])]
        ;override-subs      @(ui-utils/subscribe-local component-id [:sub])]

    ;(log/info "component-panel" data "//" @d)

    (fn []

      (l/update-local-values component-id (local-config d))

      (let [l-c           (local-config d)
            local-subs    (ui-utils/build-subs component-id l-c)]
            ;subscriptions (ui-utils/override-subs container-id local-subs override-subs)]

        ;(log/info "component-panel (render)" component-id "//" @d "//" local-subs)

        (if (empty? @d)
          [rc/alert-box :src (rc/at)
           :alert-type :info
           :style {:width "100%" :height "100%"}
           :heading "Waiting for data"]

          [component-panel*
           :data @d
           :component-id component-id
           :container-id container-id
           :subscriptions local-subs
           :isAnimationActive? isAnimationActive?])))))


(defn configurable-component-panel [& {:keys [data component-id container-id]}]

  (let [open?        (r/atom false)
        config-key   (keyword component-id "config")
        data-key     (keyword component-id "data")
        tab-panel    (ui-utils/path->keyword component-id "tab-panel")
        selected-tab (ui-utils/path->keyword component-id "tab-panel.value")
        chart-events [config-key data-key tab-panel selected-tab]
        d            (h/resolve-value data)]

    ;(log/info "configurable-component" component-id "//" data "//" @d)

    (ui-utils/init-widget component-id (config component-id d))

    (fn []
      [rc/v-box :src (rc/at)
       :gap "2px"
       :children [[rc/h-box :src (rc/at)
                   :justify :end
                   :children [[ct/configure-toggle open?]]]
                  [layout/centered {:extra-classes :is-one-third}
                   [rc/h-box :src (rc/at)
                    :gap "5px"
                    :width "600px"
                    :height "600px"
                    :children (conj
                                (if @open?
                                  [[layout/centered {:extra-classes :is-one-third}
                                    [:div {:width "75%"}
                                     [ui-utils/chart-config
                                      chart-events
                                      [utils/meta-tabular-data-panel @d]
                                      [config-panel d component-id]]]]]
                                  [])
                                [component-panel
                                 :data data
                                 :component-id component-id
                                 :container-id container-id])]]]])))


(defn component [& {:keys [data component-id container-id]}]

  (let [id (r/atom nil)
        d  (h/resolve-value data)
        c  (config component-id d)]

    ;(log/info "component" data "//" d "//" @d)

    (fn []
      (when (nil? @id)
        ;(log/info "initializing" component-id)
        (reset! id component-id)
        (ui-utils/init-widget @id c)
        (ui-utils/dispatch-local @id [:container] container-id))

      [configurable-component-panel
       :data data
       :component-id component-id
       :container-id (or container-id "")])))



(def meta-data {:rechart/bar-2 {:component component
                                ;:configurable-component configurable-component
                                :ports     {:data   :port/sink
                                            :config :port/sink}}})


(comment
  (def component-id ":bar-chart-2-demo.bar-chart-2")
  (def data {:metadata {} :data []})
  (def data example-data/meta-tabular-data)

  (def d (h/resolve-value data))



  (->> (get-in data [:metadata :fields])
    (filter (fn [[_ v]] (= :number v)))
    keys
    (map (fn [a]
           [:> Bar {:type "monotone" :dataKey a :fill "#aa0000"}]))
    (remove empty?)
    (into [:<>]))



  ())

