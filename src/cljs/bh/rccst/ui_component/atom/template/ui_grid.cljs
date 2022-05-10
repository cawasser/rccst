(ns bh.rccst.ui-component.atom.template.ui-grid
  (:require [bh.rccst.subs :as subs]
            [bh.rccst.ui-component.atom.layout.responsive-grid :as grid]
            [bh.rccst.ui-component.molecule.grid-container :as grid-container]
            [bh.rccst.ui-component.utils.helpers :as h]
            [re-com.core :as rc]
            [re-frame.core :as re-frame]
            [reagent.core :as r]
            [taoensso.timbre :as log]
            [woolybear.ad.layout :as layout]))


(log/info "bh.rccst.ui-component.atom.template.ui-grid")


(defn- make-widget [[id title content bk-color txt-color]]
  ;(log/info "make-widget" id "//" title)

  [:div.widget-parent {:key id}
   [:div.grid-toolbar.title-wrapper.move-cursor
    [:div {:style {:background-color bk-color
                   :color            txt-color
                   :padding          "5px"
                   :font-weight      :bold
                   :font-size        "1.1em"}}
     title]]
   [:div.widget.widget-content
    {:style         {:width       "100%"
                     :height      "90%"
                     :cursor      :default
                     :align-items :stretch
                     :display     :flex}
     :on-mouse-down #(.stopPropagation %)}
    content]])


(defn component [& {:keys [widgets container-id]}]

  (let [resolve (h/resolve-value widgets)]

    ;(log/info "component (resolve)" container-id "//" @resolve)

    (fn []
      [grid/grid
       :id container-id
       :children (doall (map make-widget (:widgets @resolve)))
       :cols 20
       :layout (:layout @resolve)])))

