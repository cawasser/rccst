(ns build
  (:refer-clojure :exclude [test])
  (:require [clojure.tools.build.api :as b]
            [org.corfield.build :as bb]))

(def lib 'blackHammer/rccst)
(def version (format "1.0.%s" (b/git-count-revs nil)))
(def main 'bh.rccst.core)


(defn uber "Build the uberjar." [opts]
      (-> opts
          (assoc :lib lib :main main) ; :version version
          (bb/clean)
          (bb/uber)))