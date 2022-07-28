(ns rccst.events.login
  (:require [taoensso.timbre :as log]
            [re-frame.core :as re-frame]
            [day8.re-frame.tracing :refer-macros [fn-traced]]
            [day8.re-frame.http-fx]

            [rccst.events :refer [default-header]]
            [rccst.subscriptions :as pub-sub]))


(log/info "rccst.events.login")


(re-frame/reg-event-fx
  :rccst.events/register
  (fn-traced [_ [_ id password]]
    {:http-xhrio (merge default-header
                   {:method     :post
                    :uri        "/user/register"
                    :params     {:user-id id :password password}
                    :on-request [::track-slow-request "::register"]
                    :on-success [:rccst.events/register-success]
                    :on-failure [::bad-lookup-result]})}))


(re-frame/reg-event-db
  :rccst.events/register-success
  (fn-traced [db [_ {:keys [registered uuid]}]]
    ;(log/info "::register-success" registered uuid)
    (assoc db
      :registered registered
      :uuid uuid)))


(re-frame/reg-event-fx
  :rccst.events/login
  (fn-traced [_ [_ id password]]
    {:http-xhrio (merge default-header
                   {:method     :post
                    :uri        "/user/login"
                    :params     {:user-id id :password password}
                    :on-success [:rccst.events/login-success]
                    :on-failure [::bad-lookup-result]})}))


(re-frame/reg-event-fx
  :rccst.events/login-success
  (fn-traced [{:keys [db]} [_ {:keys [logged-in user-id uuid] :as result}]]
    (log/info ":rccst.events/login-success"
      ;db
      "///" logged-in
      "///" user-id
      "///" uuid
      "///" result)
    {:dispatch [::pub-sub/start user-id]
     :db (assoc db
           :logged-in? logged-in
           :user-id user-id
           :uuid uuid)}))


(re-frame/reg-event-fx
  :rccst.events/logoff
  (fn-traced [{:keys [db]} _]
    {:fx [[:dispatch [::pub-sub/stop]]
          [:dispatch [::pub-sub/cancel-all]]]
     :db (assoc db :logged-in? false)}))



; try out the calls to the server
(comment
  (re-frame/dispatch [:rccst.events/login "test-user" "test-pwd"])

  ())