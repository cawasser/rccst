(ns rccst.views.atom.example.re-com.alert-box
  (:require [bh.ui-component.atom.bh.markdown :as markdown]
            [re-com.core :as rc]
            [reagent.core :as r]
            [woolybear.ad.catalog.utils :as acu]
            [woolybear.ad.layout :as layout]))


(defn example []
  (let [info-alert? (r/atom true)]
    (fn []
      (acu/demo "Alert Box"
        "We're using the `alert-box` from [Re-com](https://github.com/Day8/re-com)"

        [layout/centered {:extra-classes :width-50}
         [rc/alert-box :alert-type :warning
          :heading "Warning!"
          :body [markdown/markdown
                 "We can embed anything in an Alert Box. See [here](https://re-com.day8.com.au/#/alert-box)"]]

         (if @info-alert?
           [rc/alert-box :alert-type :info
            :id :alert/info-demo
            :heading ""
            :closeable? true
            :on-close #(reset! info-alert? false)
            :body "You can just put text into an Alert Box. 'Info' alerts are green.
         Notice that this one can also be closed"])

         [rc/alert-box :alert-type :danger
          :heading "STOP!"
          :body "You'll really want you users to read the RED alerts. Use :alert-type :danger"]]

        '(let [info-alert? (reagent/atom true)]
           [layout/centered {:extra-classes :width-50}
            [rc/alert-box :alert-type :warning
             :heading "Warning!"
             :body [markdown/markdown
                    "We can embed anything in an Alert Box. See [here](https://re-com.day8.com.au/#/alert-box)"]]

            (if @info-alert?
              [rc/alert-box :alert-type :info
               :id :alert/info-demo
               :heading ""
               :closeable? true
               :on-close #(reset! info-alert? false)
               :body "You can just put text into an Alert Box. 'Info' alerts are green.
             Notice that this one can also be closed"])

            [rc/alert-box :alert-type :danger
             :heading "STOP!"
             :body "You'll really want you users to read the RED alerts. Use :alert-type :danger"]])))))
