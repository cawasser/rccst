(ns bh.rccst.ui-component.atom.oz.line-chart
  (:require [bh.rccst.ui-component.atom.chart.utils :as utils]
            [bh.rccst.ui-component.atom.chart.wrapper :as c]
            [bh.rccst.ui-component.utils :as ui-utils]

            [oz.core :as oz]
            [re-com.core :as rc]

            [reagent.core :as r]
            [taoensso.timbre :as log]))


(defn play-data [& names]
  (for [n names
        i (range 20)]
    {:time i
     :item n
     :quantity (+ (Math/pow (* i (count n)) 0.8)
                 (rand-int (count n)))}))


(def sample-data
  "the Line Chart works best with \"tabular data\" so we return the tabular-data from utils"
  (r/atom (play-data "monkey" "slipper" "broom")))


(def source-code '[oz.core/vega-lite
                   {:data {:values (get @data :data)}
                    :mark "bar"
                    :encoding {:x {:field "time"
                                   :type "ordinal"}
                               :y {:aggregate "sum"
                                   :field "quantity"
                                   :type "quantitative"}
                               :color {:field "item"
                                       :type "nominal"}}}])


(defn- config [chart-id data]
  (-> ui-utils/default-pub-sub
    (merge
      utils/default-config
      {:type      "oz-line-chart"}
      (ui-utils/config-tab-panel chart-id))))


(defn- config-panel
  [data chart-id]

  [:div "config panel here"])


(defn data []
  {:width 250
   :height 300
   :autosize {:type "fit"
              :contains "padding"}
   :background "#000"
   :data {:values (play-data "munchkin" "witch" "dog" "lion" "tiger" "bear")}
   :mark "line"
   :encoding {:y {:field "quantity" :type "quantitative"}
              :x {:field "time" :type "ordinal"}
              :color {:field "item" :type "nominal"}}})

(defn- component-panel
  [_ chart-id]

  [:div {:style {:width "400px" :height "500px"}}
   [oz/vega-lite (data)]])



(comment
  (def data sample-data)

  @data
  ())

(defn component
  ([data chart-id]
   [component data chart-id ""])


  ([data chart-id container-id]

   ;(log/info "line-chart" @data)

    ;(if (not= :tabular (get-in @data [:metadata :type]))
    ;  [rc/alert-box :src (rc/at)
    ;   :id (str container-id "/" chart-id ".ERROR")
    ;   :alert-type :danger
    ;   :closeable? false
    ;   :body [:div "The data passed is NOT of type :tabular!"]]

   (let [id (r/atom nil)]

     (fn []
       (when (nil? @id)
         (reset! id chart-id)
         (ui-utils/init-widget @id (config @id data))
         (ui-utils/dispatch-local @id [:container] container-id))

       ;(log/info "component" @id)

       [c/configurable-chart
        :data data
        :id @id
        :data-panel utils/dummy-data-panel
        :config-panel config-panel
        :component component-panel]))))


(comment
  [:div
   [oz.core/vega {}]
   [oz.core/vega-lite {}]]


  ())