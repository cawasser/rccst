(ns bh.rccst.ui-component.molecule.composite.multi-chart
  (:require [bh.rccst.ui-component.atom.chart.bar-chart :as bar-chart]
            [bh.rccst.ui-component.atom.chart.line-chart :as line-chart]
            [bh.rccst.ui-component.atom.chart.utils :as utils]
            [bh.rccst.ui-component.molecule.composite :as c]
            [bh.rccst.ui-component.utils :as ui-utils]
            [bh.rccst.ui-component.utils.color :as color]
            [bh.rccst.ui-component.utils.locals :as l]
            [bh.rccst.ui-component.utils.helpers :as h]
            [re-com.core :as rc]
            [re-frame.core :as re-frame]
            [reagent.core :as r]
            [taoensso.timbre :as log]
            [woolybear.ad.containers :as containers]))


(log/info "bh.rccst.ui-component.molecule.composite.multi-chart")


(def sample-data line-chart/sample-data)

;; region
(declare config-panel)


(defn- compute-data-config [data]
  ;(log/info "compute-data-config" data)

  (let [ret (merge {:brush true}
              (->> (get-in data [:metadata :fields])
                (filter (fn [[k v]] (= :number v)))
                keys
                (map-indexed (fn [idx a]
                               {a {:include true
                                   :stroke  (color/get-color idx)
                                   :fill    (color/get-color idx)
                                   :stackId ""}}))
                (into {})))]
    ;(log/info "compute-data-config (ret)" ret)
    ret))


(defn fn-make-config [{:keys [data config-data container-id] :as params}]
  ; needs to implement something along the lines of:
  ;
  ; (l/update-local-values component-id (local-config d))
  ;
  ; and maybe...
  ;
  ; (ui-utils/build-subs component-id l-c)

  ;(log/info "fn-make-config" params)

  (re-frame/reg-sub
    (first config-data)
    :<- data
    (fn [d _]
      (doall
        (l/update-local-path-values container-id [:blackboard :topic.config] (compute-data-config d))))))


(defn- data-config [config-data-path item position]
  [rc/v-box :src (rc/at)
   :gap "5px"
   :children [[rc/line :size "2px"]
              [rc/h-box :src (rc/at)
               :gap "5px"
               :children [[utils/boolean-config config-data-path item [item :include]]
                          [utils/color-config config-data-path ":stroke" [item :stroke] position]
                          [utils/color-config config-data-path ":fill" [item :fill] position]]]
              [utils/text-config config-data-path ":stackId" [item :stackId]]]])


(defn- config-panel [& {:keys [config-data component-id container-id] :as params}]
  (let [c (h/resolve-value config-data)
        data-only (dissoc @c :brush)
        item-controls (map-indexed (fn [idx [item _]]
                                     (data-config config-data item (if (= 0 idx)
                                                                     :below-right
                                                                     :right-center)))
                        data-only)]

    ;(log/info "config-panel" params
    ;  "//" config-data "//" @c)

    [rc/scroller
     :v-scroll :auto
     :height   "90%"
     :child [rc/v-box :src (rc/at)
             :width "100%"
             :height "100%"
             :gap "2px"
             :children (apply merge
                         [[utils/boolean-config config-data ":brush?" [:brush]]]
                         item-controls)]]))

;; endregion

(def ui-definition
  {:components  {:ui/line        {:type :ui/component :name :rechart/bar-2}
                 :ui/bar         {:type :ui/component :name :rechart/bar-2}
                 :ui/area         {:type :ui/component :name :rechart/bar-2}
                 :ui/config      {:type :ui/component :name config-panel}
                 :topic/data     {:type :source/local :name :topic/data :default @sample-data}
                 :topic/config   {:type :source/local :name :topic/config :default {}}
                 :fn/make-config {:type  :source/fn :name fn-make-config
                                  :ports {:data :port/sink :config-data :port/source-sink}}}

   :links       {:ui/config      {:config-data {:topic/config :data}}
                 :topic/data     {:data {:ui/line        :data
                                         :ui/bar         :data
                                         :ui/area         :data
                                         :fn/make-config :data}}
                 :topic/config   {:data {:ui/line   :config-data
                                         :ui/bar    :config-data
                                         :ui/area    :config-data
                                         :ui/config :config-data}}
                 :fn/make-config {:config-data {:topic/config :data}}}

   :grid-layout [{:i :ui/config :x 0 :y 0 :w 12 :h 5 :static true}
                 {:i :ui/line :x 0 :y 5 :w 4 :h 11 :static true}
                 {:i :ui/bar :x 4 :y 5 :w 4 :h 11 :static true}
                 {:i :ui/area :x 8 :y 5 :w 4 :h 11 :static true}]})


