(ns bh.rccst.views.technologies.clojure
  (:require [woolybear.ad.layout :as layout]
            [woolybear.packs.flex-panel :as flex]

            [bh.rccst.views.technologies.overview.ring :as ring-overview]
            [bh.rccst.views.technologies.overview.compojure :as compojure-overview]
            [bh.rccst.views.technologies.overview.component :as component-overview]
            [bh.rccst.views.technologies.overview.sente :as sente-overview]
            [bh.rccst.views.technologies.overview.transit :as transit-overview]
            [bh.rccst.views.technologies.overview.next-jdbc :as next-jdbc-overview]
            [bh.rccst.views.technologies.overview.jackdaw :as jackdaw-overview]))

(defn page []

  [layout/page
   [flex/flex-panel
    {:height "80vh"}
    [flex/flex-top
     [:div
      [:h2.has-text-info "Clojure"]
      [layout/markdown-block
       "[Clojure](https://clojure.org) is a dynamic, general-purpose programming language, combining the approachability and
       interactive development of a scripting language with an efficient and robust infrastructure for multithreaded programming.
       [Clojure](https://clojure.org) is a compiled language, yet remains completely dynamic – every feature supported by
       [Clojure](https://clojure.org) is supported at runtime. [Clojure](https://clojure.org) provides easy access to the Java
       frameworks, with optional type hints and type inference, to ensure that calls to Java can avoid reflection.

[Clojure](https://clojure.org) is a dialect of [Lisp](https://en.wikipedia.org/wiki/Lisp_(programming_language)), and shares with
Lisp the code-as-data philosophy and a powerful macro system.
[Clojure](https://clojure.org) is predominantly a functional programming language, and features a rich set of immutable,
persistent data structures. When mutable state is needed, Clojure offers a software transactional memory system and reactive
Agent system that ensure clean, correct, multithreaded designs.

> See also:
>
>[Clojure](https://clojure.org)
>[Lisp](https://en.wikipedia.org/wiki/Lisp_(programming_language))
>[Functional Programming ](https://en.wikipedia.org/wiki/Functional_programming)"]
      [layout/section "We use a number of libraries to provide critical server-side functionality:"]]]

    [ring-overview/overview]
    [compojure-overview/overview]
    [component-overview/overview]
    [sente-overview/overview]
    [transit-overview/overview]
    [next-jdbc-overview/overview]
    [jackdaw-overview/overview]]])
