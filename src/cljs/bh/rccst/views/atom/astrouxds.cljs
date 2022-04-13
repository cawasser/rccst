(ns bh.rccst.views.atom.astrouxds
      "Catalog and demonstrations of available AstroUXDS components."
      (:require [bh.rccst.views.atom.example.astrouxds.button :as button]
                [bh.rccst.views.atom.example.astrouxds.classification-marking :as cm]
                [bh.rccst.views.atom.example.astrouxds.classification-list :as cl]
                [bh.rccst.views.atom.example.astrouxds.status :as status]))


(defn examples
      []

      [:div
       [button/example]
       [cl/example]
       [cm/example]
       [status/example]])