(def source-code '(let [def {:components  {:ui/line        {:type :ui/component :name :rechart/bar-2}
                                           :ui/bar         {:type :ui/component :name :rechart/bar-2}
                                           :ui/area         {:type :ui/component :name :rechart/bar-2}
                                           :ui/config      {:type :ui/component :name config-panel}
                                           :topic/data     {:type :source/local :name :topic/data :default @sample-data}
                                           :topic/config   {:type :source/local :name :topic/config :default {}}
                                           :fn/make-config {:type  :source/fn :name fn-make-config
                                                            :ports {:data :port/sink :config-data :port/source-sink}}}

                             :links       {:ui/config      {:config-data {:topic/config :data}}
                                           :topic/data     {:data {:ui/line        :data
                                                                   :ui/bar         :data
                                                                   :ui/area         :data
                                                                   :fn/make-config :data}}
                                           :topic/config   {:data {:ui/line   :config-data
                                                                   :ui/bar    :config-data
                                                                   :ui/area    :config-data
                                                                   :ui/config :config-data}}
                                           :fn/make-config {:config-data {:topic/config :data}}}

                             :grid-layout [{:i :ui/config :x 0 :y 0 :w 12 :h 5 :static true}
                                           {:i :ui/line :x 0 :y 5 :w 4 :h 11 :static true}
                                           {:i :ui/bar :x 4 :y 5 :w 4 :h 11 :static true}
                                           {:i :ui/area :x 8 :y 5 :w 4 :h 11 :static true}]}]
                    [grid-widget/component
                     :data def
                     :component-id (h/path->keyword container-id "widget")]))




; testing for setting up subs/events to support widgets
(comment
  @(re-frame/subscribe [:multi-chart-widget.widget.blackboard])
  @(re-frame/subscribe [:multi-chart-widget.widget.blackboard.topic.data])
  @(re-frame/subscribe [:multi-chart-widget.widget.blackboard.topic.config])


  @(re-frame/subscribe [:multi-chart-widget.widget.blackboard.topic.config.uv])
  @(re-frame/subscribe [:multi-chart-widget.widget.blackboard.topic.config.uv.include])
  @(re-frame/subscribe [:multi-chart-widget.widget.blackboard.topic.config.uv.fill])
  @(re-frame/subscribe [:multi-chart-widget.widget.blackboard.topic.config.uv.stroke])
  @(re-frame/subscribe [:multi-chart-widget.widget.blackboard.topic.config.uv.stackId])


  (re-frame/dispatch [:multi-chart-widget.widget.blackboard.topic.config.uv.include false])
  (re-frame/dispatch [:multi-chart-widget.widget.blackboard.topic.config.uv.include true])


  (get-in @re-frame.db/app-db [:widgets :multi-chart-widget.widget])
  (get-in @re-frame.db/app-db [:widgets :multi-chart-widget.widget :blackboard])
  (get-in @re-frame.db/app-db [:widgets :multi-chart-widget.widget
                               :blackboard (ui-utils/path->keyword :topic/config)])
  (get-in @re-frame.db/app-db [:widgets :multi-chart-widget.widget.blackboard])


  (reduce conj [:widget :dummy] [:blackboard :topic.config])

  (reduce conj [:widgets] [:dummy])
  (reduce conj [:widgets] [[:blackboard :topic.config]])

  (def container :multi-chart-widget.widget)
  (def container [:multi-chart-widget.widget :blackboard :topic.config])
  (def values "")
  (let [data-path (cond
                    (coll? container) (reduce conj [:widgets] container)
                    :else [:widget container])]
    (get-in @re-frame/app-db data-path values))


  ())


