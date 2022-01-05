(ns bh.rccst.routes.render
  (:require [clojure.java.io :as io]
            [clojure.tools.logging :as log]
            [ring.middleware.anti-forgery :refer [*anti-forgery-token*]]
            [ring.util.anti-forgery :refer [anti-forgery-field]]
            [ring.util.http-response :as http]
            [selmer.parser :as parser]))


(parser/set-resource-path! (io/resource "html"))
(parser/add-tag! :csrf-field (fn [_ _] (anti-forgery-field)))


(defn render
  "renders the HTML template located relative to resources/html, using Selmer to substitute values
  (given in the params) in place of tags embedded in the html template

  ---

  - template - (string) the 'raw' html page, typically with Selmer tags embedded
  - params - (hash-map, optional) the mapping from 'tags' to 'values' for substitution

  > See also:
  >
  > [Http-response](https://github.com/metosin/ring-http-response)
  > [Selmer](https://github.com/yogthos/Selmer)
  "
  [template & [params]]
  (http/content-type
    (http/ok
      (parser/render-file
        template
        (assoc params
          :page template
          :csrf-token *anti-forgery-token*)))
    "text/html; charset=utf-8"))