(ns bh.rccst.views.technologies.overview.data-flow.ui-tutorial
  (:require [woolybear.ad.layout :as layout]))


(defn- the-problem []
  [layout/frame {:extra-classes :is-fluid}
   [:h3 "The Problem"]
   [layout/markdown-block ""]])


(defn- first-steps []
  [layout/frame {:extra-classes :is-fluid}
   [:h3 "First Steps"]
   [layout/markdown-block ""]])


(defn- defining-the-components []
  [layout/frame {:extra-classes :is-fluid}
   [:h3 "Defining the Components"]
   [layout/markdown-block ""]])


(defn- linking-things []
  [layout/frame {:extra-classes :is-fluid}
   [:h3 "Linking Things"]
   [layout/markdown-block ""]])


(defn- layout-components []
  [layout/frame {:extra-classes :is-fluid}
   [:h3 "Layout"]
   [layout/markdown-block ""]])


(defn- testing-and-troubleshooting []
  [layout/frame {:extra-classes :is-fluid}
   [:h3 "Testing and Troubleshooting"]
   [layout/markdown-block ""]])


(defn tutorial []
  [layout/frame {:extra-classes :is-fluid}
   [:h2.has-text-info "Let's Build One!"]
   [layout/markdown-block
    ""]

   [the-problem]

   [first-steps]

   [defining-the-components]

   [linking-things]

   [layout-components]

   [testing-and-troubleshooting]])

