(ns bh.rccst.views.technologies.clojurescript
  (:require [woolybear.ad.layout :as layout]
            [woolybear.packs.flex-panel :as flex]
            [bh.rccst.views.technologies.overview.hiccup :as hiccup]
            [bh.rccst.views.technologies.overview.reagent :as reagent]
            [bh.rccst.views.technologies.overview.re-frame :as re-frame]
            [bh.rccst.views.technologies.overview.re-com :as re-com]
            [bh.rccst.views.technologies.overview.woolybear :as woolybear]
            [bh.rccst.views.technologies.overview.recharts :as recharts]))


(defn page []

  [layout/page {:extra-classes :is-fluid}
   [flex/flex-panel {:extra-classes :is-fluid
                     :height "80vh"}
    [flex/flex-top {:extra-classes :is-fluid}
     [:div.is-fluid
      [:h2.has-text-info "Clojurescript"]
      [layout/markdown-block "[ClojureScript](https://github.com/clojure/clojurescript) is a compiler for Clojure that
      targets JavaScript. It emits JavaScript code which is compatible with the advanced compilation mode of the Google Closure
      optimizing compiler to provide the most powerful language for programming the web. [Clojurescript](https://clojurescript.org/)
      is a robust, practical, and fast programming language with a set of useful features that together form a simple, coherent,
      and powerful tool.

> See also:
>
>[Clojurescript](https://clojurescript.org/)"]
      [layout/section "We use a number of libraries to provide critical client-side functionality:"]]]
    [layout/frame {:extra-classes :is-fluid}
     [reagent/overview]]
    [layout/frame {:extra-classes :is-fluid}
     [hiccup/overview]]
    [layout/frame {:extra-classes :is-fluid}
     [re-frame/overview]]
    [layout/frame {:extra-classes :is-fluid}
     [re-com/overview]]
    [layout/frame {:extra-classes :is-fluid}
     [woolybear/overview]]
    [layout/frame {:extra-classes :is-fluid}
     [recharts/overview]]]])
