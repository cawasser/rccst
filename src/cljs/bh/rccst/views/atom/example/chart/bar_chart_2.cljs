(ns bh.rccst.views.atom.example.chart.bar-chart-2
  (:require [bh.rccst.ui-component.atom.chart.bar-chart-2 :as chart]
            [bh.rccst.ui-component.atom.chart.utils :as chart-utils]
            [bh.rccst.ui-component.molecule.example :as example]
            [bh.rccst.ui-component.utils :as utils]
            [re-com.core :as rc]
            [reagent.core :as r]
            [taoensso.timbre :as log]))


(log/info "bh.rccst.views.atom.example.chart.bar-chart-2")



(defonce data-data (r/atom chart/sample-data))
(defonce data-config (r/atom chart/sample-data))

(def default-config-data {:brush false
                          :uv    {:include true, :fill "#ff0000", :stackId ""}
                          :pv    {:include true, :fill "#00ff00", :stackId ""}
                          :tv    {:include true, :fill "#0000ff", :stackId ""}
                          :amt   {:include true, :fill "#745ea5", :stackId ""}})
(defonce config-data (r/atom default-config-data))


(defn- data-tools []
  [rc/h-box :src (rc/at)
   :gap "10px"
   :style {:border     "1px solid" :border-radius "3px"
           :box-shadow "5px 5px 5px 2px"
           :margin     "5px" :padding "5px"}
   :children [[rc/button :on-click #(reset! data-data []) :label "Empty"]
              [rc/button :on-click #(reset! data-data chart/sample-data) :label "Default"]
              [rc/button :on-click #(swap! data-data assoc-in [:data 0 :uv] 10000) :label "A -> 10,000"]
              [rc/button :on-click #(swap! data-data assoc :data
                                      (conj (-> @data-data :data)
                                        {:name "Page Q" :uv 1100
                                         :pv   1100 :tv 1100 :amt 1100}))
               :label "Add 'Q'"]
              [rc/button :on-click #(swap! data-data assoc :data (into [] (drop-last 2 (:data @data-data))))
               :label "Drop Last 2"]
              [rc/button :on-click #(reset! data-data (-> @data-data
                                                        (assoc-in [:metadata :fields :new-item] :number)
                                                        (assoc :data (into []
                                                                       (map (fn [x]
                                                                              (assoc x :new-item 1750))
                                                                         (:data @data-data))))))
               :label "Add :new-item"]]])


(defn- data-update-example [& {:keys [data config-data container-id component-id] :as params}]
  (log/info "data-update-example (params)" params)

  [rc/v-box :src (rc/at)
   :gap "10px"
   :width "100%"
   :height "100%"
   :children [[:div.chart-part {:style {:width "100%" :height "90%"}}
               [chart/component
                :data data
                :config-data config-data
                :component-id component-id
                :container-id container-id
                :component-panel chart/component
                :data-panel chart-utils/meta-tabular-data-panel
                :config-panel chart/config-panel]]
              [:div.data-tools-part {:style {:width "100%"}}
               [data-tools]]]])


(defn- show-config [config-data]
  [rc/h-box :src (rc/at)
   :gap "10px"
   :style {:border     "1px solid" :border-radius "3px"
           :box-shadow "5px 5px 5px 2px"
           :margin     "5px" :padding "5px"}
   :children [[:p (str @config-data)]]])


(defn- config-tools []
  [rc/h-box :src (rc/at)
   :gap "10px"
   :style {:border     "1px solid" :border-radius "3px"
           :box-shadow "5px 5px 5px 2px"
           :margin     "5px" :padding "5px"}
   :children [[rc/button :on-click #(reset! config-data default-config-data) :label "Default"]
              [rc/button :on-click #(swap! config-data update-in [:brush] not) :label "!Brush"]
              [rc/button :on-click #(swap! config-data update-in [:uv :include] not) :label "! uv data"]
              [rc/button :on-click #(swap! config-data update-in [:tv :include] not) :label "! tv data"]
              [chart-utils/color-config config-data ":amt :fill" [:amt :fill] :above-center]
              [rc/button :on-click #(reset! config-data (-> @config-data
                                                          (assoc-in [:uv :stackId] "a")
                                                          (assoc-in [:tv :stackId] "a")))
               :label "stack"]
              [rc/button :on-click #(reset! config-data (-> @config-data
                                                          (assoc-in [:uv :stackId] "")
                                                          (assoc-in [:tv :stackId] "")))
               :label "un-stack"]]])


(defn- config-update-example [& {:keys [data config-data container-id component-id] :as params}]
  (log/info "config-update-example (params)" params)

  [rc/v-box :src (rc/at)
   :gap "10px"
   :width "100%"
   :height "100%"
   :children [[:div.chart-part {:style {:width "100%" :height "90%"}}
               [chart/component
                :data data
                :config-data config-data
                :component-id component-id
                :container-id container-id
                :component-panel chart/component]]

              [show-config config-data]

              [:div.config-tools-part {:style {:width "100%"}}
               [config-tools]]]])


(defn example []
  [:<>
   (let [container-id "bar-chart-2-data-demo"
         component-id (utils/path->keyword container-id "bar-chart-2")]
     [example/component-example
      :title "Bar Chart 2 (Live Data)"
      :description "A Bar Chart (2) built using [Recharts](https://recharts.org/en-US/api/BarChart). This example shows how
   charts can take [ratoms](http://reagent-project.github.io/docs/master/reagent.ratom.html) as input and re-render as the data changes."
      :data data-data
      :component data-update-example
      :container-id container-id
      :component-id component-id
      :source-code chart/source-code])

   (let [container-id "bar-chart-2-config-demo"
         component-id (utils/path->keyword container-id "bar-chart-2")]
     [example/component-example
      :title "Bar Chart 2 (Live Configuration)"
      :description "A Bar Chart (2) built using [Recharts](https://recharts.org/en-US/api/BarChart). This example shows how
     charts can take [ratoms](http://reagent-project.github.io/docs/master/reagent.ratom.html) as input and re-render as the configuration changes."
      :data data-config
      :extra-params {:config-data config-data}
      :component config-update-example
      :container-id container-id
      :component-id component-id
      :source-code chart/source-code])])


(comment

  (reset! data [])
  (reset! data chart/sample-data)



  (assoc-in {:dummy '({:one 1})} [:dummy 0 :new] 1000)
  (assoc-in @data [:data 0 :uv] 10000)
  (assoc @data :data (into [] (drop-last 2
                                (:data @data))))
  (-> @data
    (assoc-in [:metadata :fields :new] :number)
    (assoc :data (map #(assoc % :new 1750) (:data @data))))



  (swap! data assoc-in [:data 0 :uv] 10000)

  (swap! data
    assoc :data
    (conj (-> @data :data)
      {:name "Page Q" :uv 1100
       :pv   1100 :tv 1100 :amt 1100}))

  (swap! data assoc :data (into [] (drop-last 2
                                     (:data @data))))

  (reset! data (-> @data
                 (assoc-in [:metadata :fields :new-item] :number)
                 (assoc :data (map #(assoc % :new-item 1750)
                                (:data @data)))))



  ())


(comment

  (def p {:name "James" :age 26})
  (update-in p [:age] inc)


  (def data config-data)
  (def path [:uv :fill])

  (assoc-in @data path "#000000")
  (assoc-in @data [:uv :fill] "#000000")

  (swap! data assoc-in [:uv :fill] "#000000")


  ())


