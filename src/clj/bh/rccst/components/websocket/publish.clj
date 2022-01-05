(ns bh.rccst.components.websocket.publish
  (:require [clojure.tools.logging :as log]
            [bh.rccst.components.system :as system]))


(defn publish-all!
  "publish the given message (msg) to _all_ connected users. This function uses the socket via
  the [system]() atom itself which is ***not*** passed as a parameter

  ---

  - msg : (typically a hash-map) the message to send to all connected users/clients

  > See also:
  >
  > [Sente](https://github.com/ptaoussanis/sente)
  "
  [msg]
  (let [uids (:any @(get-in @system/system [:socket :connected-uids]))
        send-fn (get-in @system/system [:socket :chsk-send!])]
    (doseq [uid uids]
      (log/info "publish! to user: " uid)
      (send-fn uid msg))))



; some messages to publish! to connected clients
(comment
  (publish-all! [:publish/data-update {:id :number :value 100}])
  (publish-all! [:publish/data-update {:id :number :value 500}])
  (publish-all! [:publish/data-update {:id :number :value 999}])

  (publish-all! [:publish/data-update
                 {:id :string :value "This is cool"}])
  (publish-all! [:publish/data-update
                 {:id :string :value "I can change things from the repl"}])
  (publish-all! [:publish/data-update
                 {:id :string :value "And have them pushed from the server"}])

  ())
