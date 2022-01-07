(ns bh.rccst.ui-component.button
  (:require-macros
    [re-com.core    :refer [handler-fn]])
  (:require [taoensso.timbre :as log]
            [reagent.core :as r]
            [re-com.core :as rc]))


(defn button [label on-click]
  (let [hover? (r/atom false)]
    (fn []
      [rc/button
       :label label
       :on-click on-click
       :style {:color            "white"
               :background-color (if @hover? "#4d90fe" "#0072bb")
               :font-size        "18px"
               :font-weight      "300"
               :border           "none"
               :border-radius    "0px"
               :padding          "10px 16px"}
       :attr {:on-mouse-over (handler-fn (reset! hover? true))
              :on-mouse-out  (handler-fn (reset! hover? false))}])))



