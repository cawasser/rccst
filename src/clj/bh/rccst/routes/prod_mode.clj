(ns bh.rccst.routes.prod-mode
  (:require [clojure.tools.logging :as log]
            [ring.middleware.anti-forgery :refer [wrap-anti-forgery *anti-forgery-token*]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.cors :refer [wrap-cors]]
            [ring.middleware.format :refer [wrap-restful-format]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.session :refer [wrap-session]]
            [ring.middleware.session.cookie :refer [cookie-store]]
            [compojure.core :refer [GET POST]]
            [compojure.route :as route]

            [bh.rccst.api.api :as api]
            [bh.rccst.routes.csrf :refer [csrf-error-handler]]
            [bh.rccst.routes.render :refer [render]]))



(defn routes
  "
  "
  [ring-ajax-post ring-ajax-get-or-ws-handshake
   database pub-sub]
  (compojure.core/routes
    ; the websocket routes need minimal processing
    (-> (compojure.core/routes
          (GET "/chsk" req (ring-ajax-get-or-ws-handshake req))
          (POST "/chsk" req (ring-ajax-post req)))
      ;need to turn "off" the built-in anti-forgery because it returns HTML, and we need transit
      (wrap-defaults
        (assoc-in site-defaults [:security :anti-forgery] false))
      (wrap-cors :access-control-allow-origin [#".*"])
      (wrap-anti-forgery {:error-handler csrf-error-handler})
      (wrap-session {:cookie-attrs {:max-age 3600}
                     :store (cookie-store {:key (byte-array (.getBytes "ahY9poQuaghahc7I"))})}))

    ; while the "general" routes need all the transit and parsing support
    (-> (compojure.core/routes
          (GET "/" _ (render "public/index.html"))
          (route/resources "/")

          (#'api/api database pub-sub)

          (route/not-found "<h1>Page not found</h1>"))

      ;need to turn "off" the built-in anti-forgery because it returns HTML, and we need transit
      (wrap-defaults
        (assoc-in site-defaults [:security :anti-forgery] false))
      (wrap-cors :access-control-allow-origin [#".*"])
      (wrap-restful-format :formats [:transit-json :edn])
      wrap-keyword-params
      wrap-params
      (wrap-anti-forgery {:error-handler csrf-error-handler})
      (wrap-session {:cookie-attrs {:max-age 3600}
                     :store (cookie-store {:key (byte-array (.getBytes "ahY9poQuaghahc7I"))})}))))
