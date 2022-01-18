(ns bh.rccst.views.technologies.overview.ring
  (:require [bh.rccst.views.technologies.overview.overview :as o]))


(defn overview []
  [o/overview
   "Ring"
   "Ring is used by the SEGS team to handle HTTP requests and responses between server and web client.

 Visit the Ring Git repository [here.](https://github.com/ring-clojure/ring)

 Additional documentation can be found at the [wiki](https://github.com/ring-clojure/ring/wiki) as well as this [site.](https://ring-clojure.github.io/ring/)"])
