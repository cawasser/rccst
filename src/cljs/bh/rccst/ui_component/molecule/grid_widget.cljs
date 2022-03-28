(ns bh.rccst.ui-component.molecule.grid-widget
  (:require [bh.rccst.ui-component.atom.layout.grid :as grid]
            [bh.rccst.ui-component.molecule.composite :as composite]
            [bh.rccst.ui-component.molecule.composite.util.digraph :as dig]
            [bh.rccst.ui-component.molecule.composite.util.signals :as sig]
            [bh.rccst.ui-component.molecule.composite.util.ui :as ui]
            [bh.rccst.ui-component.utils :as ui-utils]
            [bh.rccst.ui-component.utils.locals :as locals]
            [loom.graph :as lg]
            [re-com.core :as rc]
            [reagent.core :as r]
            [taoensso.timbre :as log]))


(log/info "bh.rccst.ui-component.molecule.grid-widget")


(defn- config
  "set up the local config keys, specifically we want the :layout key, so we can
  track updates to the layout should the user drag/resize any of the internal
  components.

  the component-panel will subscribe to this 'local' using (locals/subscribe-local ...) and
  dispatch updates (via on-layout-update) using (locals/dispatch-local ...)
  "
  [full-config]
  {:blackboard {:defs {:source full-config
                       :dag    {:open-details ""}}}
   :container  ""
   :layout     (:grid-layout full-config)})


(defn- wrap-component [[id component]]

  (log/info "wrap-component" id)

  [:div.widget-parent {:key id}
   [:div.grid-toolbar.title-wrapper.move-cursor (name id)]
   [:div.widget.widget-content
    {:style         {;:width       "100%"
                     ;:height      "100%"
                     :cursor      :default
                     :align-items :stretch
                     :display     :flex}
     :on-mouse-down #(.stopPropagation %)}
    component]])


(defn- on-width-update [width margin cols padding]
  "
  ---

  - width : (number) new width of the container
  - margin : (vector) margin [left? right?]
  - cols : (number) number of columns
  - padding : (vector) padding [left? right?]
  "

  (log/info "on-width-update" width "//" margin "//" cols "//" padding)
  ())


(defn- on-layout-change [component-id new-layout all-layouts]
  (let [new-layout*  (js->clj new-layout :keywordize-keys true)
        all-layouts* (js->clj all-layouts :keywordize-keys true)
        fst          (first new-layout*)]

    (log/info "on-layout-change" new-layout*
      "//" all-layouts*
      "//" (keys all-layouts*))

    (when (and
            (not (empty? new-layout*))
            (<= 1 (count new-layout*))
            (not= (:i fst) "null"))
      (let [cooked (map #(zipmap '(:i :x :y :w :h) %)
                     (map (juxt :i :x :y :w :h) new-layout*))]
        (locals/dispatch-local component-id [:layout] cooked)))))

;[:blackboard :defs :source :grid-layout]

(defn- component-panel [& {:keys [configuration component-id container-id]}]
  ;(log/info "component-panel" component-id
  ;  "//" (keys configuration)
  ;  "// dummy-layout" dummy-layout
  ;  "// :components" (:components configuration)
  ;  "// process-components" (into {}
  ;                            (sig/process-components
  ;                              configuration :ui/component
  ;                              composite/meta-data-registry component-id)))

  (let [layout           (locals/subscribe-local component-id [:layout])
        component-lookup (into {}
                           (sig/process-components
                             configuration :ui/component
                             composite/meta-data-registry component-id))

        ; 1. build UI components (with subscription/event signals against the blackboard or remotes)
        composed-ui      (map wrap-component component-lookup)]

    ;(log/info "component-panel INNER" component-id
    ;  "//" @layout
    ;  "//" composed-ui)

    (fn []
      ; 5. return the composed component layout!
      [:div.grid-container {:style {:width "100%" :height "100%"}}
       [grid/grid
        :id component-id
        :class "layout"
        :children composed-ui
        :layout layout
        :cols (r/atom 12)
        :width 900
        :rowHeight 25
        :layoutFn #(on-layout-change component-id %1 %2)
        :widthFn #(on-width-update %1 %2 %3 %4)]])))


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
        (ui-utils/init-widget @id (config full-config))
        (ui-utils/dispatch-local @id [:container] container-id)
        (ui/prep-environment full-config @id composite/meta-data-registry))

      (let [buttons [{:id :component :label [:i {:class "zmdi zmdi-view-compact"}]}
                     {:id :dag :label [:i {:class "zmdi zmdi-share"}]}
                     {:id :definition :label [:i {:class "zmdi zmdi-format-subject"}]}]]

        [:div.box {:style {:width "1000px" :height "800px"}}
         [rc/v-box :src (rc/at)
          :justify :end
          :width "100%"
          :height "100%"
          :gap "5px"
          :children [[rc/h-box :src (rc/at)
                      :justify :end
                      :children [[rc/horizontal-bar-tabs
                                  :model comp-or-dag?
                                  :tabs buttons
                                  :on-change #(reset! comp-or-dag? %)]]]
                     (condp = @comp-or-dag?
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
                                 :body "There is a problem with this component."])]]]))))
