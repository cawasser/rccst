(ns bh.rccst.views.technologies.overview.transit
  (:require [bh.rccst.views.technologies.overview.overview :as o]))

(defn overview []
      [o/overview
       "Transit"
       "The Transit library is used by the team to move data between applications.  Clojure types are mapped to a standard format
by Transit on the writer side, and mapped back into Clojure types on the reader side.

Visit the Transit Git repository [here.](https://github.com/cognitect/transit-clj)

The Transit API documentation can be found [here.](https://cognitect.github.io/transit-clj/)"])

