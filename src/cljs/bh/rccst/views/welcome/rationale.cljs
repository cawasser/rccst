(ns bh.rccst.views.welcome.rationale
  (:require [bh.rccst.views.technologies.overview.overview :as o]))


(defn overview []
  [o/overview
   "Rationale"
   "As proud members of the greater Clojure Community, we take seriously the notion that
every design decision should provide a rationale both to justify the effort and to outline the goals of the project.


As stated by Bruce Hauman in [the Devcards readme](https://github.com/bhauman/devcards#why):

> \"We primarily design and iterate on our front end applications inside the main application itself. In other words, our
> execution environment is constrained by the shape and demands of the application we are working on.
> This is extremely limiting.\"

We agree. Therefore, we developed this Catalog both as a means to show off the breadth of UI Components available to
us as Professional Software Developers, but also to allow us to work more quickly and more easily, by isolating the development
of the components from the develop of the User-facing solutions that will be built _from_ those components.


We are confident in this approach, as it has been adopted by a number of other Coompanies and Communities:

- [Astro UXDS](https://www.astrouxds.com)
- [Devcards](https://github.com/bhauman/devcards)
- [Workspaces](https://github.com/nubank/workspaces)
- [Storybook](https://storybook.js.org)
- [Carbon Design System](https://www.carbondesignsystem.com)

"])

