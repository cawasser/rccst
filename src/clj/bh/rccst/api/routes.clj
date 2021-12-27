(ns bh.rccst.api.routes
  (:require [clojure.java.io :as io]
            [clojure.tools.logging :as log]
            [ring.middleware.anti-forgery :refer [wrap-anti-forgery *anti-forgery-token*]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.cors :refer [wrap-cors]]
            [ring.middleware.format :refer [wrap-restful-format]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.session :refer [wrap-session]]
            [ring.middleware.session.cookie :refer [cookie-store]]
            [compojure.core :refer [GET POST]]
            [ring.util.anti-forgery :refer [anti-forgery-field]]
            [ring.util.http-response :as http]
            [compojure.route :as route]
            [selmer.parser :as parser]

            [bh.rccst.api.api :as api]))


(parser/set-resource-path! (io/resource "html"))
(parser/add-tag! :csrf-field (fn [_ _] (anti-forgery-field)))


(defn csrf-error-handler [request]
  (http/content-type
    (http/forbidden
      {:error-text "Invalid CSRF Token"
       :token (get-in request [:headers "x-csrf-token"])})
    "application/transit+json"))


(defn render
  "renders the HTML template located relative to resources/html"
  [template & [params]]
  (http/content-type
    (http/ok
      (parser/render-file
        template
        (assoc params
          :page template
          :csrf-token *anti-forgery-token*)))
    "text/html; charset=utf-8"))


(defn routes [{:keys [ring-ajax-post ring-ajax-get-or-ws-handshake]}]
  (log/info "setting up the routes")

  (compojure.core/routes
    ; the websocket routes need minimal processing
    (-> (compojure.core/routes
          (GET "/chsk" req (ring-ajax-get-or-ws-handshake req))
          (POST "/chsk" req (ring-ajax-post req)))
      (wrap-defaults
        ;site-defaults)
        (assoc-in site-defaults [:security :anti-forgery] false))
      (wrap-cors :access-control-allow-origin [#".*"])
      (wrap-session {:cookie-attrs {:max-age 3600}
                     :store (cookie-store {:key (byte-array (.getBytes "ahY9poQuaghahc7I"))})}))

    ; while the "general' routes need all the transit and parsing support
    (-> (compojure.core/routes
          (GET "/" _ (render "public/index.html"))
          (route/resources "/")

          api/api

          (route/not-found "<h1>Page not found</h1>"))

      (wrap-defaults
        ;site-defaults)
        (assoc-in site-defaults [:security :anti-forgery] false))
      (wrap-cors :access-control-allow-origin [#".*"])
      (wrap-restful-format :formats [:transit-json :edn])
      wrap-keyword-params
      wrap-params
      (wrap-session {:cookie-attrs {:max-age 3600}
                     :store (cookie-store {:key (byte-array (.getBytes "ahY9poQuaghahc7I"))})}))))


(comment
  (routes {:ring-ajax-post                nil
           :ring-ajax-get-or-ws-handshake nil})

  (anti-forgery-field)


  ())

