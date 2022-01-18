(ns bh.rccst.views.technologies.overview.re-com
  (:require [bh.rccst.views.technologies.overview.overview :as o]))

(defn overview []
      (o/overview "Re-com"

"#### A ClojureScript library of UI components for Reagent.\n
**re-com provides:**\n\n
- familiar UI widgetry components such as dropdowns, date pickers, popovers, tabs, etc.\n
- layout components, which arrange widgets vertically and horizontally, within splitters, etc. Plus components which put borders
around their children. These various pieces can be arbitrarily nested to create sophisticated layouts.\n
- a mostly Bootstrap look, mixed with some Material Design Icons.\n
- UI basics to build a desktop-class SPA app.\n
- Use of the [Flexbox](https://css-tricks.com/snippets/css/a-guide-to-flexbox/). The entire layout side of this library
relies on Flexbox.\n\n

See also:
>[re-com/project](https://github.com/day8/re-com)\n
>[re-com/demo](https://re-com.day8.com.au/)"

"/imgs/re-com-logo.png"))
