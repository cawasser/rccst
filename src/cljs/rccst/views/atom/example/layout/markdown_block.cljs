(ns rccst.views.atom.example.layout.markdown-block
  (:require [reagent.core :as r]
            [woolybear.ad.catalog.utils :as acu]
            [woolybear.ad.layout :as layout]))


(defn example []
  (fn []
    (acu/demo "Markdown-Block"
      "Use Markdown blocks for [Markdown](https://en.wikipedia.org/wiki/Markdown) content."
      [layout/frame
       [layout/markdown-block "## You can make titles

  > or notes/quotes

  - even
  - bulleted
  - lists

  1. or
  2. numbered
  3. lists

  what about checkboxes:
  - [ ]  one
  - [ ]  two
  - [X]  three (which is checked)

  and a hyperlinks: [ring](https://github.com/ring-clojure/ring)


  tables?

  | thing     | description (right-justified)     |
  |:----------|----------------------------------:|
  | `thing-1` | a description for thing-1         |
  | `thing-2` | and for thing-2                   |

  even code-blocks:

  ``` clojure
  (defn func [a b]
     (+ a b))
  ```"]]

      '[layout/frame
        [layout/markdown-block "## You can make titles

  > or notes/quotes

  - even
  - bulleted
  - lists

  1. or
  2. numbered
  3. lists

  what about checkboxes:
  - [ ]  one
  - [ ]  two
  - [X]  three (which is checked)

  and a hyperlinks: [ring](https://github.com/ring-clojure/ring)

  tables?

  | thing     | description (right-justified)     |
  |:----------|----------------------------------:|
  | `thing-1` | a description for thing-1         |
  | `thing-2` | and for thing-2                   |

  even code-blocks:

  ``` clojure
  (defn func [a b]
     (+ a b))
  ```"]])))
