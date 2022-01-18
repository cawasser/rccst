(ns bh.rccst.views.technologies.overview.compojure
  (:require [bh.rccst.views.technologies.overview.overview :as o]))



(defn overview []
  [o/overview
   "Compojure"
   "Visit the Compojure Git repository [here.](https://github.com/weavejester/compojure)"])