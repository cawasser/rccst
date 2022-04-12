(ns bh.rccst.views.atom.example.misc.meta-bh-table
  (:require [bh.rccst.ui-component.atom.bh.table :as table]
            [bh.rccst.ui-component.utils :as ui-utils]
            [bh.rccst.ui-component.utils.helpers :as h]
            [reagent.core :as r]
            [taoensso.timbre :as log]
            [woolybear.ad.catalog.utils :as acu]
            [woolybear.ad.layout :as layout]))


(log/info "bh.rccst.views.atom.example.misc.meta-bh-table")


(defn- example-wrapper [component-id data]
  (let [id (r/atom nil)]

    (fn []
      (when (nil? @id)
        (reset! id (h/path->keyword component-id))

        ; this sets up the storage location in the app-db
        (ui-utils/init-widget @id {:data []})

        ; now we can add the data for the table to actually render
        (ui-utils/dispatch-local @id [:data] data))

      [table/table :data [(h/path->keyword @id :data)]])))


(defn example []
  (let [component-id "table-with-meta-demo"]
    (acu/demo "Basic Table with meta-data"
      "Table using HTML tags

Now with meta-data!

> This example also takes advantage of the component's ability to present data that it gets
> from it's parent either
> directly, via a Clojure [atom](https://clojure.org/reference/atoms), or via a
> re-frame [subscription](https://day8.github.io/re-frame/subscriptions/) to state held
> by the parent (container)."
      [layout/centered {:extra-classes :width-50}
       [example-wrapper component-id table/sample-meta-data]]
      '[table/table :data table/sample-meta-data])))

