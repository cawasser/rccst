(ns bh.rccst.ui-component.utils.container
  (:require [re-frame.core :as re-frame]
            [day8.re-frame.tracing :refer-macros [fn-traced]]
            [bh.rccst.ui-component.utils.helpers :as h]
            [bh.rccst.ui-component.utils.locals :as l]))


(def default-composite {:blackboard {}})


(re-frame/reg-event-db
  :events/init-container
  (fn-traced [db [_ container]]
    ;(log/info ":events/init-container" container)
    (if (get-in db [:widgets container])
      (do
        ;(log/info ":events/init-container // already exists")
        db)
      (do
        ;(log/info ":events/init-container // adding")
        (assoc-in db [:widgets container] default-composite)))))


(defn init-container [container-id]
  (let [id         (h/path->keyword container-id)
        c          (h/path->keyword :widgets container-id)
        blackboard (h/path->keyword container-id "blackboard")]

    ;(log/info "init-container" container-id id c blackboard)

    (re-frame/reg-sub
      c
      :<- [:widgets]
      (fn [widgets _]
        ;(log/info "sub" c id)
        (get widgets id)))

    (re-frame/reg-sub
      blackboard
      :<- [c]
      (fn [w [_ path]]
        ;(log/info "blackboard sub" w blackboard)
        (get-in w path)))

    (re-frame/reg-event-db
      blackboard
      (fn [bb [_ id component-path new-val]]
        ;(log/info "container-event blackboard" id component-path new-val)
        (update-in bb [:widgets id :blackboard]
          assoc component-path new-val)))))

;(re-frame/dispatch-sync [:events/init-container id])))


(defn subscribe-to-container [container-id [a & more :as component-path]]
  (let [p (l/compute-container-path container-id a more)]
    ;(log/info "subscribe-to-container" container-id component-path p)
    (re-frame/subscribe [p])))


(defn publish-to-container
  "
> NOTE: the re-frame event-handlers ***MUST*** be created beforehand, using [[init-widget]]

  ---

  - `container-id` : (string) name of the widget, typically a guid, but it can be any string you'd like
  - `component-path : (vector of keys [keywords or string]) the 'key' for the item that is being publised
  - `new-val` : (any) the new value to store at the given path

  `value-path` functions exactly like any other re-frame subscription, but relative to the
  `[:widgets <widget-id>]` in the overall `app-db`

  It is destructured as follows:

  | var        | type       | description                         |
  |:-----------|:----------:|:------------------------------------|
  | `a`        | keyword    | the (primary) value to subscribe to |
  | `& more`   | keyword(s) | any additional parts to the path    |

   ---

   #### EXAMPLES

  "
  [container-id component-path new-val]

  ;(log/info "publish-to-container-local" container-id component-path new-val)

  (let [p (h/path->keyword container-id "blackboard")]
    ;(log/info "publish-to-container" container-id component-path new-val p)
    (re-frame/dispatch [p component-path new-val])))


(defn build-container-subs
  "build the subscription needed to access all the container's configuration
  properties

  1. process-locals
  2. map over the result and call ui-utils/subscribe-to-container
  3. put the result into a hash-map
  "
  [container-id local-config]

  (->> (l/process-locals [] nil local-config)
    (map (fn [path]
           ;(log/info "build-container-subs" container-id path)
           {path (subscribe-to-container container-id path)}))
    (into {})))


(defn override-subs [container-id local-subs subs]
  ;(log/info "override-subs" subs)
  (->> subs
    (map (fn [path]
           ;(log/info "override-subs map" container-id path)
           (let [s (subscribe-to-container container-id path)]
             (when s {path s}))))
    (apply merge local-subs)))


