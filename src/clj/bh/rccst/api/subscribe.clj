(ns bh.rccst.api.subscribe
  (:require [clojure.tools.logging :as log]
            [compojure.api.sweet :as sweet]
            [schema.core :as s]
            [bh.rccst.api.common :as c]

            [bh.rccst.components.system :as system]))


(s/defschema DataSources
  "vector of Clojure [keyword](https://clojure.org/reference/data_structures#Keywords), each
   of which uniquely identifies a specific data-source in the system.

  "
  #{s/Keyword})


(s/defschema SubscribeRequest
  "data required to subscribing to a data-source.

  - :user-id string identifying the user that made the subscription request
  - :data-sources - the [[DataSource]] the user wants to receive _push_ updates from
  "
  {:user-id s/Str :data-sources DataSources})


(defn subscription-handlers
  "handler for users subscribing via the `/data/source` URL.

  ---

  - subscriptions - the Subscriptions Component, which manages subscriptions over the Websocket

  > See also:
  >
  > [Compojure](https://github.com/weavejester/compojure)
  > [compojure.sweet](https://github.com/metosin/compojure-api)
  "
  [pub-sub]
  (log/info "subscription-handlers" pub-sub)
  (let [subscribe (:subscribe pub-sub)] ; we need just the (subscribe) function
    (sweet/context "/subscribe" []
      :tags ["subscribe"]

      (sweet/POST "/data-source" req
        :body [{:keys [user-id data-sources]} SubscribeRequest]
        :summary "subscribe to a data-source (by its keyword name)"
        (do
          (log/info "/data-source")
          (c/wrapper (subscribe user-id data-sources)))))))
