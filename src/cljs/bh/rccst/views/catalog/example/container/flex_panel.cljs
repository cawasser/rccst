(ns bh.rccst.views.catalog.example.container.flex-panel
  (:require [woolybear.ad.catalog.utils :as acu]
            [woolybear.ad.layout :as layout]
            [woolybear.packs.flex-panel :as flex]))

(defn example []
  (acu/demo "Flex panel"
    "A flex panel is designed primarily to implement a container that fills the
    entire viewport, with a scrolling content section between an (optional) flex-top
    and/or flex-bottom component. Specify the `:height` as `\"100vh\"` to fill the entire
    screen, or use any valid CSS height specification to set a different size."
    [flex/flex-panel {:height "33vh"}
     [flex/flex-top "This line should be fixed to the top of the flex panel."]
     [flex/flex-bottom "This line should be fixed to the bottom of the flex panel."]
     [layout/text-block acu/lorem]
     [layout/text-block acu/lorem]
     [layout/text-block acu/lorem]]
    '[flex/flex-panel {:height "33vh"}
      [flex/flex-top "This line should be fixed to the top of the flex panel."]
      [flex/flex-bottom "This line should be fixed to the bottom of the flex panel."]
      [layout/text-block acu/lorem]
      [layout/text-block acu/lorem]
      [layout/text-block acu/lorem]]))
