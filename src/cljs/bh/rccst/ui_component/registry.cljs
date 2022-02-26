(ns bh.rccst.ui-component.registry
  (:require [bh.rccst.ui-component.atom.chart.registry :as charts]))


(def ui-component-registry (merge {}
                             charts/registry))