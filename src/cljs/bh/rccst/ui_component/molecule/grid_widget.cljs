(ns bh.rccst.ui-component.molecule.grid-widget
  (:require [bh.rccst.ui-component.layout-grid :as grid]
            [bh.rccst.ui-component.molecule.composite :as composite]
            [bh.rccst.ui-component.molecule.composite.util.digraph :as dig]
            [bh.rccst.ui-component.molecule.composite.util.signals :as sig]
            [bh.rccst.ui-component.molecule.composite.util.ui :as ui]
            [bh.rccst.ui-component.utils :as ui-utils]
            [loom.graph :as lg]
            [re-com.core :as rc]
            [reagent.core :as r]
            [taoensso.timbre :as log]))


(log/info "bh.rccst.ui-component.molecule.grid-widget")


(defn- build-ui [[id component]]

  (log/info "build-ui" id)

  [:div.widget-parent {:key id}
   [:div.grid-toolbar (name id)]
   [:div.widget.widget-content
    {:style         {:width       "100%"
                     :height      "100%"
                     :cursor      :default
                     :align-items :stretch
                     :display     :flex}
     :on-mouse-down #(.stopPropagation %)}
    [:div {:style {:width "100%" :height "100%" :display :flex}}
     component]]])


(defn- on-layout-change [layout-atom new-layout]
  (let [layout (js->clj new-layout :keywordize-keys true)
        fst    (first layout)]

    (log/info "on-layout-change"
      ;(js->clj new-layout)
      "//" @layout-atom
      "//" layout)
    ;  "//" fst)

    (when (and
            (not (empty? layout))
            (<= 1 (count layout))
            (not= (:i fst) "null"))
      (let [cooked (map #(zipmap '(:i :x :y :w :h) %)
                     (map (juxt :i :x :y :w :h) layout))]
        (reset! layout-atom (zipmap (map :i cooked) cooked))))))


(defn- component-panel [& {:keys [configuration component-id container-id]}]
  ;(log/info "component-panel" component-id
  ;  "//" (keys configuration)
  ;  "// dummy-layout" dummy-layout
  ;  "// :components" (:components configuration)
  ;  "// process-components" (into {}
  ;                            (sig/process-components
  ;                              configuration :ui/component
  ;                              composite/meta-data-registry component-id)))

  (let [layout           (r/atom (:grid-layout configuration))
        layout-atom      (atom (:grid-layout configuration))
        components       (:components configuration)
        component-lookup (into {}
                           (sig/process-components
                             configuration :ui/component
                             composite/meta-data-registry component-id))

        ; 1. build UI components (with subscription/event signals against the blackboard or remotes)
        composed-ui      (map build-ui component-lookup)]

    ;(log/info "component-panel INNER" component-id "//" composed-ui)

    (fn []
      ; 5. return the composed component layout!
      [:div.grid-container {:style {:width "100%" :height "100%"}}
       [grid/grid
        :id component-id
        :class "layout"
        :children composed-ui
        :layout layout
        :layoutFn #(on-layout-change layout-atom %)
        :isDraggable true
        :isResizable true
        :draggableHandle ".grid-toolbar"
        :draggableCancel ".grid-content"
        :cols (r/atom 12)
        :rowHeight 25
        :compactType :vertical]])))


(defn component [& {:keys [data component-id container-id]}]
  (let [id            (r/atom nil)
        configuration @data
        graph         (apply lg/digraph (ui/compute-edges configuration))
        comp-or-dag?  (r/atom :component)
        full-config   (assoc configuration
                        :graph graph
                        :denorm (dig/denorm-components graph (:links configuration) (lg/nodes graph))
                        :nodes (lg/nodes graph)
                        :edges (lg/edges graph))]

    (fn []
      (when (nil? @id)
        (reset! id component-id)
        (ui-utils/init-widget @id {:blackboard {} :container ""})
        (ui-utils/dispatch-local @id [:container] container-id)
        (ui/prep-environment full-config @id composite/meta-data-registry))

      (let [buttons [{:id :component :label [:i {:class "zmdi zmdi-view-compact"}]}
                     {:id :dag :label [:i {:class "zmdi zmdi-share"}]}
                     {:id :definition :label [:i {:class "zmdi zmdi-format-subject"}]}]]

        [:div.box {:style {:width  "1000px" :height "800px"}}
         [rc/h-box :src (rc/at)
          :justify :end
          :width "100%"
          :height "100%"
          :children [(condp = @comp-or-dag?
                       :dag [composite/dag-panel
                             :configuration full-config
                             :component-id @id
                             :container-id container-id]
                       :component [component-panel
                                   :configuration full-config
                                   :component-id @id
                                   :container-id container-id]
                       :definition [composite/definition-panel
                                    :configuration configuration]
                       :default [rc/alert-box :src (rc/at)
                                 :alert-type :warning
                                 :body "There is a problem with this component."])

                     [rc/horizontal-bar-tabs
                      :model comp-or-dag?
                      :tabs buttons
                      :on-change #(reset! comp-or-dag? %)]]]]))))
