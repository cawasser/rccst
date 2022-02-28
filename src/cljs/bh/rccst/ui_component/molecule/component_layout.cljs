(ns bh.rccst.ui-component.molecule.component-layout
  "creates a single top-level Reagent component, organized by the provided DSL"
  (:require [re-com.core :as rc]))



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; THE DSL
;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn empty-space
  "provides a placeholder, empty space, gap to help keep the layout organized into rows and columns
  "
  []
  [rc/gap :src (rc/at) :size "1"])


; layout is managed using a single data-structure-based DSL

; for example, let's put 2 :divs side-by-side (horizontally)
(def horz-divs [[[:div "1"] [:div "2"]]])

; this would be turned into:
(def horz-divs-layout [rc/v-box
                       :gap "5px"
                       :children [[rc/h-box
                                   :gap "5px"
                                   :children [[:div "1"]
                                              [:div "2"]]]]])

; we'll always make the outermost element be a v-box, so we can have as many rows (h-boxes) as we need


; to arrange the :div vertically, we would do:
(def vert-divs [[[:div "1"]]
                [[:div "2"]]])

; this would be turned into:
(def vert-divs-layout [rc/v-box
                       :gap "5px"
                       :children [[rc/h-box
                                   :gap "5px"
                                   :children [[:div "1"]]]
                                  [rc/h-box
                                   :gap "5px"
                                   :children [[:div "2"]]]]])

; yes, there are redundant h-boxes, but that keep the login simple.

; let's try something more complex, like where we want to leave a gap
(def complex-divs [[[:div "1"] [empty-space] [:div "2"]]
                   [[empty-space] [:div "3"] [:div "4"]]])


;   |   "1"     |             |   "2"     |
;   |           |    "3"      |    "4"    |

; we have 3 positions in the top row, but only 2 in the bottom row, so we need a placeholder, we'll call it "empty"
; to fill up the empty space

(def complex-divs-layout [rc/v-box
                          :gap "5px"
                          :children [[rc/h-box
                                      :gap "5px"
                                      :children [[:div "1"] [empty-space] [:div "2"]]]
                                     [rc/h-box
                                      :gap "5px"
                                      :children [[empty-space] [:div "3"] [:div "4"]]]]])


; TODO: how do we get all the item in one "column" to be the same width? in one row to be the same height?



(defn layout [components]
  [rc/v-box
   :gap "5px"
   :height "400px"
   :children (doall
               (map (fn [row]
                      [rc/h-box
                       :gap "5px"
                       :children row])
                 components))])




; let's try out the function
(comment
  (= (layout horz-divs) horz-divs-layout)
  (= (layout vert-divs) vert-divs-layout)
  (= (layout complex-divs) complex-divs-layout)

  ())




(comment
  ; more thinking about components and layout, specifically how do we wire these things together?
  ;
  ; like
  ;      how does the DATA get in?
  ;      how do the components exchange state?



  ())