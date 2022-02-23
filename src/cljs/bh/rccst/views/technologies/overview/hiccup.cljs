(ns bh.rccst.views.technologies.overview.hiccup
  (:require [bh.rccst.views.technologies.overview.overview :as o]))


(defn overview []
  (o/overview "Hiccup"

    "#### Hiccup is a library for representing HTML in Clojure. It uses
    vectors to represent elements, and maps to represent an element's attributes.

Hiccup turns Clojure data structures like this:

  `[:a {:href \"http://github.com\"} \"GitHub\"]`

Into strings of HTML like this:

  `<a href=\"http://github.com\">GitHub</a>`

Hiccup provides a convenient shortcut for adding id and class attributes to an element.
Instead of writing:

  `[:div {:id \"email\" :class \"selected starred\"} \"...\"]`

You can write:

  `[:div#email.selected.starred \"...\"]`

As in CSS, the word after the \"#\" denotes the element's ID, and the word after each \".\" denotes
the element's classes.

There may be multiple classes, but there can only be one ID. Additionally, the ID must always come
first, so `div#foo.bar` would work, but `div.foo#bar` would not.

See also:
>[Hiccup](https://github.com/weavejester/hiccup)
>[reagent-project/reagent](https://github.com/reagent-project/reagent)"))

