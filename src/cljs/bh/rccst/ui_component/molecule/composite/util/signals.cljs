(ns bh.rccst.ui-component.molecule.composite.util.signals
  (:require [bh.rccst.ui-component.utils :as ui-utils]
            [bh.rccst.ui-component.utils.locals :as ul]
            [day8.re-frame.tracing :refer-macros [fn-traced]]
            [re-com.core :as rc]
            [re-frame.core :as re-frame]
            [taoensso.timbre :as log]
            ["dagre" :as dagre]
            ["graphlib" :as graphlib]
            ["react-flow-renderer" :refer (ReactFlowProvider Controls Handle Background) :default ReactFlow]))



(defn- make-params [configuration node direction container-id]
  (->> configuration
    :denorm
    node
    direction
    (map (fn [[target ports]]
           (let [[source-port target-port] ports
                 target-type (-> configuration :components target :type)
                 remote      (-> configuration :components target :name)]
             ;(log/info "make-params" target target-type remote)
             (if (= direction :outputs)
               {source-port (if (= :source/local target-type)
                              [(ui-utils/path->keyword container-id :blackboard target)]
                              [:bh.rccst.subs/source remote])}
               {target-port (if (= :source/local target-type)
                              [(ui-utils/path->keyword container-id :blackboard target)]
                              [:bh.rccst.subs/source remote])}))))
    (into {})))


(defmulti component->ui (fn [{:keys [type]}]
                          type))


(defmethod component->ui :ui/component [{:keys [node registry configuration container-id]}]
  ;(log/info "component->ui :ui/component" node)

  (let [ui-type      (->> configuration :components node :name)
        ui-component (->> registry ui-type :component)]
    {node
     (reduce into [ui-component]
       (seq
         (merge
           (make-params configuration node :inputs container-id)
           (make-params configuration node :outputs container-id))))}))


(defmethod component->ui :source/local [{:keys [node meta-data container-id]}]
  ;(log/info "component->ui :source/local" node meta-data)

  ; 1. add the key to the blackboard, uses the :default property of the meta-data
  ;
  ;  only IF one exists, otherwise we assume it will be serviced by a :source/fn somewhere
  ;
  (when (:default meta-data)
    (ul/dispatch-local container-id [:blackboard node] (:default meta-data)))

  ; 2. create the subscription against the new :blackboard key
  (ul/create-widget-local-sub container-id [:blackboard node] (:default meta-data))

  ; 3. create the event against the new :blackboard key
  (ul/create-widget-local-event container-id [:blackboard node])

  ; 4. return the signal vector for the new subscription
  [(ui-utils/path->keyword container-id [:blackboard node])])


(defmethod component->ui :source/remote [{:keys [node meta-data]}]
  ; get the meta-data for the node and use the "actual name" as the thing to subscribe to
  (let [remote (:name meta-data)]

    ;(log/info "component->ui :source/remote" node "//" remote)

    ; 1. subscribe to the server (if needed)
    (re-frame/dispatch-sync [:bh.rccst.events/subscribe-to #{remote}])

    ; 2. return the signal vector to the new data-source key
    [:bh.rccst.subs/source remote]))


(defmethod component->ui :source/fn [{:keys [node configuration container-id]}]
  (let [actual-fn (->> configuration :components node :name)
        params    (merge
                    (make-params configuration node :inputs container-id)
                    (make-params configuration node :outputs container-id))]

    ;(log/info "component->ui :source/fn" node "//" params)

    (actual-fn params)))


(defn process-components [configuration node-type registry container-id]

  ;(log/info "process-components" container-id node-type
  ;  "//" (->> configuration
  ;         :components
  ;         (filter (fn [[_ meta-data]]
  ;                   (= node-type (:type meta-data))))))

  (doall
    (->> configuration
      :components
      (filter (fn [[_ meta-data]]
                (= node-type (:type meta-data))))
      (map (fn [[node meta-data]]
             ;(log/info "process-components (nodes)" node "//" meta-data "//" (:type meta-data))
             (component->ui {:node          node
                             :type          (:type meta-data)
                             :meta-data     meta-data
                             :configuration configuration
                             :registry      registry
                             :component-id  (ui-utils/path->keyword container-id node)
                             :container-id  container-id}))))))


(defn parse-token [lookup token]
  (condp = token
    :v-box [rc/v-box :src (rc/at) :gap "10px" :max-width "1000px" :max-height "700px"]
    :h-box [rc/h-box :src (rc/at) :gap "10px" :max-width "1000px" :max-height "700px"]
    (or (get lookup token)
      [rc/alert-box :src (rc/at)
       :alert-type :warning
       :body "There is a problem with this component."])))


(defn process-ui [lookup acc tree]
  (let [[node children] tree
        siblings? (and (vector? node))
        branch?   (and (vector? children)
                    (or (= :v-box node) (= :h-box node)))]
    (cond
      branch? (apply conj acc (into (parse-token lookup node) [:children (process-ui lookup [] children)]))
      siblings? (apply conj acc (mapv #(process-ui lookup [] %) tree))
      :else (apply conj acc (mapv #(parse-token lookup %) tree)))))


