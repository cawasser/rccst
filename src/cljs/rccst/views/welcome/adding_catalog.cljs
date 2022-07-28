(ns rccst.views.welcome.adding-catalog
  (:require [rccst.views.technologies.overview.overview :as o]))


(defn overview []
  [o/overview
   "Adding to the Catalog"
   "See also:

- [Atomic Design](https://bradfrost.com/blog/post/atomic-web-design/)
- [Clojure](https://clojure.org)
- [Lisp](https://en.wikipedia.org/wiki/Lisp_(programming_language))
- [Functional Programming ](https://en.wikipedia.org/wiki/Functional_programming)
- [Woolybear](https://github.com/manutter51/woolybear)
- [Re-com](https://re-com.day8.com.au)"])

