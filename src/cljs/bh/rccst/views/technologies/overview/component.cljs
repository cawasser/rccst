(ns bh.rccst.views.technologies.overview.component
  (:require [bh.rccst.views.technologies.overview.overview :as o]))

(defn overview []
      [o/overview
       "Component"
       "
The Component library allows the team to manage the lifecycle and dependencies of stateful software components.
An example would be a component built around database access.
Component is more of a design pattern with some supporting functions rather than a full blown library.

Visit the Component Git repository [here.](https://github.com/stuartsierra/component)"])

