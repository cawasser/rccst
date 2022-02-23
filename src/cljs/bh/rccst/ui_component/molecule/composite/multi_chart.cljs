(ns bh.rccst.ui-component.molecule.composite.multi-chart
  (:require [bh.rccst.ui-component.molecule.component-layout :as layout]
            [bh.rccst.ui-component.molecule.composite :as c]
            [bh.rccst.ui-component.utils :as ui-utils]
            [bh.rccst.ui-component.atom.chart.bar-chart :as bar-chart]
            [bh.rccst.ui-component.atom.chart.line-chart :as line-chart]
            [re-com.core :as rc]
            [reagent.core :as r]
            [taoensso.timbre :as log]))



(def sample-data line-chart/sample-data)


(def source-code '[:div])


(defn my-div [x]
  [:div {:style {:border-width "1px"
                 :border-style :solid
                 :border-color :black}} x])


(defn local-config [data component-id]
  {:components [[[line-chart/component data (str component-id "/line-chart") component-id]
                 [bar-chart/component data  (str component-id "/bar-chart") component-id]]]})


(defn- config [component-id data]
  (merge ui-utils/default-pub-sub
    (local-config data component-id)))


(defn- component-panel [data component-id container-id]

  (let [subscriptions (ui-utils/build-subs component-id (local-config data component-id))]

    (fn [data component-id container-id]
      [c/composite
       :id component-id
       :components (ui-utils/resolve-sub subscriptions [:components])])))


(defn component
  ([data component-id]
   [component data component-id ""])

  ([data component-id container-id]

   (log/info "multi-chart" @data)

   (let [id (r/atom nil)]

     (fn []
       (when (nil? @id)
         (reset! id component-id)
         (ui-utils/init-widget @id (config @id data))
         (ui-utils/dispatch-local @id [:container] container-id))

       (log/info "component" @id)

       [component-panel data @id container-id]))))

