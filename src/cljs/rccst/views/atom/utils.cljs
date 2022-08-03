(ns rccst.views.atom.utils
  "
  Misc utilities for rendering demonstrations in the RCCTS Catalog, including
  a utility for pop-up source code blocks.
  "
  (:require [bh.ui-component.atom.bh.navbar :as navbar]
            [bh.ui-component.utils :as ui-utils]
            [clojure.string :as string]
            [re-com.core :as rc]
            ["react-markdown" :as ReactMarkdown]
            [woolybear.ad.catalog.utils :as ad-utils]
            [woolybear.ad.catalog.utils :as acu]
            [woolybear.ad.containers :as containers]
            [woolybear.ad.layout :as layout]
            [woolybear.ad.layout :as layout]
            [woolybear.packs.tab-panel :as tab-panel]))


(defn- chart-config [[config data panel tab] data-panel config-panel]
  ;(log/info "chart-config" config data panel tab)
  (let [data-or-config [[config "config"]
                        [data "data"]]]
    [:div.chart-config {:style {:width "100%" :height "100%"}}
     [navbar/navbar data-or-config [panel]]

     [rc/scroller
      :v-scroll :auto
      :height "500px"
      :child [tab-panel/tab-panel {:extra-classes             :is-fluid
                                   :subscribe-to-selected-tab [tab]}

              [tab-panel/sub-panel {:panel-id config}
               config-panel]

              [tab-panel/sub-panel {:panel-id data}
               data-panel]]]]))


(defn- config-display
  "this is the workhorse function. It build a side-by-side UI with the config to the left
  and the component to the right. The user can change values in the config (checkboxes,
  sliders, etc.) and the component will update on the fly.

  ---

  - component : (hiccup) the UI component we want to demonstrate for the user
  - config : (hiccup) a UI component that provides options to the User for changing the config
  of the corresponding UI component

  > See also:
  >
  > [Re-com](https://github.com/Day8/re-com)
  > [re-com demo](https://re-com.day8.com.au/#/h-box)
  > [demo source](https://github.com/day8/re-com/blob/master/src/re_demo/h_box.cljs)
  "
  [chart-events data-panel config-panel component]
  ;(log/info "config-display" data)
  [:div.demo-display
   [rc/h-box :src (rc/at)
    :width "100%"
    :height "100%"
    ;:size "auto"
    :align :center
    :children [[layout/centered {:extra-classes :is-one-third}
                [chart-config chart-events data-panel config-panel]]
               [layout/centered {:extra-classes :is-one-third}
                component]]]])


(defn configurable-demo
  "provides a complex UI, similar to the basic Woolybear
  "
  [name & children]
  (let [[notes children] (if (string? (first children))
                           [(first children) (rest children)]
                           [nil children])
        [chart-events data-panel config-panel component src] children]

    [:div.demo-container
     [:div.demo-name name]
     (if notes
       [:> ReactMarkdown {:source notes}]
       "")
     [config-display chart-events data-panel config-panel component]
     [containers/spoiler {:show-label "Show Code"
                          :hide-label "Hide Code"}
      [ad-utils/code-block (string/triml (ad-utils/pps src))]]]))



(defn component-example [& {:keys [title
                                   description
                                   component
                                   component-id
                                   source-code
                                   extra-classes] :as params}]

  (ui-utils/init-container component-id)

  (let [input-params      (dissoc params :title :sample-data :description :extra-classes :source-code :component)
        paramed-component (reduce into [component] (seq input-params))]

    ;(log/info "component-example" params
    ;"////" input-params
    ;"////" paramed-component

    (acu/demo
      title
      description
      [layout/centered (or extra-classes {})
       ;;
       ;; NOTE: the :height MUST be specified here since the ResponsiveContainer down in bowels of the chart needs a height
       ;; in order to actually draw the Recharts components. just saying "100%" doesn't work, since the
       ;; that really means "be as big as you need" and ResponsiveContainer then doesn't know what to do.
       ;;
       [:div.component-example {:style {:width "100%" :height "700px"}}
        paramed-component]]

      source-code)))


(defn example [& {:keys [title container-id description
                         data config
                         data-panel config-panel component-panel
                         source-code] :as params}]

  ;(log/info "example" params)

  (ui-utils/init-container-locals container-id config)

  (let [config-key   (keyword container-id "config")
        data-key     (keyword container-id "data")
        tab-panel    (keyword container-id "tab-panel")
        selected-tab (keyword container-id "tab-panel.value")]

    (configurable-demo
      title
      description
      [config-key data-key tab-panel selected-tab]
      [data-panel data]
      [config-panel data container-id]
      ;[:div.configurable-demo-component {:style {:width "100%" :height "100%"}} ;{:width "1500px" :height "700px"}}
      [component-panel data container-id]
      source-code)))
