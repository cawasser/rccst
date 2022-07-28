(ns rccst.views.technologies.overview.reagent
  (:require [rccst.views.technologies.overview.overview :as o]))

(defn overview []
      (o/overview "Reagent"

        "#### _A simple [ClojureScript](https://github.com/clojure/clojurescript) interface to [React](https://reactjs.org/)_

Reagent provides a way to write efficient React components using (almost) nothing but plain ClojureScript functions.
Reagent uses Hiccup-like markup instead of React's sort-of html. The goal of Reagent is to make it possible to define
arbitrarily complex UIs using just a couple of basic concepts, and to be fast enough by default that you rarely have to
think about performance. The easiest way to manage state in Reagent is to use Reagent’s own version of atom. Sometimes
you may want to maintain state locally in a component. That is easy to do with an atom as well.
Another important feature of reagent is that a component function can return another function, that is used to do the actual rendering.
This function is called with the same arguments as the first one.

See also:
> [Reagent:Minimalistic React for Clojurescipt](https://reagent-project.github.io/)
> [reagent-project/reagent](https://github.com/reagent-project/reagent)"

"/imgs/reagent-logo.png"))
