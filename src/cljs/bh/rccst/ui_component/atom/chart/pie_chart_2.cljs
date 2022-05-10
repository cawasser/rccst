(ns bh.rccst.ui-component.atom.chart.pie-chart-2
  (:require [bh.rccst.ui-component.atom.chart.utils :as utils]
            [bh.rccst.ui-component.utils :as ui-utils]
            [bh.rccst.ui-component.utils.color :as color]
            [bh.rccst.ui-component.utils.example-data :as example-data]
            [bh.rccst.ui-component.atom.chart.wrapper-2 :as wrapper]
            [re-com.core :as rc]
            [taoensso.timbre :as log]
            ["recharts" :refer [ResponsiveContainer PieChart Pie Cell]]))


(log/info "bh.rccst.views.atom.example.chart.pie-chart-2")


(def source-code '[])
(def sample-data example-data/meta-tabular-data)

(defn local-config [data]

  (log/info "local-config" @data)

  (let [d      (get @data :data)
        fields (get-in @data [:metadata :fields])]

    (merge
      {:fill (color/get-color 0)}

      ; process options for :name
      (->> fields
           (filter (fn [[k v]] (= :string v)))
           keys
           ((fn [m]
              {:name {:keys m :chosen (first m)}})))

      ; process options for :value
      (->> fields
           (filter (fn [[k v]] (= :number v)))
           keys
           ((fn [m]
              {:value {:keys m :chosen (first m)}}))))))


(defn config [component-id data]
      (merge
        ui-utils/default-pub-sub
        utils/default-config
        (ui-utils/config-tab-panel component-id)
        (local-config data)))

(defn- config-panel [data component-id]
  [rc/v-box :src (rc/at)
   :gap "10px"
   :width "100%"
   :height "500px"
   :style {:padding          "15px"
           :border-top       "1px solid #DDD"
           :background-color "#f7f7f7"}
   :children [[utils/non-gridded-chart-config @data component-id]
              [rc/line :src (rc/at) :size "2px"]
              [utils/option component-id ":name" [:name]]
              [rc/line :src (rc/at) :size "2px"]
              [utils/option component-id ":value" [:value]]
              [utils/color-config-text component-id ":fill" [:fill] :above-right]]])

(defn- included-cells [data subscriptions]
  (->> data
       (filter (fn [{:keys [name]}] (ui-utils/resolve-sub subscriptions [name :include])))
       (into [])))

(defn- component* [& {:keys [data component-id container-id
                             subscriptions isAnimationActive?]
                      :as params}]
  (let [d (if (empty? data) [] (get data :data))
        included (included-cells d subscriptions)]

      [:> ResponsiveContainer
       [:> PieChart {:label (utils/override true {} :label)}

        (utils/non-gridded-chart-components component-id {})

        [:> Pie {:dataKey           (ui-utils/resolve-sub subscriptions [:value :chosen])
                 :nameKey           (ui-utils/resolve-sub subscriptions [:name :chosen])
                 :data              included
                 :fill              (ui-utils/resolve-sub subscriptions [:fill])
                 :label             (utils/override true {} :label)
                 :isAnimationActive @isAnimationActive?}]]]))

(defn component [& {:keys [data config-data component-id container-id
                           data-panel config-panel] :as params}]

  ;(log/info "component-2" params)

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

(def meta-data {:component              component
                ;:configurable-component configurable-component
                :sources                {:data :source-type/meta-tabular}
                :pubs                   []
                :subs                   []})

