(ns bh.rccst.ui-component.molecule.composite.coverage-plan
  "provide a composed UI for a \"Coverage Plan\" which shows targets and satellite coverage areas
  on a 3D globe"
  (:require [bh.rccst.ui-component.molecule.composite.coverage-plan.support :as s]
            [bh.rccst.ui-component.utils :as ui-utils]
            [bh.rccst.ui-component.utils.helpers :as h]
            [cljs-time.coerce :as coerce]
            [cljs-time.core :as t]
            [re-frame.core :as re-frame]
            [reagent.core :as r]
            [taoensso.timbre :as log]
            ["dagre" :as dagre]
            ["graphlib" :as graphlib]
            ["react-flow-renderer" :refer (ReactFlowProvider Controls Handle Background) :default ReactFlow]))


(log/info "bh.rccst.ui-component.molecule.composite.coverage-plan")


;; region ; data for developing the UI


(def dummy-targets [{:name  "alpha-hd" :cells #{[7 7 "hidef-image" 0]
                                                [7 6 "hidef-image" 1]
                                                [7 6 "hidef-image" 2]
                                                [7 5 "hidef-image" 3]}
                     :color [:green "rgba(0, 128, 0, .3)" [0.0 0.5 0.0 0.1]]}
                    {:name  "bravo-img" :cells #{[7 2 "image" 0]
                                                 [7 1 "image" 1]}
                     :color [:blue "rgba(0, 0, 255, .3)" [0.0 0. 1.0 0.1]]}
                    {:name  "fire-hd" :cells #{[5 3 "hidef-image" 0]
                                               [4 3 "hidef-image" 2] [5 3 "hidef-image" 2]
                                               [4 3 "hidef-image" 3] [5 3 "hidef-image" 3]}
                     :color [:orange "rgba(255, 165, 0, .3)" [1.0 0.65 0.0 0.3]]}
                    {:name  "fire-ir" :cells #{[5 4 "v/ir" 0]
                                               [5 3 "v/ir" 1] [5 4 "v/ir" 1]
                                               [5 4 "v/ir" 2]
                                               [5 4 "v/ir" 3]}
                     :color [:grey "rgba(128, 128, 128, .3)" [0.5 0.5 0.5 0.3]]}
                    {:name  "severe-hd" :cells #{[5 6 "hidef-image" 0]
                                                 [5 7 "hidef-image" 1] [6 5 "hidef-image" 1]
                                                 [6 6 "hidef-image" 2]
                                                 [5 7 "hidef-image" 3]}
                     :color [:cornflowerblue "rgba(100, 149, 237, .3)" [0.4 0.58 0.93 0.3]]}])


(def dummy-satellites #{"avhhr-6" "viirs-5" "abi-meso-11"
                        "abi-meso-4" "abi-meso-10" "abi-meso-2"})


;; endregion


;; region ; local function to support :source/local topics

(defn fn-coverage
  "registers the subscription for the entity defined by 'layers'. processing from
  inputs to the output is performed by 'some-computation'

  this function assumes that the caller provides fully-qualified signal vectors, so the CALLER
  is responsible for building the keyword (using path->keyword)

  - targets : (vector of keywords) the subscription signal for the target data
  - satellites : (vector of keywords) the subscription signal for the 'selected' satellite data
  - coverages : (vector of keywords) the subscription signal for the coverage data

  builds and registers the subscription provided by 'layers'

  "
  [{:keys [targets satellites coverages current-time shapes]}]

  ;(log/info "fn-coverage" shapes
  ;  "//" targets
  ;  "//" satellites
  ;  "//" coverages)

  (re-frame/reg-sub
    (first shapes)
    :<- targets
    :<- satellites
    :<- coverages
    :<- current-time
    (fn [[t s c ct] _]
      ;(log/info "fn-coverage (sub)" ct
      ;  "// (targets)" t)
      ;  ;"// (satellites)" s
      ;  ;"// (cooked)" (s/cook-coverages c ct)
      ;  "// (filter)" (filter #(contains? s (get-in % [:coverage :sensor]))
      ;                  (s/cook-coverages c ct)))

      (let [cvg (if (or (empty? c) (empty? (:data c)))
                  []
                  (map s/make-coverage-shape (filter #(contains? s (get-in % [:coverage :sensor]))
                                               (s/cook-coverages c ct))))
            trg (if (empty? t)
                  []
                  (map s/make-target-shape (s/cook-targets t ct)))
            ret (concat cvg trg)]

        ;(log/info "fn-coverage (ret)" ret
        ;  "//" cvg
        ;  "//" trg)

        ret))))


