(ns bh.rccst.views.welcome
  (:require [woolybear.ad.layout :as layout]
            [woolybear.packs.flex-panel :as flex]
            [bh.rccst.views.technologies.overview.overview :as o]))


(defn page []

  [layout/page
   [flex/flex-panel
    {:height "80vh"}
    [flex/flex-top
     [:div
      [:h2.has-text-info "Welcome!"]
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
>
>"]

      [layout/frame
       [o/overview "How to use this application"]]

      [layout/frame
       [o/overview "Rationale"
        "There are several reasons to provide a rationale for any decision made:"]]

      [layout/frame
       [o/overview "Examples"]]

      [layout/frame
       [o/overview "Installing the Library"]]

      [layout/frame
       [o/overview "Adding to the Catalog"]]]]]])


