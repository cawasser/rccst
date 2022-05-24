(ns bh.rccst.ui-component.molecule.composite.coverage-plan
  "provide a composed UI for a \"Coverage Plan\" which shows targets and satellite coverage areas
  on a 3D globe"
  (:require [bh.rccst.ui-component.molecule.composite.coverage-plan.support :as s]
            [bh.rccst.ui-component.utils :as ui-utils]
            [bh.rccst.ui-component.utils.helpers :as h]
            [cljs-time.coerce :as coerce]
            [cljs-time.core :as t]
            [re-com.core :as rc]
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


(def dummy-satellites [{:name            "goes-east",
                        :start           [9 6],
                        :path            "geo",
                        :platform_id     "goes-east",
                        :type            "hidef-image",
                        :sensor_steering [[-5 5] [-5 5]],
                        :sensor_size     [1 1],
                        :sensor_id       "abi-meso-2"
                        :color           [:blue "rgba(0, 0, 255, .3)" [0.0 0. 1.0 0.1]]}
                       {:name            "goes-east",
                        :start           [9 6],
                        :path            "geo",
                        :platform_id     "goes-east",
                        :type            "hidef-image",
                        :sensor_steering [[-5 5] [-5 5]],
                        :sensor_size     [1 1],
                        :sensor_id       "abi-meso-10"
                        :color           [:orange "rgba(255, 165, 0, .3)" [1.0 0.65 0.0 0.3]]}
                       {:name            "goes-west",
                        :start           [9 2],
                        :path            "geo",
                        :platform_id     "goes-west",
                        :type            "hidef-image",
                        :sensor_steering [[-5 5] [-5 5]],
                        :sensor_size     [1 1],
                        :sensor_id       "abi-meso-4"
                        :color           [:cornflowerblue "rgba(100, 149, 237, .3)" [0.4 0.58 0.93 0.3]]}
                       {:name            "goes-west",
                        :start           [9 2],
                        :path            "geo",
                        :platform_id     "goes-west",
                        :type            "hidef-image",
                        :sensor_steering [[-5 5] [-5 5]],
                        :sensor_size     [1 1],
                        :sensor_id       "abi-meso-11"
                        :color           [:darkcyan "rgba(0, 139, 139, .3)" [0.0 0.55 0.55 0.3]]}
                       {:name            "noaa-xx",
                        :start           [4 3],
                        :path            "horz",
                        :platform_id     "noaa-xx",
                        :type            "v/ir",
                        :sensor_steering [[0 0] [0 0]],
                        :sensor_size     [10 2],
                        :sensor_id       "viirs-5"
                        :color           [:goldenrod "rgba(218, 165, 32, .3)" [0.84 0.65 0.13 0.3]]}
                       {:name            "metop-yy",
                        :start           [4 1],
                        :path            "horz",
                        :platform_id     "metop-yy",
                        :type            "v/ir",
                        :sensor_steering [[0 0] [0 0]],
                        :sensor_size     [10 2],
                        :sensor_id       "avhhr-6"
                        :color           [:khaki "rgba(240, 230, 140, .3)" [0.94 0.90 0.55 0.3]]}])


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
      ;  "// (targets)" t
      ;  "// (satellites)" s)
      ;  ;"// (cooked)" (s/cook-coverages c ct)
      ;  "// (filter)" (filter #(contains? s (get-in % [:coverage :sensor]))
      ;                  (s/cook-coverages c ct)))

      (let [s-under-c          (->> s (map #(get-in % [:sensor_id])) set)
            ;_                              (log/info "fn-coverage (s-u-c)" s-under-c)
            filtered-coverages (filter #(contains?
                                          s-under-c
                                          (get-in % [:coverage :sensor]))
                                 (s/cook-coverages s c ct))
            ;_                  (log/info "fn-coverage (filter)" filtered-coverages)
            cvg                (if (seq c)
                                 (map s/make-coverage-shape filtered-coverages)
                                 [])
            trg                (if (seq t)
                                 (map s/make-target-shape (s/cook-targets t ct))
                                 [])
            ret                (concat cvg trg)]

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
  (let [next-target-color (atom -1)
        last-target-data  (atom [])]
    (re-frame/reg-sub
      (first colored)
      :<- data
      (fn [d _]
        ;(log/info "fn-color-targets (data)" d "//" (:data d))
        (let [cnt          (count s/sensor-color-pallet)
              assigned     (map (juxt :name :color) @last-target-data)
              assigned-set (->> assigned (map first) set)
              ;_            (log/info "fn-color-targets (atom)" @last-target-data "//" assigned "//" assigned-set)
              ret          (doall
                             (map (fn [t]
                                    (if (contains? assigned-set (:name t))
                                      (assoc t :color (->> @last-target-data
                                                        (filter #(= (:name t) (:name %)))
                                                        first
                                                        :color))
                                      (assoc t :color (nth s/sensor-color-pallet
                                                        (mod (swap! next-target-color inc) cnt)))))
                               (:data d)))]

          ;(log/info "fn-color-targets (ret)" ret "//" @next-target-color)

          (reset! last-target-data ret)
          @last-target-data)))))


(defn fn-color-satellites [{:keys [data colored]}]
  ;(log/info "fn-color-satellites" data "//" colored)
  (let [next-sat-color (atom -1)
        last-sat-data  (atom [])]
    (re-frame/reg-sub
      (first colored)
      :<- data
      (fn [d _]
        ;(log/info "fn-color-satellites (data)" d "//" (:data d))
        (let [cnt          (count s/sensor-color-pallet)
              assigned     (map (juxt :sensor_id :color) @last-sat-data)
              assigned-set (->> assigned (map first) set)
              ;_            (log/info "fn-color-satellites (atom)" @last-sat-data "//" assigned "//" assigned-set)
              ret          (doall
                             (map (fn [t]
                                    (if (contains? assigned-set (:sensor_id t))
                                      (assoc t :color (->> @last-sat-data
                                                        (filter #(= (:sensor_id t) (:sensor_id %)))
                                                        first
                                                        :color))
                                      (assoc t :color (nth s/sensor-color-pallet
                                                        (mod (swap! next-sat-color inc) cnt)))))
                               (:data d)))]
          ;(log/info "fn-color-satellites (ret)" ret "//" @next-sat-color)

          (reset! last-sat-data ret)
          @last-sat-data)))))


;; endregion


;; region ; custom tables for display

(defn- display-checkbox [id name under-consideration toggle-fn]
  ^{:key (str "check-" id)}
  [:td.is-narrow
   {:style    {:text-align :center}
    :on-click (rc/handler-fn
                (toggle-fn))}

   (if (contains? under-consideration id)
     [:span.icon.has-text-success.is-small [:i.fas.fa-check]]
     [:span.icon.has-text-success.is-small [:i.far.fa-square]])])


(defn- display-symbol [name [color _ _]]
  ; TODO: add HexColorPicker (also, ad an actual "atom" for the color pickers...)

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


(defn- display-color [name [color _ _]]
  ; TODO: add RgbaColorPicker (also, ad an actual "atom" for the color pickers...)

  ;(log/info "display-color" name "//" color)
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


(defn- toggle-target [targets resolved-selection selection id]
  (let [s-ids (->> resolved-selection (map :name) set)]
    (if (contains? s-ids id)
      ; remove
      (h/handle-change-path selection []
        (remove #(= id (:name %)) resolved-selection))

      ; add
      (h/handle-change-path selection []
        (concat resolved-selection (filter #(= id (:name %)) targets))))))


(defn- toggle-satellite [satellites resolved-selection selection id]
  (let [s-ids (->> resolved-selection (map :sensor_id) set)]
    (if (contains? s-ids id)
      ; remove
      (h/handle-change-path selection []
        (remove #(= id (:sensor_id %)) resolved-selection))

      ; add
      (h/handle-change-path selection []
        (concat resolved-selection (filter #(= id (:sensor_id %)) satellites))))))


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
                 ;(log/info "target-table (for)" @d
                 ; "//" name color)

                 ^{:key name}
                 [:tr
                  [display-checkbox name name under-consideration #(toggle-target @d @s selection name)]

                  [display-symbol name color]

                  ^{:key (str "target-" name)} [:td name]

                  [display-edit-control name is-editing]

                  [display-delete-control name]])))]]]))))


(defn- satellite-table [& {:keys [data selection component-id container-id]}]
  ;(log/info "satellites-table" data "//" selection)

  (let [d          (h/resolve-value data)
        s          (h/resolve-value selection)
        is-editing (r/atom "")]

    ;(log/info "satellites-table (s)" @d "//" @s)

    (fn []
      (let [under-consideration (->> @s (map :sensor_id) set)]
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
             (for [{:keys [platform_id sensor_id color] :as platform} @d]
               (doall
                 ^{:key sensor_id}
                 [:tr
                  [display-checkbox sensor_id
                   (str platform_id "-" sensor_id)
                   under-consideration
                   #(toggle-satellite @d @s selection sensor_id)]

                  [display-color (str platform_id "-" sensor_id) color]

                  ^{:key (str "satellite-" platform_id "-" sensor_id)}
                  [:td (str platform_id "  " sensor_id)]])))]]]))))


;; endregion


(def ui-definition {:title        "Coverage Plan"
                    :component-id :coverage-plan
                    :components   {; ui components
                                   ; TODO: add a :label element for use in the UI
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
                                   :topic/selected-targets    {:type :source/local :name :selected-targets}
                                   ;:default dummy-targets}

                                   :topic/colored-targets     {:type :source/local :name :colored-targets}

                                   :topic/selected-satellites {:type    :source/local :name :selected-satellites}
                                                               ;:default dummy-satellites}
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

                                   :topic/satellite-data      {:data {:fn/color-satellites :data}}
                                   :topic/colored-satellites  {:data {:ui/satellites :data}}
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



;; region ; Rich comments
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


; can we cache the results so we only add :color to "new" elements?
(comment
  (do
    ;(def colored [:coverage-plan-demo-ww.grid-widget.blackboard.topic.colored-targets]) ; actual
    ;(def colored [:coverage-plan-demo-ww.grid-widget :blackboard :topic.colored-targets]) ; what we'd prefer

    (def last-data (atom []))
    (def last-target-data (atom dummy-targets))
    (def assigned (map (juxt :name :color) @last-data))
    (def assigned-set (->> assigned (map first) set))
    (def candidate {:name  "alpha-hd",
                    :cells #{[7 7 "hidef-image" 0] [7 6 "hidef-image" 1]
                             [7 5 "hidef-image" 3] [7 6 "hidef-image" 2]}}))


  (if (contains? assigned-set (:name candidate))
    candidate
    (assoc candidate :color [:dummy :dummy :dummy]))


  (def last-target-data (atom []))

  (->> @last-target-data
    (filter #(= (:name candidate) (:name %)))
    first
    :color)


  @(re-frame/subscribe [:bh.rccst.subs/source :source/targets])

  (re-frame/dispatch [:bh.rccst.events/data-update
                      {:id    :source/targets
                       :value {:title    "Targets",
                               :c-o-c    [{:step      :generated,
                                           :by        "bh.rccst.data-source.targets",
                                           :version   "0.6.0",
                                           :at        "Mon May 23 14:21:10 EDT 2022",
                                           :signature "73f7a470-ddc9-44d9-84fa-cf1ce1acc8f9"}],
                               :metadata {:title "Targets", :type :tabular, :id :target, :fields {:target :string, :cells :string}},
                               :data     [{:name  "alpha-hd",
                                           :cells #{[7 7 "hidef-image" 0] [7 6 "hidef-image" 1] [7 5 "hidef-image" 3] [7 6 "hidef-image" 2]}}
                                          {:name "bravo-img", :cells #{[7 2 "image" 0] [7 1 "image" 1]}}
                                          {:name  "fire-hd",
                                           :cells #{[5 3 "hidef-image" 2]
                                                    [4 3 "hidef-image" 3]
                                                    [4 3 "hidef-image" 2]
                                                    [5 3 "hidef-image" 0]
                                                    [5 3 "hidef-image" 3]}}
                                          {:name "fire-ir", :cells #{[5 4 "v/ir" 2] [5 4 "v/ir" 1] [5 3 "v/ir" 1] [5 4 "v/ir" 0] [5 4 "v/ir" 3]}}
                                          {:name  "severe-hd",
                                           :cells #{[5 7 "hidef-image" 3]
                                                    [5 6 "hidef-image" 0]
                                                    [6 6 "hidef-image" 2]
                                                    [6 5 "hidef-image" 1]
                                                    [5 7 "hidef-image" 1]}}
                                          {:name  "new-target",
                                           :cells #{[0 0 "hidef-image" 0]}}]}}])



  (get-in @re-frame.db/app-db '(:containers))
  (get-in @re-frame.db/app-db '(:containers :coverage-plan-demo-ww.grid-widget))
  (get-in @re-frame.db/app-db '(:containers :coverage-plan-demo-ww.grid-widget
                                 :blackboard))
  (get-in @re-frame.db/app-db '(:containers :coverage-plan-demo-ww.grid-widget
                                 :blackboard :topic.colored-targets))


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



;; endregion

