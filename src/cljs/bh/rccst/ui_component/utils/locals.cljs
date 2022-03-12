(ns bh.rccst.ui-component.utils.locals
  (:require [re-frame.core :as re-frame]
            [day8.re-frame.tracing :refer-macros [fn-traced]]
            [bh.rccst.ui-component.utils.helpers :as h]))


(re-frame/reg-event-db
  :events/init-widget-locals
  (fn-traced [db [_ container init-vals]]
    ;(log/info "::init-widget-locals" container init-vals)
    (if (get-in db [:widgets container])
      (do
        ;(log/info "::init-widget-locals // already exists")
        db)
      (do
        ;(log/info "::init-widget-locals // adding")
        (assoc-in db [:widgets container] init-vals)))))


(defn init-local-values
  "add the given 'tree' of values into the app-db under the `[:widgets <widget-id as a keyword>] path

  `widget-id` is automatically converted into a `keyword`

  In cases where there are multiple widgets of the same 'type', using 'locals' keeps each
  instance's local state away form all the others, so changing the state of one does _not_ change
  them all.

  ---

  - `widget-id` : (string) id of the widget, passed as a string so we can use generated values (like guids)
  - `values` : (hash-map) hash-map (tree) of values specific to _this_ widget.

  "
  [widget-id values]
  (let [target (keyword widget-id)
        path   [:events/init-widget-locals target values]]
    ;(log/info "init-local-values" path)
    (re-frame/dispatch-sync path)))


(declare process-locals)


(defn process-branch [accum root k v]
  (do
    ;(println "branch" v [root k] accum)
    (as-> accum x
      (conj x (if root
                (if (vector? root)
                  (conj root k)
                  [root k])
                [k]))
      (apply conj x (process-locals []
                      (if root
                        (if (vector? root)
                          (conj root k)
                          [root k])
                        k)
                      v)))))


(defn process-leaf [accum root k]
  (do
    ;(println "leaf" root k accum)
    (conj accum (if root
                  (if (vector? root)
                    (conj root k)
                    [root k])
                  [k]))))


