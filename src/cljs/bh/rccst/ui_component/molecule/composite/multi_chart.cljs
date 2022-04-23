(ns bh.rccst.ui-component.molecule.composite.multi-chart
  (:require [bh.rccst.ui-component.atom.chart.bar-chart :as bar-chart]
            [bh.rccst.ui-component.atom.chart.line-chart :as line-chart]
            [bh.rccst.ui-component.atom.chart.utils :as utils]
            [bh.rccst.ui-component.molecule.composite :as c]
            [bh.rccst.ui-component.utils :as ui-utils]
            [bh.rccst.ui-component.utils.color :as color]
            [bh.rccst.ui-component.utils.locals :as l]
            [re-com.core :as rc]
            [re-frame.core :as re-frame]
            [reagent.core :as r]
            [taoensso.timbre :as log]
            [woolybear.ad.containers :as containers]))


(log/info "bh.rccst.ui-component.molecule.composite.multi-chart")


(def sample-data line-chart/sample-data)


(declare config-panel-2)


(defn- compute-data-config [data]
  ;(log/info "compute-data-config" data)

  (let [ret (merge {:brush false}
              (->> (get-in data [:metadata :fields])
                (filter (fn [[k v]] (= :number v)))
                keys
                (map-indexed (fn [idx a]
                               {a {:include true
                                   :stroke  (color/get-color idx)
                                   :fill    (color/get-color idx)
                                   :stackId ""}}))
                (into {})))]
    ;(log/info "compute-data-config (ret)" ret)
    ret))


(defn fn-make-config [{:keys [data config-data container-id] :as params}]
  ; needs to implement something along the lines of:
  ;
  ; (l/update-local-values component-id (local-config d))
  ;
  ; and maybe...
  ;
  ; (ui-utils/build-subs component-id l-c)

  ;(log/info "fn-make-config" params)

  (re-frame/reg-sub
    (first config-data)
    :<- data
    (fn [d _]
      (doall
        (l/update-local-path-values container-id [:blackboard :topic.config] (compute-data-config d))))))


(def ui-definition
  "this data structure defines the composite in terms of the:

  - :components   - the subcomponents, like tables, charts, and such used for the actual UI as well as
  remote data sources (mapped to a local name) and internal pub/sub \"topics\"
  - :links        - linkages between the components
  - :grid-layout  - vector of layout data for the react-grid-layout component that positions the children
  "
  {:components  {:ui/line        {:type :ui/component :name :rechart/bar-2}
                 :ui/bar         {:type :ui/component :name :rechart/bar-2}
                 :ui/config      {:type :ui/component :name :stunt/config-panel}
                 :topic/data     {:type :source/local :name :topic/data :default @sample-data}
                 :topic/config   {:type :source/local :name :topic/config :default {}}
                 :fn/make-config {:type  :source/fn :name fn-make-config
                                  :ports {:data :port/sink :config-data :port/source-sink}}}

   :links       {:ui/config      {:config-data {:topic/config :data}}
                 :topic/data     {:data {:ui/line        :data
                                         :ui/bar         :data
                                         :fn/make-config :data}}
                 :topic/config   {:data {:ui/line   :config-data
                                         :ui/bar    :config-data
                                         :ui/config :config-data}}
                 :fn/make-config {:config-data {:topic/config :data}}}

   :grid-layout [{:i :ui/line :x 0 :y 0 :w 5 :h 11 :static true}
                 {:i :ui/config :x 5 :y 0 :w 2 :h 11 :static true}
                 {:i :ui/bar :x 7 :y 0 :w 5 :h 11 :static true}]})


