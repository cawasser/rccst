(ns bh.rccst.views.catalog.utils
  "
  Misc utilities for rendering demonstrations in the RCCTS Catalog, including
  a utility for pop-up source code blocks.
  "
  (:require [clojure.string :as string]
            [taoensso.timbre :as log]
            [woolybear.ad.containers :as containers]
            [woolybear.ad.layout :as layout]
            [woolybear.ad.catalog.utils :as ad-utils]
            ["react-markdown" :as ReactMarkdown]
            [re-com.core :as rc]))



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
  [data config component]
  (log/info "config-display" data)
  [:div.demo-display
   [rc/h-box :src (rc/at)
    :size "auto"
    :children [[layout/centered {:extra-classes :is-one-third}
                data]
               [layout/centered {:extra-classes :is-one-third}
                config]
               [layout/centered {:extra-classes :is-one-third}
                component]]]])


(defn configurable-demo
  "provides a complex UI, similar to the basic Woolybear
  "
  [name & children]
  (let [[notes children] (if (string? (first children))
                           [(first children) (rest children)]
                           [nil children])
        [data config component-ui config-ui src] children]
    [:div.demo-container
     [:div.demo-name name]
     (if notes
       [:> ReactMarkdown {:source notes}]
       "")
     [config-display data config component-ui config-ui]
     [containers/spoiler {:show-label "Show Code"
                          :hide-label "Hide Code"}
      [ad-utils/code-block (string/triml (ad-utils/pps src))]]]))