(ns ^:dev/once rccst.app
  (:require [cljs.spec.alpha :as s]
            [expound.alpha :as expound]
            [devtools.core :as devtools]
            [taoensso.timbre :as log]
            [rccst.core :as core]))

(extend-protocol IPrintWithWriter
  js/Symbol
  (-pr-writer [sym writer _]
    (-write writer (str "\"" (.toString sym) "\""))))

(set! s/*explain-out* expound/printer)

(enable-console-print!)

(devtools/install!)

(log/info "rccst.app")

(core/init)
