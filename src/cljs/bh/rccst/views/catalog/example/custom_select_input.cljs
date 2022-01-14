(ns bh.rccst.views.catalog.example.custom-select-input
  (:require [woolybear.ad.catalog.utils :as acu]
            [woolybear.ad.utils :as adu]
            [woolybear.ad.layout :as layout]
            [woolybear.ad.forms :as forms]))

(defn example []
      (acu/demo "Select input with custom labels and values"
                "This example uses custom :get-label-fn and :get-value-fn options to display a list of items in a
                more human-friendly format, and to return a custom value for each option. Watch the JS console for
                on-change messages when making a selection."
                [layout/padded
                 [forms/select-input {:id                          "select-demo-2"
                                      :component-data-path         [:catalog :forms-demo :select-2]
                                      :subscribe-to-component-data [:forms-demo/demo :select-2]
                                      :subscribe-to-option-items   (atom [{:id 1 :type :widget}
                                                                          {:id 2 :type :widget}
                                                                          {:id 3 :type :gizmo}
                                                                          {:id 4 :type :widget}
                                                                          {:id 5 :type :gizmo}])
                                      :get-label-fn                (fn [item] (str (name (:type item)) "-" (:id item)))
                                      :get-value-fn                (fn [item] (str (:id item)))
                                      :on-change                   (fn [_ new-val] (js/console.log "Selected %o" (adu/js-event-val new-val)))}]]
                '[layout/padded
                  [forms/select-input {:id                          "select-demo-2"
                                       :component-data-path         [:catalog :forms-demo :select-2]
                                       :subscribe-to-component-data [:forms-demo/demo :select-2]
                                       :subscribe-to-option-items   (atom [{:id 1 :type :widget}
                                                                           {:id 2 :type :widget}
                                                                           {:id 3 :type :gizmo}
                                                                           {:id 4 :type :widget}
                                                                           {:id 5 :type :gizmo}])
                                       :get-label-fn                (fn [item] (str (name (:type item)) "-" (:id item)))
                                       :get-value-fn                (fn [item] (str (:id item)))
                                       :on-change                   (fn [_ new-val] (fn [_ new-val] (js/console.log "Selected %o" (adu/js-event-val new-val))))}]]))
