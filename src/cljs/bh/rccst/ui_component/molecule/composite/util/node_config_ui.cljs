(ns bh.rccst.ui-component.molecule.composite.util.node-config-ui
  (:require [taoensso.timbre :as log]
            [bh.rccst.ui-component.utils.helpers :as h]
            [bh.rccst.ui-component.utils.locals :as l]))


(log/info "bh.rccst.ui-component.molecule.composite.util.node-config-ui")


;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;
;
; build the correct data-entry control for each type
;
;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;
(defmulti make-config-item (fn [{:keys [type]}] type))


(defmethod make-config-item :string [{:keys [type name]}]
  [:div (str ":string - " type " - " name)])


(defmethod make-config-item :id [{:keys [type name]}]
  [:div (str ":id - " type " - " name)])


(defmethod make-config-item :port [{:keys [type name]}]
  [:div (str ":port - " type " - " name)])


(defmethod make-config-item :choices [{:keys [type name]}]
  [:div (str ":choices - " type " - " name)])


;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;
;
; build the complete panel for each type
;
;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;
(defmulti make-config-panel (fn [{:keys [type]}] type))


(defmethod make-config-panel :ui/component [{:keys [type name]}]
  (log/info "make-config-panel :ui/component" type name)
  [:div.ui-component (str type " - " name)])


(defmethod make-config-panel :source/remote [{:keys [type name]}]
  (log/info "make-config-panel :source/remote" type name)
  [:div.source-remote (str type " - " name)])


(defmethod make-config-panel :source/local [{:keys [type name]}]
  (log/info "make-config-panel :source/local" type name)
  [:div.source-local (str type " - " name)])


(defmethod make-config-panel :source/fn [{:keys [type name]}]
  (log/info "make-config-panel :source/fn" type name)
  [:div.source-fn (str type " - " name)])



(comment
  (do
    (def component-id :widget-grid-demo.grid-widget)
    (def item :topic/target-data)
    (def components   @(l/subscribe-local component-id [:blackboard :defs :source :components]))
    (def details      ((h/string->keyword item) components))
    (def detail-types (:type details)))


  (map (fn [{:keys [type name]}] {:t type :n name}) details)

  (make-config-panel details)

  ())




