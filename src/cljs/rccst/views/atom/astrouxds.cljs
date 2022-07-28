(ns rccst.views.atom.astrouxds
      "Catalog and demonstrations of available AstroUXDS components."
      (:require [rccst.views.atom.example.astrouxds.button :as button]
                [rccst.views.atom.example.astrouxds.classification-marking :as cm]
                [rccst.views.atom.example.astrouxds.classification-list :as cl]
                [rccst.views.atom.example.astrouxds.status :as status]
                [rccst.views.atom.example.astrouxds.progress-bar :as progress-bar]
                [rccst.views.atom.example.astrouxds.slider :as slider]
                [rccst.views.atom.example.astrouxds.radio-button :as rb]
                [rccst.views.atom.example.astrouxds.table :as table]))


(defn examples
      []

      [:div
       [button/example]
       [cl/example]
       [cm/example]
       [status/example]
       [progress-bar/example]
       [slider/example]
       [rb/example]
       [table/example]])
