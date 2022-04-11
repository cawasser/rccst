(ns bh.rccst.views.molecule.welcome
  (:require [woolybear.ad.layout :as layout]
            [woolybear.packs.flex-panel :as flex]))


(defn page
  "Top-level AD Atom page"
  []

  [layout/page
   [flex/flex-panel
    {:height "80vh"}
    [flex/flex-top
     [:div
      [:h2.has-text-info "Welcome!"]
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
>[Functional Programming ](https://en.wikipedia.org/wiki/Functional_programming)"]]]]])


