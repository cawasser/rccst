(ns bh.rccst.views.atom.astrouxds
      "Catalog and demonstrations of available AstroUXDS components."
      (:require [bh.rccst.views.atom.example.astrouxds.button :as button]
                [bh.rccst.views.atom.example.astrouxds.classification-marking :as cm]
                [bh.rccst.views.atom.example.astrouxds.classification-list :as cl]
                [bh.rccst.views.atom.example.astrouxds.status :as status]
                [bh.rccst.views.atom.example.astrouxds.progress-bar :as progress-bar]
                [bh.rccst.views.atom.example.astrouxds.slider :as slider]
                [bh.rccst.views.atom.example.astrouxds.radio-button :as rb]))


(defn examples
      []

      [:div
       [button/example]
       [cl/example]
       [cm/example]
       [status/example]
       [progress-bar/example]
       [slider/example]
       [rb/example]])
