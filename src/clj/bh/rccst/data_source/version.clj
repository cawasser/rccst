(ns bh.rccst.data-source.version
  (:require [clojure.tools.logging :as log]

            [bh.rccst.version :as v]))


(defn get-version
  "returns the version number, as generated by the Metav tool.

  returns: [[VersionResponse]]

  > See also:
  >
  > [Metav](https://github.com/jgrodziski/metav)
  "

  []
  (log/info "get-version")
  {:version v/version :build-date v/generated-at})

