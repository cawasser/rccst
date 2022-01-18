(ns bh.rccst.views.giants
  (:require [re-frame.core :as re-frame]
            [woolybear.ad.buttons :as buttons]
            [woolybear.ad.layout :as layout]
            [woolybear.packs.flex-panel :as flex]
            [woolybear.packs.tab-panel :as tab-panel]))



(defn view []
  [layout/page {:extra-classes :rccst}
   [flex/flex-panel {:height "calc(100vh - 2rem)"}
    [flex/flex-top
     [layout/page-header {:extra-classes :rccst}
      [layout/page-title "'Giants'"]
      [layout/markdown-block "It has been said that [\"we stand on the shoulders of giants\"](https://www.phrases.org.uk/meanings/268025.html).
      The following people may not all qualify as 'giants', but we certainly have
      looked to them for insight, inspiration, guidance, and general thoughtfulness."]
      [layout/section]]]

    [layout/frame
     [:h2 "Rich Hickey"]
     [layout/markdown-block "Discuss various works and presentations"]]

    [layout/frame
     [:h2 "Stu Halloway"]
     [layout/markdown-block "Discuss the various works and presentations"]]

    [layout/frame
     [:h2 "David Nolan"]
     [layout/markdown-block "Discuss the various works and presentations"]]

    [layout/frame
     [:h2 "Dan Holmsand"]
     [layout/markdown-block "Discuss the various works and presentations"]]

    [layout/frame
     [:h2 "Bret Victor"]
     [layout/markdown-block "Discuss various works and presentations"]]

    [layout/frame
     [:h2 "James Reeves"]
     [layout/markdown-block "Discuss various works and presentations"]]

    [layout/frame
     [:h2 "Dave Martin"]
     [layout/markdown-block "Discuss various works and presentations"]]

    [layout/frame
     [:h2 "Stuart Sierra"]
     [layout/markdown-block "Discuss various works and presentations"]]

    [layout/frame
     [:h2 "Mark Nutter"]
     [layout/markdown-block "Discuss various works and presentations"]]

    [layout/frame
     [:h2 "Sean Corfield"]
     [layout/markdown-block "Discuss various works and presentations"]]

    [layout/frame
     [:h2 "Mark Bastian"]
     [layout/markdown-block "Discuss various works and presentations"]]

    [layout/frame
     [:h2 "Alex Engelberg"]
     [layout/markdown-block "Discuss various works and presentations"]]

    [layout/frame
     [:h2 "Martin Kleppmann"]
     [layout/markdown-block "Discuss various works and presentations"]]

    [layout/frame
     [:h2 "Thomas Heller"]
     [layout/markdown-block "Discuss various works and presentations"]]

    [layout/frame
     [:h2 "Eric Normand"]
     [layout/markdown-block "Discuss various works and presentations"]]

    [layout/frame
     [:h2 "Will Byrd"]
     [layout/markdown-block "Discuss various works and presentations"]]

    [layout/frame
     [:h2 "Paul deGrandis"]
     [layout/markdown-block "Discuss various works and presentations"]]]])



