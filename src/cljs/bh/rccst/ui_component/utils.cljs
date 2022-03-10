(ns bh.rccst.ui-component.utils
  (:require [bh.rccst.ui-component.navbar :as navbar]
            [cljs-uuid-utils.core :as uuid]
            [day8.re-frame.tracing :refer-macros [fn-traced]]
            [re-com.core :as rc]
            [re-frame.core :as re-frame]

            [taoensso.timbre :as log]
            [woolybear.packs.tab-panel :as tab-panel]))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; General Helper
;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; region

(def default-pub-sub {:pub [] :sub [] :container ""})


(def default-composite {:blackboard {}})


(defn config-tab-panel [chart-id]
  {:tab-panel {:value     (keyword chart-id "config")
               :data-path [:widgets (keyword chart-id) :tab-panel]}})


(defn component-id []
  (-> (uuid/make-random-uuid)
    uuid/uuid-string))


(defn chart-config [[config data panel tab] data-panel config-panel]
  ;(log/info "chart-config" config data panel tab)
  (let [data-or-config [[config "config"]
                        [data "data"]]]
    [:div.chart-config {:style {:width "100%"}}
     [navbar/navbar data-or-config [panel]]

     [rc/scroller
      :v-scroll :auto
      :height "500px"
      :child [tab-panel/tab-panel {:extra-classes             :rccst
                                   :subscribe-to-selected-tab [tab]}

              [tab-panel/sub-panel {:panel-id config}
               config-panel]

              [tab-panel/sub-panel {:panel-id data}
               data-panel]]]]))


(def h-wrap {:-webkit-flex-flow "row wrap"
             :flex-flow         "row wrap"})


(def v-wrap {:-webkit-flex-flow "column wrap"
             :flex-flow         "column wrap"})


