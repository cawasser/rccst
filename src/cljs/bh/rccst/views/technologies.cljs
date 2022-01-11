(ns bh.rccst.views.technologies
  (:require [re-frame.core :as re-frame]
            [woolybear.ad.buttons :as buttons]
            [woolybear.ad.layout :as layout]
            [woolybear.packs.flex-panel :as flex]
            [woolybear.packs.tab-panel :as tab-panel]

            [bh.rccst.views.technologies.clojure :as tech-clj]
            [bh.rccst.views.technologies.clojurescript :as tech-cljs]
            [bh.rccst.views.technologies.systems-services :as s-s]
            [bh.rccst.views.technologies.all :as all]))


(def data-path [:tech :tab-panel])

(def init-db
  {:tab-panel (tab-panel/mk-tab-panel-data data-path :tech/server)})

(re-frame/reg-sub
  :db/tech
  (fn [db _]
    (:tech db)))

(re-frame/reg-sub
  :tech/tab-panel
  :<- [:db/tech]
  (fn [tech]
    (:tab-panel tech)))

(re-frame/reg-sub
  :tech/selected-tab
  :<- [:tech/tab-panel]
  (fn [tab-panel]
    (:value tab-panel)))


(defn page
  "Some background on the various technologies we are using"
  []
  [layout/page {:extra-classes :rccst}
   [flex/flex-panel {:height "calc(100vh - 2rem)"}
    [flex/flex-top
     [layout/page-header {:extra-classes :rccst}
      [layout/page-title "Technology Overview"]]

     [tab-panel/tab-bar {:extra-classes               :rccst
                         :subscribe-to-component-data [:tech/tab-panel]}
      [buttons/tab-button {:panel-id :tech/server} "Server-side"]
      [buttons/tab-button {:panel-id :tech/client} "Client-side"]
      [buttons/tab-button {:panel-id :tech/system} "System / Services"]
      [buttons/tab-button {:panel-id :tech/all} "All"]]]

    [layout/page-body {:extra-classes :rccst}
     [tab-panel/tab-panel {:extra-classes             :rccst
                           :subscribe-to-selected-tab [:tech/selected-tab]}

      [tab-panel/sub-panel {:panel-id :tech/server}
       [tech-clj/page]]

      [tab-panel/sub-panel {:panel-id :tech/client}
       [tech-cljs/page]]

      [tab-panel/sub-panel {:panel-id :tech/system}
       [s-s/page]]

      [tab-panel/sub-panel {:panel-id :tech/all}
       [all/page]]]]]])

