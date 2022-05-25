(ns bh.rccst.ui-component.atom.chart.funnel-chart
  (:require [bh.rccst.ui-component.atom.chart.utils :as utils]
            [bh.rccst.ui-component.atom.chart.wrapper-2 :as wrapper]
            [bh.rccst.ui-component.utils :as ui-utils]
            [bh.rccst.ui-component.utils.color :as color]
            [bh.rccst.ui-component.utils.example-data :as data]
            [re-com.core :as rc]
            [taoensso.timbre :as log]

            ["recharts" :refer [ResponsiveContainer FunnelChart Funnel Cell LabelList
                                XAxis YAxis CartesianGrid Tooltip Brush]]))


(log/info "bh.rccst.ui-component.atom.chart.funnel-chart")


(def sample-data
  "the Funnel Chart works best with \"paired data\" so we return the paired-data from utils"
  data/meta-tabular-data)


(defn local-config [data]
  (log/info "local-config" data)
  (let [d      (get @data :data)
        fields (get-in @data [:metadata :fields])]
    ;(log/info "configgg : " @data)
    (merge
      ; process options for :name
      (->> fields
        (filter (fn [[k v]] (= :string v)))
        keys
        ((fn [m]
           {:name {:keys m :chosen (first m)}})))

      ; process :name to map up the :colors
      (->> d
        (map-indexed (fn [idx entry]
                       {(ui-utils/path->keyword (:name entry))
                        {:name  (:name entry)
                         :color (nth (cycle color/default-stroke-fill-colors) idx)}}))
        (into {})
        (assoc {} :colors))

      ; process options for :value
      (->> fields
        (filter (fn [[k v]] (= :number v)))
        keys
        ((fn [m]
           {:value {:keys m :chosen (first m)}}))))))


(defn config [component-id data]
  (log/info "config" @data)
  (merge
    ui-utils/default-pub-sub
    utils/default-config
    (ui-utils/config-tab-panel component-id)
    (local-config data)))


(defn- color-anchors [component-id]
  [:<>
   (doall
     (map (fn [[id color-data]]
            (let [text (:name color-data)]
              ^{:key id} [utils/color-config-text component-id text [:colors id :color] :right-above]))
       @(ui-utils/subscribe-local component-id [:colors])))])


(defn config-panel [data component-id]
  (log/info "config-panel")
  [rc/v-box :src (rc/at)
   :gap "10px"
   :width "100%"
   :style {:padding          "15px"
           :border-top       "1px solid #DDD"
           :background-color "#f7f7f7"}
   :children [[utils/non-gridded-chart-config component-id]
              [rc/line :src (rc/at) :size "2px"]
              [utils/option component-id ":name" [:name]]
              [rc/line :src (rc/at) :size "2px"]
              [utils/option component-id ":value" [:value]]
              [rc/v-box :src (rc/at)
               :gap "5px"
               :children [[rc/label :src (rc/at) :label "Funnel Colors"]
                          (color-anchors component-id)]]]])


(defn make-cells [data subscriptions]
  (let [ret (->> data
              (map-indexed
                (fn [idx {name :name}]
                  ^{:key (str idx name)}
                  [:> Cell {:key  (str "cell-" idx)
                            :fill (or (ui-utils/resolve-sub subscriptions [:colors name :color])
                                     (color/get-color 0))}])))]
    ret))


(defn- component* [& {:keys [data component-id container-id
                             subscriptions isAnimationActive?]
                      :as   params}]

  (log/info "funnel component*" params)
  (let [d (if (empty? data) [] (get data :data))]

    [:> ResponsiveContainer
     [:> FunnelChart {:label true}
      ;(utils/override true {} :label)

      ;(utils/non-gridded-chart-components component-id {})

      [:> Funnel {:dataKey           (ui-utils/resolve-sub subscriptions [:value :chosen])
                  :nameKey           (ui-utils/resolve-sub subscriptions [:name :chosen])
                  :label             true
                  :data              d
                  :isAnimationActive @isAnimationActive?}
       (make-cells d subscriptions)
       [:> LabelList {:position :right :fill "#000000" :stroke "none" :dataKey (ui-utils/resolve-sub subscriptions [:value :chosen])}]]]]))


(def source-code `[:> FunnelChart {:height 400 :width 500}
                   [:> Funnel {:dataKey           :value
                               :nameKey           "name"
                               :label             true
                               :data              @data
                               :isAnimationActive @isAnimationActive?}]])


(defn component [& {:keys [data config-data component-id container-id
                           data-panel config-panel] :as params}]

  (log/info "component-2 funnel" params)

  [wrapper/base-chart
   :data data
   :config-data config-data
   :component-id component-id
   :container-id container-id
   :component* component*
   :component-panel wrapper/component-panel
   :data-panel data-panel
   :config-panel config-panel
   :config config
   :local-config local-config])


(def meta-data {:component component
                :sources   {:data :source-type/meta-tabular}
                :pubs      []
                :subs      []})

(comment

  ())










