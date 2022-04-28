(ns bh.rccst.views.welcome
  (:require [woolybear.ad.layout :as layout]
            [re-com.core :as rc]
            [woolybear.packs.flex-panel :as flex]
            [bh.rccst.views.welcome.how-to-use :as how-to-use]
            [bh.rccst.views.welcome.rationale :as rationale]
            [bh.rccst.views.welcome.examples :as examples]
            [bh.rccst.views.welcome.installing :as installing]
            [bh.rccst.views.welcome.adding-catalog :as adding-catalog]))



(defn page []

  [layout/page {:extra-classes :is-fluid}

   [layout/page-header {:extra-classes :is-fluid}
    [:h1.has-text-info "Welcome"]
    [layout/markdown-block
     "This application documents the design and implementation of a UI Design System using the principles of
      [Atomic Design](https://bradfrost.com/blog/post/atomic-web-design/) by Brad Frost.

> See also:
>
> [Atomic Design](https://bradfrost.com/blog/post/atomic-web-design/)
> [Clojure](https://clojure.org)
> [Lisp](https://en.wikipedia.org/wiki/Lisp_(programming_language))
> [Functional Programming ](https://en.wikipedia.org/wiki/Functional_programming)
>
> [Woolybear](https://github.com/manutter51/woolybear)
> [Re-com](https://re-com.day8.com.au)
> [Astro UXDS](https://www.astrouxds.com)
> [Storybook](https://storybook.js.org)
> [Devcards](https://github.com/bhauman/devcards)
> [Carbon Design System](https://www.carbondesignsystem.com)
"]
    [rc/gap :size "10px"]]

   [flex/flex-panel {:extra-classes :is-fluid
                     :height "70vh"}

    [layout/page-body {:extra-classes :is-fluid}
     [layout/frame {:extra-classes :is-fluid}
      [how-to-use/overview]]

     [layout/frame {:extra-classes :is-fluid}
      [rationale/overview]]

     [layout/frame {:extra-classes :is-fluid}
      [examples/overview]]

     [layout/frame {:extra-classes :is-fluid}
      [installing/overview "Installing the Library"]]

     [layout/frame {:extra-classes :is-fluid}
      [adding-catalog/overview]]]]])