(def source-code '[:div])


(defn- data-config [component-id label path position]
  ;(log/info "data-config" component-id label path position)
  [rc/v-box :src (rc/at)
   :gap "5px"
   :children [[utils/boolean-config component-id label (conj path :include)]
              [utils/color-config component-id ":stroke" (conj path :stroke) position]
              [utils/color-config component-id ":fill" (conj path :fill) position]
              [utils/text-config component-id ":stackId" (conj path :stackId)]]])


(defn- make-config [component-id data]
  (->> (get-in @data [:metadata :fields])
    (filter (fn [[k v]] (= :number v)))
    keys
    (map-indexed (fn [idx a]
                   [data-config component-id a [:blackboard a] :above-right]))
    (into [])))


(defn- config-panel [data component-id]
  [containers/v-scroll-pane
   {:width  "200px"
    :height "400px"}
   [rc/v-box :src (rc/at)
    ;:width "200px"
    ;:height "100%"
    :gap "2px"
    :children (apply merge
                [[utils/boolean-config component-id ":brush?" [:blackboard :brush]]]
                (make-config component-id data))]])


(defn local-config [data component-id]
  {:components [[[:div {:style {:width "400px"}}
                  [line-chart/component
                   :data data
                   :component-id (ui-utils/path->keyword component-id "line-chart")
                   :container-id component-id]]
                 [:div {:style {:width "150px"}}
                  [config-panel data component-id]]
                 [:div {:style {:width "400px"}}
                  [bar-chart/component
                   :data data
                   :component-id (ui-utils/path->keyword component-id "bar-chart")
                   :container-id component-id]]]]
   :blackboard (merge {:brush false}
                 (->> (get-in @data [:metadata :fields])
                   (filter (fn [[k v]] (= :number v)))
                   keys
                   (map-indexed (fn [idx a]
                                  {a {:include true
                                      :stroke  (color/get-color idx)
                                      :fill    (color/get-color idx)
                                      :stackId ""}}))
                   (into {})))})


(defn- config [component-id data]
  ;(log/info "config" component-id "//" data)

  (merge ui-utils/default-pub-sub
    (local-config data component-id)))


(defn- component-panel [data component-id container-id]

  (let [subscriptions (ui-utils/build-subs component-id (local-config data component-id))]

    (fn [data component-id container-id]
      [c/composite
       :id component-id
       :components (ui-utils/resolve-sub subscriptions [:components])
       :ui {}])))


(defn component
  ([& {:keys [data component-id container-id ui]}]

   ;(log/info "multi-chart" @data)

   (let [id (r/atom nil)]

     (fn []
       (when (nil? @id)
         (reset! id component-id)
         (ui-utils/init-widget @id (config @id data))
         (ui-utils/dispatch-local @id [:container] container-id))

       ;(log/info "multi-chart" @id (config @id data))

       [component-panel data @id container-id ui]))))



; testing for setting up subc/events to support wdigets
(comment
  @(re-frame/subscribe [:multi-chart-widget.widget.blackboard])
  @(re-frame/subscribe [:multi-chart-widget.widget.blackboard.topic.data])
  @(re-frame/subscribe [:multi-chart-widget.widget.blackboard.topic.config])


  @(re-frame/subscribe [:multi-chart-widget.widget.blackboard.topic.config.uv])
  @(re-frame/subscribe [:multi-chart-widget.widget.blackboard.topic.config.uv.include])
  @(re-frame/subscribe [:multi-chart-widget.widget.blackboard.topic.config.uv.fill])
  @(re-frame/subscribe [:multi-chart-widget.widget.blackboard.topic.config.uv.stroke])
  @(re-frame/subscribe [:multi-chart-widget.widget.blackboard.topic.config.uv.stackId])


  (re-frame/dispatch [:multi-chart-widget.widget.blackboard.topic.config.uv.include false])
  (re-frame/dispatch [:multi-chart-widget.widget.blackboard.topic.config.uv.include true])


  (get-in @re-frame.db/app-db [:widgets :multi-chart-widget.widget])
  (get-in @re-frame.db/app-db [:widgets :multi-chart-widget.widget :blackboard])
  (get-in @re-frame.db/app-db [:widgets :multi-chart-widget.widget
                               :blackboard (ui-utils/path->keyword :topic/config)])
  (get-in @re-frame.db/app-db [:widgets :multi-chart-widget.widget.blackboard])


  (reduce conj [:widget :dummy] [:blackboard :topic.config])

  (reduce conj [:widgets] [:dummy])
  (reduce conj [:widgets] [[:blackboard :topic.config]])

  (def container :multi-chart-widget.widget)
  (def container [:multi-chart-widget.widget :blackboard :topic.config])
  (def values "")
  (let [data-path (cond
                    (coll? container) (reduce conj [:widgets] container)
                    :else [:widget container])]
    (get-in @re-frame/app-db data-path values))


  ())


; play with the app-db configuration and subscriptions
(comment
  (do
    (def data sample-data)
    (def component-id "multi-chart-demo/multi-chart")
    (def container-id "")
    (def id (r/atom component-id))
    (def locals-and-defaults (config @id data)))

  (ui-utils/init-widget @id (config @id data))
  (ui-utils/dispatch-local @id [:container] container-id)

  (def subscriptions (ui-utils/build-subs component-id
                       (local-config data component-id)))
  (keys (get @re-frame.db/app-db :widgets))
  (get-in @re-frame.db/app-db [:widgets :multi-chart-demo/multi-chart])

  subscriptions

  (ui-utils/process-locals [] nil locals-and-defaults)

  (ui-utils/resolve-sub subscriptions [:components])
  (ui-utils/resolve-sub subscriptions [:blackboard :tv :include])

  ())




