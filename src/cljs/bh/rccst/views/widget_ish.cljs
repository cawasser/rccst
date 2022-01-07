(ns bh.rccst.views.widget-ish
  (:require [re-frame.core :as re-frame]
            [re-com.core :as rc]

            [bh.rccst.subs :as subs]
            [bh.rccst.events :as events]
            [bh.rccst.ui-component.button :as button]
            [bh.rccst.ui-component.labeled-field :as lf]))


(defn- lookup-control
  "this is an example of a 'local' value, specifically, this ui-component displays data that
  is 'local' to the given widget-isn (identified by the `uuid`). This is on contrast to 'global'
  data, such as `[:sources :number]` and `[:sources :string]`.

  In summary, all widgets that subscribe to `[:sources :number]` and `[:sources :string]` will
  always show the same value, which will update every time the server publishes a new version, but
  each widget will have it ***own*** instance of `:lookup`, and these are _not_ necessary the same
  across multiple widget-ish's.

  ---

  - uuid : (string) the uuid that uniquely identified this widget-ish, distinct form _all_ the others.

  > See also:
  >
  > [re-com](https://github.com/Day8/re-com)
  "
  [uuid]
  (let [result (re-frame/subscribe [::subs/lookup uuid])
        error (re-frame/subscribe [::subs/lookup-error])]
    (fn []
      [rc/h-box :src (rc/at)
       :align :stretch
       :gap "2px"
       :width "100%"
       :children [[rc/v-box :src (rc/at)
                   :width "80%"
                   :children [[lf/labeled-area "Result:" @result 2]
                              [lf/labeled-area "Error:" @error]]]
                  [button/button "Lookup" #(re-frame/dispatch [::events/lookup uuid])]]])))



(defn view
  "returns a complex (hence 'widget-ish') view that lets the user subscribe to different data-sources
  and shows the most recent value of each subscribed source.

  ---

  - uuid : (string) uuid that uniquely identifies this widget-ish in the system

  > See also:
  >
  > [re-com](https://github.com/Day8/re-com)
  "
  [uuid]
  (let [layout (re-frame/dispatch [::events/layout uuid])
        number (re-frame/subscribe [::subs/source :number])
        s (re-frame/subscribe [::subs/source :string])
        error (re-frame/subscribe [::subs/subscribe-error uuid])]

    (re-frame/dispatch [::events/subscribe-to #{:number :string}])

    (fn []
      [rc/v-box :src (rc/at)
       :width "30%"
       :style {:border "solid" :border-width "2px"}
       :children [[rc/title
                   :level :level2
                   :style {:color :white :background-color :orange
                           :height "2em"}
                   :label uuid]

                  [rc/v-box :src (rc/at)
                   :width "100%"
                   :gap "2px"
                   :padding "5px"
                   :children [[lookup-control uuid]

                              [rc/line :size  "2px" :color "orange"]

                              [lf/labeled-field "Number:" @number]
                              [lf/labeled-field "String:" @s]

                              [rc/line :size  "2px" :color "orange"]

                              [lf/labeled-area "Error:" @error]]]]])))

