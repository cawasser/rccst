(ns bh.rccst.ui-component.registry
  (:require [bh.rccst.ui-component.atom.chart.registry :as charts]
            [bh.rccst.ui-component.utils :as ui-utils]
            [re-frame.core :as re-frame]
            [reagent.core :as r]))


(def ui-component-registry (merge {}
                             charts/registry))


; resolve the ui-components
(defn- resolved-components [composite-def]
  (->> composite-def
    :components
    (map (fn [[name meta-data]]
           {name [(or (->> (get ui-component-registry (get-in meta-data [:type]))
                        :component)
                    (:type meta-data))]}))
    (into {})))


; TODO!!!!
; get data from the topics
; replaced by (defmethod component->ui :source/remote) in ...coverage-plan
(defn- resolved-sources [composite-def]
  (->> composite-def
    :sources
    keys
    (map (fn [name]
           (re-frame/dispatch [:bh.rccst.events/subscribe-to #{name}])
           {name (or (re-frame/subscribe [:bh.rccst.subs/source name])
                   (r/atom []))}))
    (into {})))


(defn- process-sources [resolved-sources sources]
  (->> sources
    (mapcat (fn [[name source]]
              ;(log/info name source)
              [name (get resolved-sources source)]))
    flatten
    (into [])))


; replaced by (defmethod component->ui :ui/component) in ...coverage-plan
(defn- process-component [composite-def resolved-components resolved-sources id component]
  (let [sources (get-in composite-def [:links component])]
    (flatten
      (conj (get resolved-components component)
        (process-sources resolved-sources sources)
        [:component-ui (ui-utils/path->keyword id component)]
        [:container-id id]))))


(defn- process-columns [composite-def resolved-components
                        resolved-sources id row]
  (->> row
    (map (fn [component]
           (process-component composite-def resolved-components
             resolved-sources id component)))
    (into [])))


(defn process-rows [composite-def id layout]
  (let [resolved-components (resolved-components composite-def)
        resolved-sources    (resolved-sources composite-def)]
    (->> layout
      (map (fn [row]
             (process-columns composite-def resolved-components
               resolved-sources id row)))
      (into []))))


(defn process-config [config component-id]
  (->> config
    :layout
    (process-rows config component-id)))


(comment
  (do
    (def composite-def bh.rccst.ui-component.molecule.composite.multi-chart/composite-def)
    (def meta-data (get-in composite-def [:components :line-chart])))

  (:components composite-def)
  (get ui-component-registry (get-in meta-data [:type]))

  (->> (get ui-component-registry (get-in meta-data [:type]))
    :sources
    keys)

  (->> composite-def
    :sources
    keys
    (map (fn [name]
           (re-frame/dispatch [:bh.rccst.events/subscribe-to #{name}])
           {name (or (re-frame/subscribe [:bh.rccst.subs/source name])
                   (r/atom []))}))
    (into {}))

  ())


; explore composite-def and the various registries
;
; i.e., we want to create something like this:
;
;    [[[line-chart/component
;       :data data
;       :component-id (path->keyword id "line-chart")
;       :container-id id]
;      [config-panel
;       :data data
;       :component-id (path->keyword id "config")]
;      [bar-chart/component
;       :data data
;       :component-id (path->keyword id "bar-chart")
;       :container-id id]]]
;
(comment
  (def composite-def bh.rccst.ui-component.molecule.composite.multi-chart/composite-def)

  ; dummy login so subscription work at all
  (re-frame/dispatch [:bh.rccst.events/login "test-user" "test-pwd"])


  ; build up the layout
  ;
  ; i.e., we want to create something like this:
  ;
  ;    [[[line-chart/component
  ;       :data data
  ;       :component-id (path->keyword id "line-chart")
  ;       :container-id id]
  ;      [config-panel
  ;       :data data
  ;       :component-id (path->keyword id "config")]
  ;      [bar-chart/component
  ;       :data data
  ;       :component-id (path->keyword id "bar-chart")
  ;       :container-id id]]]
  ;
  ; let's start with :line-chart
  ;
  ; first we need to get the correct data source
  (def component :line-chart)
  (def sources (get-in composite-def [:links component]))

  (get-in composite-def [:links component])

  (get (resolved-sources composite-def) :tabular-data)

  (process-sources composite-def sources)
  (process-sources composite-def {:data :tabular-data})

  (flatten (conj [(get (resolved-components composite-def) component)]
             (process-sources composite-def sources)
             [:component-ui (ui-utils/path->keyword "dummy" component)]
             [:container-id "dummy"]))

  (process-component composite-def "dummy" :line-chart)
  (process-columns composite-def "dummy" [:line-chart :config-panel :bar-chart])
  (process-rows composite-def "dummy" (:layout composite-def))


  ; next, process them all
  (->> composite-def
    :layout
    (process-rows composite-def "dummy"))

  (process-config composite-def "dummy")

  ())