(defn path->string [& path]
  (->> path
    flatten
    (remove nil?)
    (map str)
    (map #(clojure.string/replace % #":" ""))
    (map #(clojure.string/replace % #"/" "."))
    (map #(clojure.string/replace % #" " "-"))
    (clojure.string/join ".")))


(defn path->keyword [& path]
  (->> path
    path->string
    keyword))


(comment
  (path->string "one" "two" "three/dummy")
  (path->keyword "one" "two" "three/dummy")

  (path->keyword :area-chart-demo.area-chart :grid nil)
  (path->string :area-chart-demo.area-chart :grid nil)


  (path->keyword :topic/layers)
  (path->keyword [:topic/layers])

  (apply conj [:widgets]
    (map path->keyword [:blackboard :topic/layers]))

  ())

;; endregion



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; Widget Locals Support
;
;    suggest (re)reading https://day8.github.io/re-frame/subscriptions/
;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; region

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


(defn- process-branch [accum root k v]
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


(defn- process-leaf [accum root k]
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


(defn- compute-container-path [widget-id a more]
  (path->keyword widget-id "blackboard" a more))


(defn- compute-deps [widget-id a more]
  (if more
    (path->keyword widget-id a (drop-last more))
    (path->keyword widget-id)))


(comment
  (def widget-id "area-chart-demo.area-chart")
  (def a :tv)
  (def more ())

  (compute-deps widget-id a more)
  (compute-container-path widget-id a more)

  ())


(defn create-widget-sub
  "create and registers a re-frame [subscription handler](https://day8.github.io/re-frame/subscriptions/)
  for the `widget-id` (as a keyword) inside the `:widgets` top-level key in the `app-db`.

  ---

  - `widget-id` : (string) id for the widget, using a string means we can use generated values, like a guid, for the id

  "
  [widget-id]
  (let [id (path->keyword widget-id)]

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
  (let [p    (path->keyword widget-id a more)
        dep  (compute-deps widget-id a more)
        item (path->keyword (if more (last more) a))]

    ;(log/info "create-widget-local-sub" p
    ;  ":<-" dep
    ;  "item" item)

    (re-frame/reg-sub
      p
      :<- [dep]
      (fn [widget _]
        ;(log/info "sub" p dep widget (last more))
        (get widget item)))))


(defn- create-widget-event
  "create and registers a re-frame [event handler](https://day8.github.io/re-frame/dominoes-30k/#domino-2-event-handling)
  for the `widget-id` (as a keyword) inside the `:widgets` top-level key in the `app-db`.

  ---

  - `widget-id` : (string) id for the widget, using a string means we can use generated values, like a guid, for the id

  "
  [widget-id]
  (let [id (path->keyword widget-id)]

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
  (let [p (path->keyword widget-id a more)]

    ;(log/info "create-widget-local-event" p
    ;  "apply conj" (apply conj [:widgets (path->keyword widget-id)] (map path->keyword value-path)))

    (re-frame/reg-event-db
      p
      (fn [db [_ new-val]]
        ;(log/info "event" p new-val)
        (assoc-in db
          (apply conj [:widgets (path->keyword widget-id)] (map path->keyword value-path))
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
  (let [p (path->keyword widget-id a more)]
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

  (let [p (path->keyword widget-id a more)]
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
                     (map path->keyword)
                     (into [])))))



;; endregion


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; Composite State Support
;
;    suggest (re)reading https://day8.github.io/re-frame/subscriptions/
;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; region

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
  (let [id         (path->keyword container-id)
        c          (path->keyword :widgets container-id)
        blackboard (path->keyword container-id "blackboard")]

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
  (let [p (compute-container-path container-id a more)]
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

  (let [p (path->keyword container-id "blackboard")]
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

  (->> (process-locals [] nil local-config)
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


;; endregion


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; Remote DataSource Support
;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; region

(re-frame/reg-event-db
  :events/init-remote-data-source
  (fn-traced [db [_ data-source-id]]
    (log/info ":events/init-remote-data-source" data-source-id)))


(defn init-data-source [data-source-id]
  (log/info ":events/init-remote-data-source" data-source-id)
  (re-frame/dispatch [:events/init-remote-data-source data-source-id]))


(defn subscribe-data-source [data-source-id]
  (log/info "subscribe-data-source" data-source-id)
  (re-frame/subscribe [:data-sources (path->keyword data-source-id)]))



;; endregion


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; Rich Comments
;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; region

; how well does relative-luminance work?
(comment
  (do
    (def black {:r 0 :g 0 :b 0 :a 1.0})
    (def white {:r 255 :g 255 :b 255 :a 1.0})
    (def red {:r 255 :g 0 :b 0 :a 1.0})
    (def green {:r 0 :g 255 :b 0 :a 1.0})
    (def blue {:r 0 :g 0 :b 255 :a 1.0}))

  (relative-luminance black)
  (relative-luminance white)
  (relative-luminance red)
  (relative-luminance green)
  (relative-luminance blue)

  ())


; how well does best-text-color work?
(comment
  (do
    (def black {:r 0 :g 0 :b 0 :a 1.0})
    (def white {:r 255 :g 255 :b 255 :a 1.0})
    (def red {:r 255 :g 0 :b 0 :a 1.0})
    (def green {:r 0 :g 255 :b 0 :a 1.0})
    (def blue {:r 0 :g 0 :b 255 :a 1.0}))

  (best-text-color black)
  (best-text-color white)
  (best-text-color red)
  (best-text-color green)
  (best-text-color blue)

  ())


; need to mix the widget-id in with the path "inside" the widget's hash-map
(comment
  (def app-db
    {:widgets {:<guid-1>   {:tab-panel  {:value     :<guid-1>/dummy
                                         :data-path [:<guid-1> :tab-panel]}
                            :some-value "value"
                            :grid       {:include false}
                            :x-axis     {:include false}}
               :catalog    {:tab-panel {:value     :catalog/atoms
                                        :data-path [:catalog :tab-panel]}}
               :line-chart {:tab-panel {:value     :line-chart/config
                                        :data-path [:line-chart :tab-panel]}
                            :grid      {:include true :strokeDasharray {:d 3 :g 3}
                                        :stroke  "#ffffff"}
                            :x-axis    {:include     true :dataKey ""
                                        :orientation :bottom :scale "auto"}
                            :y-axis    {:include       true :dataKey "" :orientation
                                        :bottom :scale "auto"}
                            :legend    {:include true :layout :horizontal
                                        :align   :center :verticalAlign :bottom}
                            :tooltip   {:include true}}}})


  (def widget-id "<guid-1>")
  (def path [:some-value])
  (def path [:tab-panel :value])

  (defn subscribe-local [widget-id [a & more]]
    (let [p (path->keyword widget-id (str (name a)
                                       (when more
                                         (str "." (clojure.string/join "." (map name more))))))]
      p))
  ;(re-frame/subscribe p)))

  (let [d (subscribe-local :line-chart [:grid :strokeDasharray :d])]
    d)
  (path->keyword :line-chart)

  (subscribe-local widget-id [:some-value])
  (subscribe-local :line-chart [:tab-panel :value])
  (subscribe-local :line-chart [:grid :strokeDasharray :d])

  ())


; how do we build all the cascading subscriptions for the widget's locals?
; rocky-road just uses a single [:widget-locals widget-id :some-value]...
; so it doesn't HAVE cascaded subscriptions in the first place
(comment

  (def widget-locals {:tab-panel  {:value     :<guid-1>/dummy
                                   :data-path [:<guid-1> :tab-panel]}
                      :some-value "value"
                      :grid       {}
                      :x-axis     {}})

  ; NOTE 1: does ':data-path' need the :widgets prefix to work? PROBABLY

  ; NOTE 2: widget-locals is both the structure AND the initial value


  ; THE GOAL:
  ;
  ;      (init-widget-locals "widget-1" widget-locals)
  ;
  ; this (1) builds all the subscriptions AND (2) loads the initial data into the app-db
  ; at the correct level


  ;; region ; set initial values into the app-db:
  (defn load-local-values [widget-id values]
    (let [target (path->keyword widget-id)
          path   [:events/init-widget-locals target values]]
      (re-frame/dispatch-sync path)))

  (load-local-values "<guid-1>" widget-locals)
  (load-local-values "<guid-2>" widget-locals)

  ;; endregion

  ;; region ; building the subscriptions

  ; let's start with hand-crafted, artisanal subscriptions
  ; sub-widget
  (re-frame/reg-sub
    :widgets/<guid-1>
    :<- [:widgets]
    (fn [widgets _]
      (:<guid-1> widgets)))
  @(re-frame/subscribe [:widgets])
  (->> @(re-frame/subscribe [:widgets/<guid-1>])
    keys)

  ; sub-some-value
  (re-frame/reg-sub
    :<guid-1>/some-value
    :<- [:widgets/<guid-1>]
    (fn [widget _]
      (:some-value widget)))
  @(re-frame/subscribe [:<guid-1>/some-value])

  ; sub-tab-panel
  (re-frame/reg-sub
    :<guid-1>/tab-panel
    :<- [:widgets/<guid-1>]
    (fn [widget _]
      (:tab-panel widget)))
  @(re-frame/subscribe [:widgets/<guid-1>])
  @(re-frame/subscribe [:<guid-1>/tab-panel])

  ; sub-tab-panel-value
  ;    see `subscribe-local` above
  (re-frame/reg-sub
    :<guid-1>/tab-panel.value
    :<- [:<guid-1>/tab-panel]
    (fn [tab-panel _]
      (:value tab-panel)))
  @(re-frame/subscribe [:<guid-1>/tab-panel.value])

  ;; endregion

  ;; region ; subscribing to locals (chart around re-frame/subscribe)
  (defn subscribe-local [widget-id [a & more :as path]]
    (let [p (path->keyword widget-id (str (name a)
                                       (when more
                                         (str "." (clojure.string/join "." (map name more))))))]
      ;(log/info "subscribe-local" widget-id path p)
      (re-frame/subscribe [p])))

  ; let's spell out what we needed to build these subscriptions
  (def sub-widget ["<guid-1>"])                             ; [(assume :widgets) <widget-id>]
  (def sub-some-value ["<guid-1>" [:some-value]])           ; [<widget-id> <path>]
  (def sub-tab-panel ["<guid-1>" [:tab-panel]])             ; [<widget-id> <path>]
  (def sub-tab-panel-value ["<guid-1>" [:tab-panel :value]]) ; [<widget-id> <path>]

  ; so 2 types:
  ;      "create-widget-sub"        i.e., [<widget-id>] (`:widget` is assumed)
  ;      "create-widget-local-sub"  i.e., [<widget-id> [<path>]]

  (path->keyword :widgets "dummy.part-1.part-2")
  (path->keyword :widgets ":dummy")
  (name :dummy)

  ;; endregion

  ;; region ; create all the subscriptions (by hand)
  (defn create-widget-sub [widget-id]
    (let [id (path->keyword widget-id)
          w  (path->keyword :widgets widget-id)]
      (re-frame/reg-sub
        w
        :<- [:widgets]
        (fn [widgets _]
          (log/info w id)
          (get widgets id)))))


  (defn create-widget-local-sub [widget-id [a & more]]
    (let [p   (path->keyword widget-id (str (name a)
                                         (when more
                                           (str "." (clojure.string/join "." (map name more))))))
          dep (if more
                (path->keyword widget-id
                  (str (name a)
                    (when (seq (drop-last [:value]))
                      (str "." (clojure.string/join "." (map name (drop 1 more)))))))
                (path->keyword :widgets widget-id))]
      ;(log/info "create-widget-local-sub" p dep more (if more (last more) a))
      (re-frame/reg-sub
        p
        :<- [dep]
        (fn [widget _]
          (log/info p dep widget (last more))
          (get widget (if more (last more) a))))))


  (create-widget-sub "<guid-1>")
  @(re-frame/subscribe [:widgets/<guid-1>])

  (create-widget-local-sub "<guid-1>" [:tab-panel])
  (create-widget-local-sub "<guid-1>" [:tab-panel :value])
  (create-widget-local-sub "<guid-1>" [:tab-panel :data-path])
  (create-widget-local-sub "<guid-1>" [:some-value])
  (create-widget-local-sub "<guid-1>" [:grid])
  (create-widget-local-sub "<guid-1>" [:x-axis])

  @(subscribe-local :<guid-1> [:tab-panel])
  @(subscribe-local :<guid-1> [:tab-panel :value])
  @(subscribe-local :<guid-1> [:tab-panel :data-path])
  @(subscribe-local :<guid-1> [:some-value])
  @(subscribe-local :<guid-1> [:grid])
  @(subscribe-local :<guid-1> [:x-axis])


  @(re-frame/subscribe [:<guid-1>/tab-panel])

  (create-widget-local-sub "<guid-1>" [:tab-panel :value])
  @(subscribe-local :<guid-1> [:tab-panel :value])

  ;; endregion

  ())


; now to figure out what subscriptions need to be built for a
; given widget/initial-values-map
(comment
  (def widget-id "<guid-1>")

  ; GOAL:
  ;
  ;   (init-widget-locals widget-id widget-locals)
  ;
  ; turn widget-locals into:
  ;
  ;     {"<guid-1>" [[:tab-panel]                    => :<guid-1>/tab-panel
  ;                  [:tab-panel :value]             =>
  ;                  [:tab-panel :data-path]
  ;                  [:some-value]
  ;                  [:grid]
  ;                  [:grid :include]
  ;                  [:grid :strokeDasharray]
  ;                  [:grid :strokeDasharray :dash]  => :<guid-1>/grid.strokeDasharray.dash
  ;                  [:grid :strokeDasharray :space] => :<guid-1>/grid.strokeDasharray.space
  ;                  [:x-axis]
  ;                  [:x-axis :include]
  ;                  [:set-of-data]}
  ;
  ; which can then be processed by
  ;    (create-widget-sub) and (create-widget-local-sub)
  ;

  (def widget-locals {:tab-panel   {:value     :<guid-1>/dummy
                                    :data-path [:<guid-1> :tab-panel]}
                      :some-value  "value"
                      :grid        {:include         true
                                    :strokeDasharray {:dash 3 :space 3}}
                      :x-axis      {:include     true
                                    :orientation :bottom}
                      :set-of-data {}})

  (reduce + 0 [1 2 3 4 5])

  (loop [a 0
         c [1 2 3 4 5]]
    (if (empty? c)
      a                                                     ; done!
      (recur (+ a (first c)) (rest c))))


  (defn process-locals [a r t]
    (println "process-locals" a r t)
    (loop [accum a
           root  r
           tree  t]
      (println "process" tree root accum)
      (if (empty? tree)
        (do
          (println "result" accum)
          accum)
        (let [[k v] (first tree)]
          (println "let" k v)
          (recur (if (map? v)
                   (do
                     (println "branch" v (if root
                                           (if (vector? root)
                                             (conj root k)
                                             [root k])
                                           [k]) accum)
                     (as-> accum x
                       ; add this root to the accum
                       (conj x (if root
                                 (if (vector? root)
                                   (conj root k)
                                   [root k])
                                 [k]))
                       ; now process the sub-tree
                       (apply conj x (process-locals []
                                       (if root
                                         (if (vector? root)
                                           (conj root k)
                                           [root k])
                                         k)
                                       v))))
                   (do
                     (println "leaf" root k accum)
                     (conj accum (if root
                                   (if (vector? root)
                                     (conj root k)
                                     [root k])
                                   [k]))))
            root
            (rest tree))))))

  ;; region ; example-based tests
  (= (process-locals [] nil {:a 1 :b 2})
    [[:a] [:b]])

  (= (process-locals [] nil {:a 1 :b 2 :c 3})
    [[:a] [:b] [:c]])

  (= (process-locals [] nil {:a 1 :b {:c 2 :d 3}})
    [[:a] [:b] [:b :c] [:b :d]])

  (= (process-locals [] nil {:a 1 :b {:c 2} :d {:e 3}})
    [[:a] [:b] [:d] [:b :c] [:d :e]])

  (= (process-locals [] nil {:a 1 :b {:c 2 :d {:e 3 :f 4}}})
    [[:a] [:b] [:b :c] [:b :d] [:b :d :e] [:b :d :f]])

  (= (process-locals [] nil {:a 1 :b {:c 2 :d {:e 3 :f {:g [2 4] :h {:i 100}}}}})
    [[:a] [:b] [:b :c] [:b :d] [:b :d :e] [:b :d :f]
     [:b :d :f :g] [:b :d :f :h] [:b :d :f :h :i]])


  (= (process-locals [] nil widget-locals)
    [[:tab-panel]
     [:tab-panel :value]
     [:tab-panel :data-path]
     [:some-value]
     [:grid]
     [:grid :include]
     [:grid :strokeDasharray]
     [:grid :strokeDasharray :dash]
     [:grid :strokeDasharray :space]
     [:x-axis]
     [:x-axis :include]
     [:x-axis :orientation]
     [:set-of-data]])

  ;; endregion


  ())


; building the complete set of subscriptions and event-handlers for a 'widget'
; and then testing them out
(comment
  (do
    (def widget-id "<guid-1>")
    (def widget-locals {:tab-panel   {:selected-panel :<guid-1>/dummy
                                      :data-path      [:<guid-1> :tab-panel]}
                        :some-value  "value"
                        :grid        {:include         true
                                      :strokeDasharray {:dash 3 :space 3}}
                        :x-axis      {:include     true
                                      :orientation :bottom}
                        :set-of-data #{}}))

  (conj [1 2 3] [4 5])
  (apply conj [1 2 3] [4 5])

  ; set everything up
  (init-widget widget-id widget-locals)

  ;; region ; try out some subscriptions
  (= @(subscribe-local widget-id [:tab-panel])
    [])
  @(subscribe-local widget-id [:tab-panel :value])
  @(subscribe-local widget-id [:tab-panel :data-path])
  @(subscribe-local widget-id [:some-value])
  @(subscribe-local widget-id [:grid])
  @(subscribe-local widget-id [:grid :include])
  @(subscribe-local widget-id [:grid :strokeDasharray])
  @(subscribe-local widget-id [:grid :strokeDasharray :dash])
  @(subscribe-local widget-id [:grid :strokeDasharray :space])
  @(subscribe-local widget-id [:x-axis])
  @(subscribe-local widget-id [:x-axis :include])
  @(subscribe-local widget-id [:x-axis :orientation])
  @(subscribe-local widget-id [:set-of-data])

  ;; endregion

  ;; region ; try out the event-handler (user the subscription above to see the updated value)
  (dispatch-local widget-id [:grid :include] true)
  (dispatch-local widget-id [:grid :include] false)
  (dispatch-local widget-id [:grid :strokeDasharray :dash] 5)
  (dispatch-local widget-id [:grid :strokeDasharray :space] 1)
  (dispatch-local widget-id [:set-of-data] #{1 2 3 4 5})




  ;; endregion



  ())


;; endregion


; playing with subscriptions and events
(comment
  @(subscribe-local "line-chart-demo" [:line-chart-demo/tab-panel.value])
  (dispatch-local "line-chart-demo" [:tab-panel :value] :line-chart-demo/data)
  (dispatch-local "line-chart-demo" [:tab-panel :value] :line-chart-demo/config)

  (re-frame/dispatch [:line-chart-demo/tab-panel.value :line-chart-demo/config])
  (re-frame/dispatch [:line-chart-demo/tab-panel.value :line-chart-demo/data])


  ())



; how do we publish things to a "container"?
(comment
  (do (def db {:widgets {:container {:blackboard {}}}})
      (def container-id :container)
      (def component-path [:chart-1 :data]))

  (get-in db [:widgets container-id :blackboard])

  (-> db
    (update-in [:widgets container-id :blackboard]
      assoc [:chart-1 :data] "new-val")
    (update-in [:widgets container-id :blackboard]
      assoc [:chart-2 :data] "another-val"))

  ())



; turning a hash-map into a collection of vectors that are the key paths into all
; the data leaves
(comment
  (def local-config {:brush false,
                     :uv    {:include true, :stroke "#8884d8", :fill "#8884d8"},
                     :pv    {:include true, :stroke "#ffc107", :fill "#ffc107"},
                     :tv    {:include true, :stroke "#82ca9d", :fill "#82ca9d"},
                     :amt   {:include true, :stroke "#ff00ff", :fill "#ff00ff"}})


  (->> (process-locals [] nil local-config)
    (map (fn [path]
           (log/info "build-container-subs" container-id path)
           {path (subscribe-to-container container-id path)}))
    (into {}))

  (do
    (def widget-id "multi-chart-demo/multi-chart")
    (def a :tv)
    (def more [:fill]))

  (compute-container-path widget-id a more)




  ())



; building valid keyword for use in the app-db, subscriptions, events, etc
(comment
  (def path ["line-chart-demo" "line-chart" "tab-panel" "value"])
  (def path2 '("line-chart-demo" "line-chart" [:uv :fill]))

  (keyword (clojure.string/join "." path))

  (path->keyword "line-chart-demo" "line-chart" "tab-panel" "value")

  (flatten path2)
  (apply conj [] (flatten path2))

  (->> path2
    flatten
    (apply conj [])
    (map name)
    (clojure.string/join ".")
    keyword)

  (path->keyword "line-chart-demo" "line-chart" [:uv :fill])

  ())
