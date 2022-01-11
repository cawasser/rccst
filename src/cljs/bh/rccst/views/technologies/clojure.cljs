(ns bh.rccst.views.technologies.clojure
  (:require [woolybear.ad.layout :as layout]
            [woolybear.packs.flex-panel :as flex]
            ["react-markdown" :as ReactMarkdown]

            [bh.rccst.views.technologies.overview.ring :as ring-overview]))


(defn page []

  [layout/page
   [flex/flex-panel
    {:height "80vh"}
    [flex/flex-top
     [:div
      [:h2.has-text-info "Clojure"]
      [layout/text-block "Discuss Clojure here"]
      [layout/text-block "We use a number of libraries to provide critical server-side functionality:"]]]


    [ring-overview/overview]

    [layout/frame
     [:h2 "Compojure"]
     [layout/text-block "See also:"]]
    [layout/frame
     [:h2 "Component"]
     [layout/text-block "Discuss Component here"]]
    [layout/frame
     [:h2 "Sente"]
     [layout/text-block "Discuss Sente here"]]
    [layout/frame
     [:h2 "Transit"]
     [layout/text-block "Discuss Transit here"]]
    [layout/frame
     [:h2 "next.jdbc"]
     [layout/text-block "Discuss next.jdbc here"]]]])