(defn fn-range
  "registers the subscription for the entity defined by 'selected'. processing from
  inputs to output is performed by 'some-computation'

  - data : (vector of keywords) the subscription signal for the input data
  - container-id : (string) name of the container holding the blackboard

  builds and registers the subscription :<container>/blackboard.<selected>
  "
  [{:keys [data range]}]

  ;(log/info "fn-range" range "//" data)

  (re-frame/reg-sub
    (first range)
    :<- data
    (fn [d _]
      (let [times (->> d :data (map :time) set)]
        [(apply min times) (apply max times)]))))


(defn fn-current-time [{:keys [value current-time]}]
  ;(log/info "fn-current-time" value "//" current-time)

  (re-frame/reg-sub
    (first current-time)
    :<- value
    (fn [v _]
      (coerce/to-date (t/plus (t/now) (t/hours v))))))


(defn fn-color-targets [{:keys [data colored]}]
  ; (log/info "fn-color-targets" data "//" colored)
  (let [next-color (atom -1)]
    (re-frame/reg-sub
      (first colored)
      :<- data
      (fn [d _]
        ;(log/info "fn-color-targets (data)" d "//" (:data d))
        (let [cnt (count s/sensor-color-pallet)
              ret (map #(do
                          (assoc % :color (nth s/sensor-color-pallet (mod (swap! next-color inc) cnt))))
                    (:data d))]
          ;(log/info "fn-color-targets (ret)" d "//" (:data d) "//" ret)
          ret)))))


(defn fn-color-satellites [{:keys [data colored]}]
  ; (log/info "fn-color-satellites" data "//" colored)
  (let [next-color (atom -1)]
    (re-frame/reg-sub
      (first colored)
      :<- data
      (fn [d _]
        ;(log/info "fn-color-satellites (data)" d "//" (:data d))
        (let [cnt (count s/sensor-color-pallet)
              ret (map #(do
                          (assoc % :color (nth s/sensor-color-pallet (mod (swap! next-color inc) cnt))))
                    (:data d))]
          ;(log/info "fn-color-satellites (ret)" d "//" (:data d) "//" ret)
          ret)))))


;; endregion


;; region ; custom tables for display

(defn- display-checkbox [id name under-consideration]
  ^{:key (str "inc-" name)}
  [:td.is-narrow
   {:style    {:text-align :center}
    :on-click #()}

   (if (contains? under-consideration id)
     [:span.icon.has-text-success.is-small [:i.fas.fa-check]]
     [:span.icon.has-text-success.is-small [:i.far.fa-square]])])


(defn- display-symbol [name [color _ _]]
  ;(log/info "display-symbol" name color)
  ^{:key (str "symb-" name)}
  [:td {:style    {:color      :white
                   :text-align :center}
        :on-click #(do)}
   [:span.icon.has-text-success.is-small
    [:i.fas.fa-circle
     {:style {:color (or color :green)}}]]])


(defn- display-edit-control [name is-editing]
  ^{:key (str "edit-" name)}
  [:td {:on-click #(if (and
                         @is-editing
                         (= name @is-editing))
                     (do
                       (log/info "SAVE" name)
                       (reset! is-editing ""))
                     (do
                       (log/info "EDIT" name)
                       (reset! is-editing name)))}
   (if (and @is-editing (= name @is-editing))
     [:span.icon.has-text-success.is-small [:i.far.fa-save]]
     [:span.icon.has-text-info.is-small [:i.far.fa-edit]])])


