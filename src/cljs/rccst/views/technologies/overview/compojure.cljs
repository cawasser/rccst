(ns rccst.views.technologies.overview.compojure
  (:require [rccst.views.technologies.overview.overview :as o]))



(defn overview []
  [o/overview
   "Compojure"
   "The Compojure library allows the SEGS team to easily build routes that will be used to handle HTTP requests.

It works with the Ring library described elsewhere.

Visit the Compojure Git repository [here.](https://github.com/weavejester/compojure)

Additional documentation can be found at the [wiki](https://github.com/weavejester/compojure/wiki) as well as this [site.](http://weavejester.github.io/compojure)"])