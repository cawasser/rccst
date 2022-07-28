(ns rccst.views.technologies.overview.jackdaw
  (:require [rccst.views.technologies.overview.overview :as o]))

(defn overview []
      [o/overview
       "Jackdaw"
       "The team uses the Jackdaw library's APIs to interact with Apache Kafka. The APIs provide functions to create
and list topics, produce and consume records, and write stream processing applications.

Visit the Jackdaw Git repository [here.](https://github.com/FundingCircle/jackdaw)

Additional documentation can be found [here.](https://cljdoc.org/d/fundingcircle/jackdaw)"])