(defn- display-delete-control [name]
  ^{:key (str "delete-" name)}
  [:td {:on-click #(do)}
   [:span.icon.has-text-danger.is-small [:i.far.fa-trash-alt]]])


(defn- display-color [name [_ _ color]]
  ^{:key (str "color-" name)}
  [:td {:style (merge
                 (if color
                   {:background-color (or color :green)
                    :border-width     "1px"}
                   {:background-color :transparent
                    :border-width     "1px"})
                 {:text-align :center
                  :width      100})}
   [:span]])


(defn- target-table [& {:keys [data selection component-id container-id]}]
  (let [d          (h/resolve-value data)
        s          (h/resolve-value selection)
        is-editing (r/atom "")]

    (fn []
      (let [under-consideration (->> @s (map :name) set)]
        ;(log/info "target-table (d)" @d)
        [:div.table-container {:style {:width       "100%"
                                       :height      "100%"
                                       :overflow-y  :auto
                                       :white-space :nowrap
                                       :border      "1px outset gray"}}
         [:table.table
          [:thead {:style {:position :sticky :top 0 :background :lightgray}}
           [:tr [:th "Include?"] [:th "Symbol"] [:th "AoI"] [:th ""] [:th ""]]]
          [:tbody
           (doall
             (for [{:keys [name cells color]} @d]
               (doall
                 ;(log/info "target-table (for)" @d "//" name color)

                 ^{:key name}
                 [:tr
                  [display-checkbox name name under-consideration]

                  [display-symbol name color]

                  ^{:key (str "target-" name)} [:td name]

                  [display-edit-control name is-editing]

                  [display-delete-control name]])))]]]))))


(defn- satellite-table [& {:keys [data selection component-id container-id]}]
  ;(log/info "satellites-table" data "//" selection)

  (let [d          (h/resolve-value data)
        s          (h/resolve-value selection)
        is-editing (r/atom "")]

    ;(log/info "satellites-table (s)" @s "//" (:data @d))

    (fn []
      (let [under-consideration @s]
        [:div.table-container {:style {:width       "100%"
                                       :height      "100%"
                                       :overflow-y  :auto
                                       :white-space :nowrap
                                       :border      "1px outset gray"}}
         [:table.table
          [:thead {:style {:position :sticky :top 0 :background :lightgray}}
           [:tr [:th "Include?"] [:th "Color"] [:th "Platform"]]]
          [:tbody
           (doall
             (for [{:keys [platform_id sensor_id color] :as platform} (:data @d)]
               (doall
                 ;(log/info "satellites-table (platform)" platform
                 ;  "//" platform_id "//" sensor_id "//" color)

                 ^{:key name}
                 [:tr
                  [display-checkbox sensor_id (str platform_id "-" sensor_id) under-consideration]

                  [display-color (str platform_id "-" sensor_id) color]

                  ^{:key (str "satellite-" platform_id "-" sensor_id)}
                  [:td (str platform_id "  " sensor_id)]])))]]]))))


;; endregion


(def ui-definition {:title        "Coverage Plan"
                    :component-id :coverage-plan
                    :components   {; ui components
                                   :ui/targets                {:type :ui/component :name target-table}
                                   :ui/satellites             {:type :ui/component :name satellite-table}
                                   :ui/globe                  {:type :ui/component :name :ww/globe}
                                   :ui/time-slider            {:type :ui/component :name :rc/slider}
                                   :ui/current-time           {:type :ui/component :name :rc/label-md}

                                   ; remote data sources
                                   :topic/target-data         {:type :source/remote :name :source/targets}
                                   :topic/satellite-data      {:type :source/remote :name :source/satellites}
                                   :topic/coverage-data       {:type :source/remote :name :source/coverages}

                                   ; composite-local data sources
                                   :topic/selected-targets    {:type    :source/local :name :selected-targets
                                                               :default dummy-targets}

                                   :topic/colored-targets     {:type :source/local :name :colored-targets}

                                   :topic/selected-satellites {:type    :source/local :name :selected-satellites
                                                               :default dummy-satellites}
                                   :topic/colored-satellites  {:type :source/local :name :colored-satellites}

                                   :topic/current-time        {:type :source/local :name :current-time :default 0}
                                   :topic/shapes              {:type :source/local :name :shapes}
                                   :topic/time-range          {:type :source/local :name :time-range}
                                   :topic/current-slider      {:type :source/local :name :current-slider :default 0}

                                   ; transformation functions
                                   :fn/coverage               {:type  :source/fn :name fn-coverage
                                                               :ports {:targets   :port/sink :satellites :port/sink
                                                                       :coverages :port/sink :current-time :port/sink
                                                                       :shapes    :port/source}}
                                   :fn/range                  {:type  :source/fn :name fn-range
                                                               :ports {:data :port/sink :range :port/source}}
                                   :fn/current-time           {:type  :source/fn :name fn-current-time
                                                               :ports {:value :port/sink :current-time :port/source}}
                                   :fn/color-targets          {:type  :source/fn :name fn-color-targets
                                                               :ports {:data :port/sink :colored :port/source}}
                                   :fn/color-satellites       {:type  :source/fn :name fn-color-satellites
                                                               :ports {:data :port/sink :colored :port/source}}}

                    :links        {:ui/targets                {;:data      {:topic/target-data :data}
                                                               :selection {:topic/selected-targets :data}}
                                   :ui/satellites             {;:data      {:topic/satellite-data :data}
                                                               :selection {:topic/selected-satellites :data}}
                                   :ui/time-slider            {:value {:topic/current-slider :data}}

                                   ; transformation functions publish to what?
                                   :fn/coverage               {:shapes {:topic/shapes :data}}
                                   :fn/range                  {:range {:topic/time-range :data}}
                                   :fn/current-time           {:current-time {:topic/current-time :data}}
                                   :fn/color-targets          {:colored {:topic/colored-targets :data}}
                                   :fn/color-satellites       {:colored {:topic/colored-satellites :data}}

                                   ; topics are inputs into what?
                                   :topic/target-data         {:data {:fn/color-targets :data}}
                                   :topic/colored-targets     {:data {:ui/targets :data}}
                                   :topic/selected-targets    {:data {:ui/targets  :selection
                                                                      :fn/coverage :targets}}

                                   :topic/satellite-data      {:data {:fn/add-satellite-color :data
                                                                      :ui/satellites :data}}
                                   ;:topic/colored-satellites  {:data {:ui/satellites :data}}
                                   :topic/selected-satellites {:data {:ui/satellites :selection
                                                                      :fn/coverage   :satellites}}

                                   :topic/coverage-data       {:data {:fn/coverage :coverages
                                                                      :fn/range    :data}}
                                   :topic/shapes              {:data {:ui/globe :shapes}}
                                   :topic/current-time        {:data {:ui/current-time :value
                                                                      :ui/globe        :current-time}}
                                   :topic/current-slider      {:data {:fn/current-time :value
                                                                      :ui/time-slider  :value
                                                                      :fn/coverage     :current-time}}
                                   :topic/time-range          {:data {:ui/time-slider :range}}}

                    :grid-layout  [{:i :ui/targets :x 0 :y 0 :w 9 :h 7 :static true}
                                   {:i :ui/satellites :x 0 :y 7 :w 9 :h 8 :static true}
                                   {:i :ui/time-slider :x 2 :y 15 :w 6 :h 2 :static true}
                                   {:i :ui/globe :x 9 :y 0 :w 11 :h 15 :static true}
                                   {:i :ui/current-time :x 9 :y 15 :w 8 :h 2 :static true}]})



(comment
  (re-frame/dispatch [:bh.rccst.events/login "test-user" "test-pwd"])

  (re-frame/subscribe [:bh.rccst.subs/source :string])

  ())


; work out making actual shapes for the coverage data we get from the server
(comment
  (do
    (def coverages (get-in @re-frame.db/app-db [:sources :source/coverages :data]))
    (def current-time 0)

    (def time-coverage (filter #(= (:time %) current-time) coverages)))


  (ui-utils/subscribe-local
    :ui-grid-ratom-demo.coverage-plan
    [:blackboard :topic.shapes])


  ())





;; components have "ports" which define their inputs and outputs:
;;
;;      you SUBSCRIBE with a :port/sink, ie, data come IN   (re-frame/subscribe ...)
;;
;;      you PUBLISH to a :port/source, ie, data goes OUT    (re-frame/dispatch ...)
;;
;;      you do BOTH with :port/source-sink (both)           should we even have this, or should we spell out both directions?
;;
;; the question about :port/source-sink arises because building the layout (the call for the UI itself) doesn't actually
;; need to make a distinction (in fact the code is a bit cleaner if we don't) and we have the callee sort it out (since it
;; needs to implement the correct usage anyway). The flow-diagram, on the other hand, is easier if we DO make the
;; distinction, so we can quickly build all the Nodes and Handles used for the diagram...




