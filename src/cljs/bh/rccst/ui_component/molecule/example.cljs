(ns bh.rccst.ui-component.molecule.example
  (:require [bh.rccst.ui-component.utils :as ui-utils]
            [bh.rccst.views.atom.utils :as bcu]
            [taoensso.timbre :as log]
            [woolybear.ad.catalog.utils :as acu]
            [woolybear.ad.layout :as layout]))


(defn component-example [& {:keys [title
                                   description
                                   component
                                   component-id
                                   source-code
                                   extra-classes] :as params}]

  (ui-utils/init-container component-id)

  (let [input-params (dissoc params :title :sample-data :description :extra-classes :source-code :component)
        paramed-component (reduce into [component] (seq input-params))]

    ;(log/info "component-example" params
      ;"////" input-params
      ;"////" paramed-component

    (acu/demo
      title
      description
      [layout/centered (or extra-classes {})
       ;;
       ;; NOTE: the :height MUST be specified here since the ResponsiveContainer down in bowels of the chart needs a height
       ;; in order to actually draw the Recharts components. just saying "100%" doesn't work, since the
       ;; that really means "be as big as you need" and ResponsiveContainer then doesn't know what to do.
       ;;
       [:div.component-example {:style {:width "100%" :height "700px"}}
        paramed-component]]

      source-code)))


(defn example [& {:keys [title container-id description
                         data config
                         data-panel config-panel component-panel
                         source-code] :as params}]

  ;(log/info "example" params)

  (ui-utils/init-container-locals container-id config)

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
      ;[:div.configurable-demo-component {:style {:width "100%" :height "100%"}} ;{:width "1500px" :height "700px"}}
      [component-panel data container-id]
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






;(comment
;  [#object[G__40345]
;   :data-panel #object[bh$rccst$ui_component$atom$chart$utils$dag_data_panel]
;   :component #object[G__40345]
;   :link-color-fn #object[bh$rccst$ui_component$atom$chart$sankey_chart$color_source__GT_target]
;   :container-id :sankey-chart-data-ratom-demo
;   :component-id :sankey-chart-data-ratom-demo.chart
;   :data-tools #object[bh$rccst$views$atom$example$chart$alt$data_tools$dag_data_ratom_tools]
;   :config-panel #object[bh$rccst$ui_component$atom$chart$sankey_chart$config_panel]
;   :data #object[reagent.ratom.RAtom {:val {:nodes [{:name "Visit"}
;                                                    {:name "Direct-Favourite"}
;                                                    {:name "Page-Click"}
;                                                    {:name "Detail-Favourite"}
;                                                    {:name "Lost"}],
;                                            :links [{:source 0, :target 1, :value 3728.3}
;                                                    {:source 0, :target 2, :value 354170}
;                                                    {:source 2, :target 3, :value 62429}
;                                                    {:source 2, :target 4, :value 291741}]}}]]
;
;
;  (:data #object[reagent.ratom.RAtom {:val {:nodes [{:name "Visit"}
;                                                    {:name "Direct-Favourite"}
;                                                    {:name "Page-Click"}
;                                                    {:name "Detail-Favourite"}
;                                                    {:name "Lost"}],
;                                            :links [{:source 0, :target 1, :value 3728.3}
;                                                    {:source 0, :target 2, :value 354170}
;                                                    {:source 2, :target 3, :value 62429}
;                                                    {:source 2, :target 4, :value 291741}]}}]
;    :config-data nil
;    :component-id :sankey-chart-data-ratom-demo.chart
;    :container-id :sankey-chart-data-ratom-demo
;    :data-panel #object[bh$rccst$ui_component$atom$chart$utils$dag_data_panel]
;    :config-panel #object[bh$rccst$ui_component$atom$chart$sankey_chart$config_panel])
;
;
;  ())




