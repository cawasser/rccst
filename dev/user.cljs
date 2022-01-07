(ns cljs.user
  "Commonly used symbols for easy access in the ClojureScript REPL during
  development."
  (:require
    [cljs.repl :refer (Error->map apropos dir doc error->str ex-str ex-triage
                       find-doc print-doc pst source)]
    [clojure.pprint :refer (pprint)]
    [clojure.string :as str]
    [re-frame.core :as re-frame]
    [bh.rccst.events :as events]))


(re-frame/dispatch [::events/login-success
                    {:logged-in true
                     :user-id "testing"
                     :uuid "testing-uuid"}])