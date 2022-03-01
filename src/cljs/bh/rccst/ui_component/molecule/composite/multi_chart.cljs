(ns bh.rccst.ui-component.molecule.composite.multi-chart
  (:require [bh.rccst.ui-component.atom.chart.bar-chart :as bar-chart]
            [bh.rccst.ui-component.atom.chart.line-chart :as line-chart]
            [bh.rccst.ui-component.atom.chart.utils :as utils]
            [bh.rccst.ui-component.molecule.composite :as c]
            [bh.rccst.ui-component.utils :as ui-utils]
            [bh.rccst.ui-component.utils.color :as color]
            [re-com.core :as rc]
            [reagent.core :as r]
            [taoensso.timbre :as log]
            [woolybear.ad.containers :as containers]))


(log/info "bh.rccst.ui-component.molecule.composite.multi-chart")


(def sample-data line-chart/sample-data)


(declare config-panel)


(def composite-def
  "this data structure defines the composite in terms of the:

  - :components - the subcomponents, like tables, charts, and such used for the actual UI
  - :topics     - remote data sources (mapped to a local name) along with internal pub/sub \"topics\"
  - :links      - linkages between the components and the data sources
  - :layout     - a \"DSL\" which defined how the components are to be organized on the display,
                  treated as a grid of rows and columns


  "
  {; the ui components (looked up in a registry), mapped to local names
   :components {:line-chart   {:type :chart/line-chart :configurable false}
                :bar-chart    {:type :chart/bar-chart :configurable false}
                :config-panel {:type config-panel}}

   ; remote data sources (mapped to a local name) along with internal pub/sub "topics"
   :topics     {:tabular-data [:remote :source-type/meta-tabular :source/sequence-of-measurements]
                :server-time  [:remote :time :source/server-time]
                :dag-data     [:remote :source-type/meta-dag :source/dag-data]
                :selected     [:local :string]
                :active-aoi   [:local :string]
                :current-time [:local :time]}

   ; links - how the different components get their data and if they publish or
   ; subscribe to the composite
   :links      {:line-chart   {:subs {:data      :tabular-data
                                      :selection :selected
                                      :time      :current-time}}
                :bar-chart    {:subs {:data :tabular-data
                                      :time :current-time}}
                :config-panel {:subs {:data :tabular-data}}
                :time-slider  {:pubs {:time :current-time}
                               :subs {:time :current-time}}}

   ; the physical layout of the components on the display
   :layout     [[:line-chart :config-panel :bar-chart]]})


(def source-code '[:div])


(defn- data-config [component-id label path position]
  (log/info "data-config" component-id label path position)
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




