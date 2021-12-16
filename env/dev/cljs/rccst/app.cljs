(ns ^:dev/once rccst.app
  (:require
    [bh.rccst.core :as core]))

;(extend-protocol IPrintWithWriter
;  js/Symbol
;  (-pr-writer [sym writer _]
;    (-write writer (str "\"" (.toString sym) "\""))))
;
;(set! s/*explain-out* expound/printer)
;
;(enable-console-print!)
;
;(devtools/install!)

(println "rccst.app")

(core/init)
