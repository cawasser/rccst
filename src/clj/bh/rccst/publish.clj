(ns bh.rccst.publish
  (:require [bh.rccst.system :as system]))


(defn publish-all! [msg]
  (let [uids (:any @(get-in @system/system [:socket :connected-uids]))]
    (doseq [uid uids]
      (println "publish! to user: " uid)
      ((get-in @system/system [:socket :chsk-send!]) uid msg))))



; some messages to publish! to connected clients
(comment
  (publish-all! [:publish/data-update {:id :number :value 100}])
  (publish-all! [:publish/data-update {:id :number :value 500}])
  (publish-all! [:publish/data-update {:id :number :value 999}])

  (publish-all! [:publish/data-update {:id :string :value "This is cool"}])
  (publish-all! [:publish/data-update {:id :string :value "I can change things from the repl"}])
  (publish-all! [:publish/data-update {:id :string :value "And have them pushed from the server"}])

  ())
