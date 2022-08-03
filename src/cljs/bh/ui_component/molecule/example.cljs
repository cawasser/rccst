(ns bh.ui-component.molecule.example
  (:require [bh.ui-component.utils :as ui-utils]
            [rccst.views.atom.utils :as bcu]
            [taoensso.timbre :as log]
            [woolybear.ad.catalog.utils :as acu]
            [woolybear.ad.layout :as layout]))









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




