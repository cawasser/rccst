(ns bh.rccst.views.atom.example.container.navbar
  (:require [woolybear.ad.catalog.utils :as acu]
            [woolybear.ad.layout :as layout]
            [woolybear.packs.tab-panel :as tab-panel]
            [re-frame.core :as re-frame]

            [bh.rccst.events :as events]
            [bh.rccst.ui-component.navbar :as navbar]))


(def data-path [:navbar-example :tab-panel])


(def init-db
  {:tab-panel (tab-panel/mk-tab-panel-data data-path :demo-navbar/one)})


(re-frame/reg-sub
  :db/navbar-example
  (fn [db _]
    (:navbar-example db)))


(re-frame/reg-sub
  :navbar-example/tab-panel
  :<- [:db/navbar-example]
  (fn [navbar]
    (:tab-panel navbar)))


(re-frame/reg-sub
  :navbar-example/value
  :<- [:navbar-example/tab-panel]
  (fn [tab-panel]
    (:value tab-panel)))


(defn example []
  (re-frame/dispatch-sync [::events/init-locals :navbar-example init-db])

  (acu/demo "Navbar"
    "A Navbar, composed of individual buttons that dispatch events for controlling tab-panels

> See also:
>
> [Woolybear/tab-bar](https://github.com/cawasser/woolybear/blob/a7f820dfb2f51636122d56d1500baefe5733eb25/src/cljs/woolybear/packs/tab_panel.cljs#L61)
>
> [Woolybear/tab-panel](https://github.com/cawasser/woolybear/blob/a7f820dfb2f51636122d56d1500baefe5733eb25/src/cljs/woolybear/packs/tab_panel.cljs#L133)
    "
    [layout/centered {:extra-classes :width-50}
     [navbar/navbar [[:demo-navbar/one "One"]
                     [:demo-navbar/two "Two"]
                     [:demo-navbar/three "Three"]
                     [:demo-navbar/four "Four"]]
      [:navbar-example/tab-panel]]

     [tab-panel/tab-panel {:extra-classes             :is-fluid
                           :subscribe-to-selected-tab [:navbar-example/value]}

      [tab-panel/sub-panel {:panel-id :demo-navbar/one}
       [:div "Panel One"]]
      [tab-panel/sub-panel {:panel-id :demo-navbar/two}
       [:div "Panel Two"]]
      [tab-panel/sub-panel {:panel-id :demo-navbar/three}
       [:div "Panel Three"]]
      [tab-panel/sub-panel {:panel-id :demo-navbar/four}
       [:div "Panel Four"]]]]

    '[layout/centered {:extra-classes :width-50}
      [navbar/navbar [[:demo-navbar/one "One"]
                      [:demo-navbar/two "Two"]
                      [:demo-navbar/three "Three"]
                      [:demo-navbar/four "Four"]]
       [:navbar-example/tab-panel]]
      [tab-panel/tab-panel {:extra-classes             :is-fluid
                            :subscribe-to-selected-tab [:navbar-example/value]}

       [tab-panel/sub-panel {:panel-id :demo-navbar/one}
        [:div "Panel One"]]
       [tab-panel/sub-panel {:panel-id :demo-navbar/two}
        [:div "Panel Two"]]
       [tab-panel/sub-panel {:panel-id :demo-navbar/three}
        [:div "Panel Three"]]
       [tab-panel/sub-panel {:panel-id :demo-navbar/four}
        [:div "Panel Four"]]]]))