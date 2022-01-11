(ns bh.rccst.views.catalog.example.markdown-block
  (:require [woolybear.ad.catalog.utils :as acu]
            [woolybear.ad.layout :as layout]
            [reagent.core :as r]))


(defn example []
  (fn []
    (acu/demo "Markdown-Block"
      "Use Markdown blocks for [Markdown](https://en.wikipedia.org/wiki/Markdown) content."
      [layout/frame
       [layout/markdown-block "## here is some markdown

  > a note/quote

  and a hyperlink: [ring](https://github.com/ring-clojure/ring)"]]

      '[layout/frame
        [layout/markdown-block "## here is some markdown

  > a note/quote

  and a hyperlink: [ring](https://github.com/ring-clojure/ring)"]])))