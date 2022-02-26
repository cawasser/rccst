(ns bh.rccst.ui-component.molecule.composite.multi-chart
  (:require [bh.rccst.ui-component.atom.chart.bar-chart :as bar-chart]
            [bh.rccst.ui-component.atom.chart.line-chart :as line-chart]
            [bh.rccst.ui-component.atom.chart.utils :as utils]
            [bh.rccst.ui-component.molecule.composite :as c]
            [bh.rccst.ui-component.registry :as registry]
            [bh.rccst.ui-component.utils :as ui-utils]
            [re-com.core :as rc]
            [re-frame.core :as re-frame]
            [reagent.core :as r]
            [taoensso.timbre :as log]

            [woolybear.ad.containers :as containers]))


(def sample-data line-chart/sample-data)

(declare config-panel)

(def composite-def
  "
  "
  {; the ui components (looked up in a registry), mapped to local names
   :components {:line-chart   :chart/line-chart
                :bar-chart    :chart/bar-chart
                :config-panel config-panel}

   ; all remote data sources (mapped to a local name)
   ; TODO: might replace this with some meta-data so the USER can select appropriate data source
   :sources    {:tabular-data :source/sequence-of-measurements
                :dag-data :source/dag-data}

   ; links - how the different components get their data and if they publish or
   ; subscribe to the composite
   :links      {:line-chart   {:data :tabular-data}
                :bar-chart    {:data :tabular-data}
                :config-panel {:data :tabular-data}}

   ; the physical layout of the components on the display
   :layout     [[:line-chart :config-panel :bar-chart]]})


; explore composite-def and the various registries
;
; i.e., we want to create something like this:
;
;    [[[line-chart/component data (str component-id "/line-chart") component-id]
;      [config-panel data component-id]
;      [bar-chart/component data (str component-id "/bar-chart") component-id]})))
;
(comment
  ; dummy login so subscription work at all
  (re-frame/dispatch [:bh.rccst.events/login "string" "string"])

  ; resolve the ui-components
  (def resolved-components
    (->> composite-def
      :components
      (map (fn [[name component]]
             {name [(or (get registry/ui-component-registry component)
                      component)]}))
      (into {})))

  ; get data from the server
  (def resolved-sources
    (->> composite-def
      :sources
      (map (fn [[name source]]
             (re-frame/dispatch [:bh.rccst.events/subscribe-to #{source}])
             {name (or (re-frame/subscribe [:bh.rccst.subs/source source])
                     (r/atom []))}))
      (into {})))


  ; build up the layout
  ;
  ; i.e., we want to create something like this:
  ;
  ;    [[[line-chart/component data (str component-id "/line-chart") component-id]
  ;      [config-panel data component-id]
  ;      [bar-chart/component data (str component-id "/bar-chart") component-id]})))
  ;
  ; let's start with :line-chart
  ;
  ; first we need to get the correct data source
  (def component :line-chart)
  (def sources (get-in composite-def [:links component]))

  (conj (get resolved-components component)
    (get resolved-sources source)
    (ui-utils/path->keyword "dummy" component)
    "dummy")


  (defn process-sources [sources]
    (->> sources
      (mapcat (fn [[name source]]
                (println name source)
                [name (get resolved-sources source)]))
      flatten
      (into [])))

  (get resolved-sources :tabular-data)

  (process-sources sources)
  (process-sources {:data :tabular-data})

  (conj [(get resolved-components component)]
    (process-sources sources)
    [:component-ui (ui-utils/path->keyword "dummy" component)]
    [:container-id "dummy"])

  (defn process-columns [id row]
    (->> row
      (map (fn [component]
             (let [sources (get-in composite-def [:links component :source])]
               (into []
                 (flatten
                   (conj (get resolved-components component)
                     (process-sources sources)
                     [:component-ui (ui-utils/path->keyword "dummy" component)]
                     [:container-id "dummy"]))))))
      (into [])))


  (defn process-rows [id layout]
    (->> layout
      (map (fn [row]
             (process-columns id row)))
      (into [])))


  (process-columns "dummy" [:line-chart :config-panel :bar-chart])
  (process-rows "dummy" (:layout composite-def))


  ; next, process them all
  (->> composite-def
    :layout
    (process-rows "dummy"))


  ())



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
   {:height "400px"}
   [rc/v-box :src (rc/at)
    :width "200px"
    :height "400px"
    :gap "2px"
    :children (apply merge
                [[utils/boolean-config component-id ":brush?" [:blackboard :brush]]]
                (make-config component-id data))]])


(defn local-config [data component-id]
  {:components [[[line-chart/component
                  :data data
                  :component-id (ui-utils/path->keyword component-id "line-chart")
                  :container-id component-id]
                 [config-panel data component-id]
                 [bar-chart/component
                  :data data
                  :component-id (ui-utils/path->keyword component-id "bar-chart")
                  :container-id component-id]]]
   :blackboard (merge {:brush false}
                 (->> (get-in @data [:metadata :fields])
                   (filter (fn [[k v]] (= :number v)))
                   keys
                   (map-indexed (fn [idx a]
                                  {a {:include true
                                      :stroke  (ui-utils/get-color idx)
                                      :fill    (ui-utils/get-color idx)
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
       :components (ui-utils/resolve-sub subscriptions [:components])])))


(defn component
  ([& {:keys [data component-id container-id]}]

   ;(log/info "multi-chart" @data)

   (let [id (r/atom nil)]

     (fn []
       (when (nil? @id)
         (reset! id component-id)
         (ui-utils/init-widget @id (config @id data))
         (ui-utils/dispatch-local @id [:container] container-id))

       ;(log/info "multi-chart" @id (config @id data))

       [component-panel data @id container-id]))))



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




