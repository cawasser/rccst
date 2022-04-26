(ns bh.rccst.ui-component.molecule.example
  (:require [bh.rccst.ui-component.utils :as ui-utils]
            [bh.rccst.views.atom.utils :as bcu]
            [taoensso.timbre :as log]
            [woolybear.ad.catalog.utils :as acu]
            [woolybear.ad.layout :as layout]))


(defn component-example [& {:keys [title
                                   description
                                   data
                                   container-id
                                   component
                                   component-id
                                   source-code
                                   extra-classes
                                   extra-params] :as input-params}]

  ;(log/info "component-example" input-params)

  (ui-utils/init-container component-id)

  ; TODO: can this be converted to (apply concat...)? (see https://clojuredesign.club/episode/080-apply-as-needed/)
  (let [params (reduce (fn [accum [k v]] (conj accum k v))
                (seq
                  (apply merge
                    {:data data :component-id component-id :container-id container-id}
                    extra-params)))]

    ;(log/info "component-example" component-id "//" params)

    (acu/demo
      title
      description
      [layout/centered (or extra-classes {})
       [:div {:style {:width "1000px" :height "700px"}}
        ; TODO: can this be converted to (apply concat...)? (see https://clojuredesign.club/episode/080-apply-as-needed/)
        (reduce conj [component] params)]]
      source-code)))


(defn example [& {:keys [title container-id description
                         data config
                         data-panel config-panel component-panel
                         source-code] :as params}]

  ;(log/info "example" params)

  (ui-utils/init-widget container-id config)

  (let [config-key (keyword container-id "config")
        data-key (keyword container-id "data")
        tab-panel (keyword container-id "tab-panel")
        selected-tab (keyword container-id "tab-panel.value")]

    (bcu/configurable-demo
      title
      description
      [config-key data-key tab-panel selected-tab]
      [data-panel data]
      [config-panel data container-id]
      [:div {:style {:width "1500px" :height "700px"}}
       [component-panel data container-id]]
      source-code)))



(comment
  (def extra-params {:node-types "dummy"})
  (def extra-params {:node-types "diagraph/default-node-types"
                     :minimap-styles "diagraph/default-minimap-styles"})
  (def params {:extra-params extra-params})


  (map (fn [[k v]]
         {:k k :v v})
    extra-params)

  (let [{:keys [extra-params]} params]
    (reduce (fn [accum [k v]] (conj accum k v))
      ["container"]
      (seq
        (apply merge
          {:data ["data" :blackboard] :component-id "component-id" :container-id "container-id"}
          extra-params))))

  (reduce conj ["container"] (flatten (seq {:one "one" :two "two"})))


  ())