(ns bh.rccst.views.atom.example.container.v-scroll-panel
  (:require [woolybear.ad.catalog.utils :as acu]
            [woolybear.ad.layout :as layout]
            [woolybear.ad.containers :as containers]))


(defn example []
  (acu/demo "Vertical scroll pane"
    "Use a `v-scroll-pane` to wrap oversized content inside a container with a vertical
    scroll bar with a fixed height specified by the :height option. Takes the standard
    `:extra-classes` and `:subscribe-to-classes` option. If the v-scroll-pane contains a
    component of type scroll-pane-header, it will be displayed above the scrolling
    portion. Similarly, a scroll-pane-footer will be displayed below.

> Note that the `:height` parameter applies only to the scrolling portion of the
> container; any header and/or footer will add additional height to the v-scroll-pane
> as a whole."
    [containers/v-scroll-pane {:height "12rem"}
     [containers/scroll-pane-header "This line does not scroll."]
     [containers/scroll-pane-footer "This line also does not scroll"]
     [layout/text-block acu/lorem]
     [layout/text-block acu/lorem]
     [layout/text-block acu/lorem]]

    '[containers/v-scroll-pane {:height "12rem"}
      [containers/scroll-pane-header "This line does not scroll."]
      [containers/scroll-pane-footer "This line also does not scroll"]
      [layout/text-block acu/lorem]
      [layout/text-block acu/lorem]
      [layout/text-block acu/lorem]]))
