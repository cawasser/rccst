(ns bh.rccst.ui-component.atom.funnel-chart
  (:require ["recharts" :refer [FunnelChart Funnel Cell LabelList XAxis YAxis CartesianGrid Tooltip Brush]]
            [bh.rccst.ui-component.atom.chart.utils :as utils]
            [bh.rccst.ui-component.atom.chart.wrapper :as c]
            [bh.rccst.ui-component.utils :as ui-utils]
            [re-com.core :as rc]
            [reagent.core :as r]
            [taoensso.timbre :as log]
            [woolybear.ad.buttons :as buttons]
            [woolybear.ad.icons :as icons]))


(def sample-data
  "the Funnel Chart works best with \"paired data\" so we return the paired-data from utils"
  (r/atom utils/meta-tabular-data))

(defn local-config [data]
      (let [d (get @data :data)
            fields (get-in @data [:metadata :fields])]

           (merge
             ; process options for :name
             (->> fields
                  (filter (fn [[k v]] (= :string v)))
                  keys
                  ((fn [m]
                       {:name {:keys m :chosen (first m)}})))

             ; process :name to map up the :colors
             (->> d
                  (map :name)
                  (#(zipmap % ui-utils/default-stroke-fill-colors))
                  (assoc {} :colors))

             ; process options for :value
             (->> fields
                  (filter (fn [[k v]] (= :number v)))
                  keys
                  ((fn [m]
                       {:value {:keys m :chosen (first m)}}))))))


(defn config
  "constructs the configuration panel for the chart's configurable properties. This is specific to
  this being a line-chart component (see [[local-config]]).

  Merges together the configuration needed for:

  1. funnel charts
  2. pub/sub between components of a container
  3. `default-config` for all Rechart-based types
  4. the `tab-panel` for view/edit configuration properties and data
  5. sets properties of the default-config (local config properties are just set inside [[local-config]])
  6. sets meta-data for properties this component publishes (`:pub`) or subscribes (`:sub`)

  ---

  - chart-id : (string) unique id of the chart
  "
  [chart-id data]

      (merge
        ui-utils/default-pub-sub
        utils/default-config
        (ui-utils/config-tab-panel chart-id)
        (local-config data)))


        ;{:tab-panel {:value     (keyword chart-id "config")
        ;             :data-path [:widgets (keyword chart-id) :tab-panel]}
        ; :colors    (zipmap (map :name utils/paired-data)
        ;              ["#8884d8" "#83a6ed" "#8dd1e1"
        ;               "#82ca9d" "#a4de6c" "#d7e62b"])}


(defn- color-anchors
  "build the config ui-components needed for each of the funnel slices
  "
  [chart-id]
  [:<>
   (doall
     (map (fn [[id _]]
            ^{:key id} [utils/color-config-text chart-id id [:colors id] :right-above])
       @(ui-utils/subscribe-local chart-id [:colors])))])


(defn config-panel
  "constructs the configuration panel for the chart's configurable properties. This is specific to
  this being a funnel-chart component (see [[local-config]]).

  Merges together the configuration needed for:

  1. funnel charts
  2. pub/sub between components of a container
  3. `default-config` for all Rechart-based types
  4. the `tab-panel` for view/edit configuration properties and data
  5. sets properties of the default-config (local config properties are just set inside [[local-config]])
  6. sets meta-data for properties this component publishes (`:pub`) or subscribes (`:sub`)

  ---

  - _ : (unused) this is where the container will pass in the data, unused by the funner chart's config
  - chart-id : (string) unique id of the chart
  "

  [_ chart-id]

  [rc/v-box :src (rc/at)
   :gap "10px"
   :width "100%"
   :style {:padding          "15px"
           :border-top       "1px solid #DDD"
           :background-color "#f7f7f7"}
   :children [[utils/non-gridded-chart-config chart-id]
              [rc/line :src (rc/at) :size "2px"]
              [utils/option chart-id ":name" [:name]]
              [rc/line :src (rc/at) :size "2px"]
              [utils/option chart-id ":value" [:value]]
              [rc/v-box :src (rc/at)
               :gap "5px"
               :children [[rc/label :src (rc/at) :label "Funnel Colors"]
                          (color-anchors chart-id)]]]])


(defn- component-panel
  "the chart to draw, taking cues from the settings of the configuration panel

  ---

  - data : (atom) any data shown by the component's ui
  - chart-id : (string) unique identifier for this chart instance
  "
  [data chart-id]
  (let [isAnimationActive? (ui-utils/subscribe-local chart-id [:isAnimationActive])
        subscriptions (ui-utils/build-subs chart-id (local-config data))]

    (fn [data chart-id]
      ;(log/info "configurable-funnel-chart" @config)
       [:> FunnelChart {:height 400 :width 500 :label true}

        (utils/non-gridded-chart-components chart-id)

        [:> Funnel {:dataKey           (ui-utils/resolve-sub subscriptions [:value :chosen])
                    :nameKey           (ui-utils/resolve-sub subscriptions [:name :chosen])
                    :label             true
                    :data              (get @data :data)
                    :isAnimationActive @isAnimationActive?}
         (doall
           (map-indexed
             (fn [idx {name :name}]
               ^{:key (str idx name)}
               [:> Cell {:key  (str "cell-" idx)
                         :fill (or (ui-utils/resolve-sub subscriptions [:colors name])
                                   (ui-utils/get-color 0))}])
             (get @data :data)))
         [:> LabelList {:position :right :fill "#000000" :stroke "none" :dataKey (ui-utils/resolve-sub subscriptions [:value :chosen])}]]])))


(def source-code `[:> FunnelChart {:height 400 :width 500}
                   [:> Funnel {:dataKey           :value
                               :nameKey           "name"
                               :label             true
                               :data              @data
                               :isAnimationActive @isAnimationActive?}]])

(defn component
      ([data component-id]
       [component data component-id ""])

      ([data component-id container-id]

       ;(log/info "funnel-chart" @data)

       (let [id (r/atom nil)]

              (fn []
                  (when (nil? @id)
                        (reset! id component-id)
                        (ui-utils/init-widget @id (config @id data))
                        (ui-utils/dispatch-local @id [:container] container-id))

                  ;(log/info "component" @id)

                  [c/configurable-chart
                   :data data
                   :id @id
                   :data-panel utils/meta-tabular-data-panel
                   :config-panel config-panel
                   :component component-panel]))))


