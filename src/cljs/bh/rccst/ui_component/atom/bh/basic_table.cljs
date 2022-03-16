(ns bh.rccst.ui-component.atom.bh.basic-table
  (:require [bh.rccst.ui-component.utils.helpers :as h]
            [bh.rccst.ui-component.atom.chart.utils.example-data :as ex]
            [re-com.core :as rc]
            [reagent.core :as r]
            [re-frame.core :as re-frame]
            [taoensso.timbre :as log]))


(def sample-data ex/tabular-data)
(def sample-meta-data ex/meta-tabular-data)


(defn- table* [data]
  (if (empty? data)

    [rc/alert-box :src (rc/at)
     :alert-type :info
     :heading "Waiting for data"]

    (let [header (keys (first data))
          body data]

      ;(log/info "basic-table/table INSIDE" header "//" body)

      [:div.table-container {:style {:width "100%" :height "100%"}}
       [:table.table.is-hoverable {:style {:width "100%" :height "100%"}}
        [:thead {:style {:position :sticky :top 0 :background :darkslategray}}
         [:tr
          (doall (for [[idx h] (map-indexed vector header)]
                   (do
                     ;(log/info "header" idx h)
                     ^{:key idx} [:th {:style {:color :white}} (str h)])))]]

        [:tbody
         (doall
           (for [[idx b] (map-indexed vector body)]
             (do
               ;(log/info "body" idx b)
               ^{:key idx} [:tr (for [key header]
                                  (do
                                    (log/info "cell" key)
                                    ^{:key key} [:td (str (get b (keyword key)))]))])))]]])))


(defn table [& {:keys [data]}]
  (let [d (h/resolve-value data)]
    [table* @d]))


(defn meta-table [& {:keys [data]}]
  (let [d (h/resolve-value data)]
    (fn []
      [table* (:data @d)])))


(def meta-data {:bh/table {:component table
                           :ports {:data :port/sink}}
                :bh/meta-table {:component meta-table
                                :ports {:data :port/sink}}})




(comment
  (def value sample-data)

  (def value [])

  (let [ret (cond
              (and (coll? value)
                (not (empty? value))
                (every? keyword? value)) (re-frame/subscribe value)
              (instance? reagent.ratom.RAtom value) value
              (instance? Atom value) value
              :else (r/atom value))]
    ;(log/info "resolve-value" value "//" ret "//" (str @ret))
    ret)

  (empty? @(h/resolve-value []))

  (def d (h/resolve-value ex/meta-tabular-data))

  (keys (first (:data @d)))
  (:data @d)

  (def body (:data @d))
  (map-indexed vector body)


  (def d (h/resolve-value ex/tabular-data))
  (keys (first @d))

  (def body @d)
  (map-indexed vector body)


  (h/resolve-value [:bh.rccst.subs/source :source/targets])
  (h/resolve-value [:bh.rccst.subs/source :source/targets] :title)

  (re-frame/subscribe [:bh.rccst.subs/source :source/targets :data])

  (get-in @re-frame.db/app-db [:sources :source/targets :data])

  (get-in @re-frame.db/app-db (reduce conj [:sources :source/targets] '(:data)))

  (or (get-in @re-frame.db/app-db (reduce conj [:sources :dummy] '(:data))) [])

  (h/resolve-value [])

  ())