(ns bh.rccst.api.subscribe
  (:require [clojure.tools.logging :as log]
            [compojure.api.sweet :as sweet]
            [schema.core :as s]
            [bh.rccst.api.common :as c]

            [bh.rccst.components.system :as system]))


(s/defschema DataSources
  s/Keyword)


(s/defschema SubscribeRequest
  {:user-id s/Str :data-sources DataSources})


(def subscription-handlers
  (sweet/context "/subscribe" []
    :tags ["subscribe"]

    (sweet/POST "/data-source" req
      :body [{:keys [user-id data-sources]} SubscribeRequest]
      :summary "subscribe to a data-sourec (by its keyword name)"
      (do
        (log/info "/data-source")
        (c/wrapper ((get-in @system/system [:subscriptions :subscribe]) user-id data-sources))))))
