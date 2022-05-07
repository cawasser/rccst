(ns bh.rccst.views.technologies.overview.re-com
  (:require [bh.rccst.views.technologies.overview.overview :as o]))

(defn overview []
      (o/overview "Re-com"

"#### _A ClojureScript library of UI components for Reagent_

**re-com provides:**
- Familiar UI widgets/components such as dropdowns, date pickers, popovers, tabs, etc.
- Layout components, which arrange widgets vertically and horizontally, within splitters, etc. Plus components which put borders
around their children. These various pieces can be arbitrarily nested to create sophisticated layouts.
- A mostly Bootstrap look, mixed with some Material Design Icons.
- UI basics to build a desktop-class SPA app.
- Use of the [Flexbox](https://css-tricks.com/snippets/css/a-guide-to-flexbox/). The entire layout side of this library
relies on Flexbox.

See also:
>[re-com/project](https://github.com/day8/re-com)
>[re-com/demo](https://re-com.day8.com.au/)"

"/imgs/re-com-logo.png"))
