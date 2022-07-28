(ns rccst.views.atom.example.icons.medium-icon
  (:require [woolybear.ad.catalog.utils :as acu]
            [woolybear.ad.containers :as containers]
            [woolybear.ad.icons :as icons]))


(defn example []
  (acu/demo "Medium Icons"
    [containers/bar
     [icons/icon {:icon "music" :size :medium}]
     [icons/icon {:icon "globe-americas" :size :medium}]
     [icons/icon {:icon "microphone" :size :medium}]
     [icons/icon {:icon "ellipsis-h" :size :medium}]]
    '[containers/bar
      [icons/icon {:icon "music" :size :medium}]
      [icons/icon {:icon "globe-americas" :size :medium}]
      [icons/icon {:icon "microphone" :size :medium}]
      [icons/icon {:icon "ellipsis-h" :size :medium}]]))
