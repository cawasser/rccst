(ns bh.rccst.ui-component.molecule.composite
  "provides a \"container\" to hold and organize other atoms and molecules"
  (:require [bh.rccst.ui-component.molecule.component-layout :as layout]
            [bh.rccst.ui-component.utils :as ui-utils]
            [day8.re-frame.tracing :refer-macros [fn-traced]]
            [re-com.core :as rc]

            [re-frame.core :as re-frame]
            [taoensso.timbre :as log]))


(re-frame/reg-event-db
  ::add-component
  (fn-traced [db [_ id component]]
    (update-in db [:widgets (keyword id) :components] (partial apply conj) component)))


(defn config [id]
  {:components []})


(defn composite
  "make the composite:

  1. sets up the app-db to hold the local state, specifically the `:components`
  2. stores the children as `:components` in local state
  2. organizes the children onto the display

  ---

  - id : (string) unique id for this composite
  - components : (vector) vector of hiccup components (atoms or molecules) to manage and display

  Returns - (hiccup) a single reagent component (equivalent to a `:div`)
  "
  [& {:keys [id components]}]

  [layout/layout components])




(comment
  (def components [[[:div "1"] [empty] [:div "2"]]
                   [[empty] [:div "3"] [:div "4"]]])
  (layout/layout components)

  ())

; make sure the event handler preserves the ordering of the components in the DSL
(comment
  (def id "dummy")
  (def c {:widgets {id (config id)}})

  ((partial apply conj) [] [[:a :b] [:c :d]])

  ; using DSL (in progress, see component-layout
  (def components [[[:div "1"]]
                   [[:div "2"]]])
  (def components [[[:div "1"] [empty] [:div "2"]]
                   [[:div "3"] [:div "4"]]])

  (update-in c [:widgets id :components] (partial apply conj) components)

  ())