(defn process-locals
  "recursively walks through the 'tree' of values and computes the 'path vector' to reach each
  value.

  For example:

  `{:value-1 \"dummy\" :value-2 {:nested-value \"dummy\"}}`

  would have paths:

  `[[:value-1] [:value-2] [:value-2 :nested-value]]`

  This is a preparation step for creating and registering the re-frame
  [subscription handlers](https://day8.github.io/re-frame/subscriptions/), so we must
  create a vector for each value in the 'tree' so other code, like a UI 'widget', can subscribe to
  the value and automatically 'update and render'.

  ---

  - a : (vector) the starting value to accumulate the result into, typically `[]`
  - r : (any) the initial value of the 'root' item, typically `()`
  - t : (hash-map) the 'tree' of values to process

  Returns a `vector` of `vector`s of `keyword`s, where each is the path (relative to the initial `r`^*^) to
  the specific value of interest.

> Note: ^*^ typically we sort out the 'base' for the relative paths separately, using
> [[create-widget-local-sub]]

  "
  [a r t]
  ;(println "process-locals" a r t)
  (loop [accum a root r tree t]
    ;(println "process" tree root accum)
    (if (empty? tree)
      (do
        ;(println "result" accum)
        accum)
      (let [[k v] (first tree)]
        ;(println "let" k v)
        (recur (if (map? v)
                 (process-branch accum root k v)
                 (process-leaf accum root k))
          root
          (rest tree))))))


(defn compute-container-path [widget-id a more]
  (h/path->keyword widget-id "blackboard" a more))


(defn compute-deps [widget-id a more]
  (if more
    (h/path->keyword widget-id a (drop-last more))
    (h/path->keyword widget-id)))


(defn create-widget-sub
  "create and registers a re-frame [subscription handler](https://day8.github.io/re-frame/subscriptions/)
  for the `widget-id` (as a keyword) inside the `:widgets` top-level key in the `app-db`.

  ---

  - `widget-id` : (string) id for the widget, using a string means we can use generated values, like a guid, for the id

  "
  [widget-id]
  (let [id (h/path->keyword widget-id)]

    ;(log/info "create-widget-sub" id)

    (re-frame/reg-sub
      id
      :<- [:widgets]
      (fn [widgets _]
        ;(log/info "sub" w id)
        (get widgets id)))))


(defn create-widget-local-sub
  "create and registers a re-frame [subscription handler](https://day8.github.io/re-frame/subscriptions/)
  for the value at the path inside the [`:widgets` `widget-id as a keyword`] key in the `app-db`.

  ---

  - `widget-id` : (string) id for the widget, using a string means we can use generated values, like a guid, for the id
  - `value-path` : (vector of keywords) the path into the widget values to locate the specific one for this subscription

  `value-path` functions exactly like any other re-frame subscription, but relative to the
  `[:widgets <widget-id as a keyword>]` in the overall `app-db`

  It is destructured as follows:

  | var        | type       | description                         |
  |:-----------|:----------:|:------------------------------------|
  | `a`        | keyword    | the (primary) value to subscribe to |
  | `& more`   | keyword(s) | any additional parts to the path    |

  We use 'cascading subscriptions', i.e., [`Layer-3` subscriptions](https://day8.github.io/re-frame/subscriptions/#reg-sub),
  to organize things. In order to generate unique ids for each subscription, we concatenate the
  path into a single value:

  assuming: `(def widget-wid \"some-guid\")` then path `[:value-2 :nested-value]` would be converted into the subscription named
  `:some-guid/value-2.nested-value`

> Note: so developer don't need to understand or even remember this encoding scheme, use the [[subscribe-local]] helper function
> in place of standard re-frame subscription calls. It provides the same result, and does all the encoding for you.
  "
  [widget-id [a & more :as value-path]]
  (let [p    (h/path->keyword widget-id a more)
        dep  (compute-deps widget-id a more)
        item (h/path->keyword (if more (last more) a))]

    ;(log/info "create-widget-local-sub" p
    ;  ":<-" dep
    ;  "item" item)

    (re-frame/reg-sub
      p
      :<- [dep]
      (fn [widget _]
        ;(log/info "sub" p dep widget (last more))
        (get widget item)))))


(defn create-widget-event
  "create and registers a re-frame [event handler](https://day8.github.io/re-frame/dominoes-30k/#domino-2-event-handling)
  for the `widget-id` (as a keyword) inside the `:widgets` top-level key in the `app-db`.

  ---

  - `widget-id` : (string) id for the widget, using a string means we can use generated values, like a guid, for the id

  "
  [widget-id]
  (let [id (h/path->keyword widget-id)]

    ;(log/info "create-widget-event" id)

    (re-frame/reg-event-db
      id
      (fn [db [_ new-val]]
        ;(log/info "event" w id)
        (assoc-in db [:widgets id] new-val)))))


(defn create-widget-local-event
  "create and registers a re-frame [event handler](https://day8.github.io/re-frame/dominoes-30k/#domino-2-event-handling)
  for the value at the path inside the [`:widgets` `widget-id as a keyword`] key in the `app-db`.

  ---

  - `widget-id` : (string) id for the widget, using a string means we can use generated values, like a guid, for the id
  - `value-path` : (vector of keywords) the path into the widget values to locate the specific one for this subscription

  `value-path` functions exactly like any other re-frame subscription, but relative to the
  `[:widgets <widget-id as a keyword>]` in the overall `app-db`

  It is destructured as follows:

  | var        | type       | description                         |
  |:-----------|:----------:|:------------------------------------|
  | `a`        | keyword    | the (primary) value to subscribe to |
  | `& more`   | keyword(s) | any additional parts to the path    |

  We use 'cascading subscriptions', i.e., [`Layer-3` subscriptions](https://day8.github.io/re-frame/subscriptions/#reg-sub),
  to organize things. In order to generate unique ids for each subscription, we concatenate the
  path into a single value:

  assuming: `(def widget-wid \"some-guid\")` then path `[:value-2 :nested-value]` would be converted into the subscription named
  `:some-guid/value-2.nested-value`

> Note: so developer don't need to understand or even remember this encoding scheme, use the [[subscribe-local]] helper function
> in place of standard re-frame subscription calls. It provides the same result, and does all the encoding for you.
  "
  [widget-id [a & more :as value-path]]
  (let [p (h/path->keyword widget-id a more)]

    ;(log/info "create-widget-local-event" p
    ;  "apply conj" (apply conj [:widgets (path->keyword widget-id)] (map path->keyword value-path)))

    (re-frame/reg-event-db
      p
      (fn [db [_ new-val]]
        ;(log/info "event" p new-val)

        ; NOTE: this "default" processing could be overridden (using an optional keyword)
        ; to perform more custom functions (like incremental updates to a collection)
        ;
        (assoc-in db
          (apply conj [:widgets (h/path->keyword widget-id)] (map h/path->keyword value-path))
          new-val)))))


(defn init-widget
  "1. adds the `locals-and`defaults` into the `app-db` in the correct location
  2. creates and registers a subscription to `:widgets/<widget-id>`
  3. creates and registers a subscription (cascaded off `:widgets/<widget-id>`) for each relative path in `locals-and-defaults`
  4. creates and registers an event handler for`:widgets/<widget-id>`
  5. creates and registers an event handler for each relative path in `locals-and-defaults`

  `locals-and-defaults` provides both the structure used to create the subscriptions and the default values when a new widget is
  created

  ---

  - `widget-id` : (string) id for the widget, using a string means we can use generated values, like a guid, for the id
  - `locals-and-defaults` : (hash-map) hash-map (tree) of values specific to _this_ widget.

> TODO: need to build the reg-event-db handlers so users/ui can change the locals
  "
  [widget-id locals-and-defaults]
  (let [paths (process-locals [] nil locals-and-defaults)]

    ;(log/info "init-widget" widget-id paths)

    ; load the app-db with the default values
    (init-local-values widget-id locals-and-defaults)

    ; create subscriptions
    (create-widget-sub widget-id)
    (doall
      (map #(create-widget-local-sub widget-id %) paths))

    ; create event handlers
    (create-widget-event widget-id)
    (doall
      (map #(create-widget-local-event widget-id %) paths))))


(defn subscribe-local
  "constructs a Re-frame subscription to a local value since the given
  widget's 'locals' in the `app-db`. This way the developer isn't concerned about the
  exact location of the data in the `app-db`.

  The widget-id string will be converted into a keyword as appropriate to access the
  registered subscription, so you can freely use generated values as widget identifiers

> NOTE: the re-frame subscriptions ***MUST*** be created beforehand, using [[init-widget]]

  ---

  - `widget-id` : (string) name of the widget, typically a guid, but it can be any string you'd like
  - `value-path : (vector of keywords) the path into the widget values to locate the specific one for this subscription

  `value-path` functions exactly like any other re-frame subscription, but relative to the
  `[:widgets <widget-id>]` in the overall `app-db`

  It is destructured as follows:

  | var        | type       | description                         |
  |:-----------|:----------:|:------------------------------------|
  | `a`        | keyword    | the (primary) value to subscribe to |
  | `& more`   | keyword(s) | any additional parts to the path    |

  Returns a `reagent/reaction` which can be used exactly like any other re-frame subscription

   ---

   #### EXAMPLES

   Assume

   `(def widget-id \"some-guid\")`

   and

   `(def some-guid-locals {:value-1 \"default\" :value-2 {:nested-value \"default\"}})`

   | desired subscription | call                                         |
   |:--------------------:| :--------------------------------------------|
   | `:value-1`           | `(subscribe-local \"some-guid\" [:value-1])` |
   | `:value-2`           | `(subscribe-local \"some-guid\" [:value-2])` |
   | `:nested-value`      | `(subscribe-local \"some-guid\" [:value-2 :nested-value])` |
  "
  [widget-id [a & more :as value-path]]
  (let [p (h/path->keyword widget-id a more)]
    ;(log/info "subscribe-local" widget-id value-path p)
    (re-frame/subscribe [p])))


(defn dispatch-local
  "constructs a Re-frame event dispatch call to a local value stored in the given
  widget's 'locals' in the `app-db`. This way the developer isn't concerned about the
  exact location of the data in the `app-db`.

  The widget-id string will be converted into a keyword as appropriate to access the
  registered subscription, so you can freely use generated values as widget identifiers

> NOTE: the re-frame event-handlers ***MUST*** be created beforehand, using [[init-widget]]

  ---

  - `widget-id` : (string) name of the widget, typically a guid, but it can be any string you'd like
  - `value-path : (vector of keywords) the path into the widget values to locate the specific one for this subscription
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
  [widget-id [a & more :as value-path] new-val]

  ;(log/info "dispatch-local" widget-id value-path new-val)

  (let [p (h/path->keyword widget-id a more)]
    ;(log/info "dispatch-local" widget-id "//" value-path "//" p "//" new-val)
    (re-frame/dispatch [p new-val])))


(defn build-subs
  "build the subscription needed to access all the 'local' configuration
  properties

  1. process-locals
  2. map over the result and call ui-utils/subscribe-local
  3. put the result into a hash-map
  "
  [component-id local-config]
  (->> (process-locals [] nil local-config)
    (map (fn [path]
           {path (subscribe-local component-id path)}))
    (into {})))


(defn resolve-sub [subs path]
  (deref (get subs (->> path
                     (map h/path->keyword)
                     (into [])))))

