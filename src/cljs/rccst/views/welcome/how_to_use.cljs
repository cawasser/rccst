(ns rccst.views.welcome.how-to-use
  (:require [rccst.views.technologies.overview.overview :as o]))


(defn overview []
  [o/overview
   "How to Use this Catalog"

   "This Catalog of UI Components serves many different purposes, for many different kinds of people, who
   are trying to solve many different kinds of problems. Specifically, we designed this Catalog to support the needs of:

- Developers
- Designers
- Product Owners
- End Users

#### Developers

For Developers, this catalog shows all the UI software components _in use_. You can see how the component works, what it looks like,
how it interacts with data and configuration, etc. Each example also includes a snippet
of source code that shows how you can use the component in your own solutions.

Additionally, by working with each component in
isolation, Developers can simplify and streamline the development of new, or custom, components. You can focus on building just the
functionality your clients need, without spending time an effort on extraneous details, like Servers, Databases, Deployment,
and other external concerns.

#### Designers

Much like Developers, Designers can use the Catalog to see the whole range of UI Components available for them to use
in their own solutions. In many cases, there are multiple options for a given Component. Sometimes they vary by the
source library they are drawn from (we re-package such components to make them interchangeable with minimal software code
changes). Sometimes they vary in how they work, with the different options suited to different situations. Having options
gives you the freedom to choose what works best for you and your clients.

#### Product Owners

Product Owners can use the Catalog to get a _sense of the possible_. Having the entire collection of components at your
fingertips can be both liberating, and somewhat overwhelming. But knowing what is possible can help you have more
meaningful conversations with your Users, Designers, and Developers.

#### End Users

End Users can use this Catalog in much the same way as the Product Owners; as a way to see what is possible. Many times, users
find it difficult to express their needs to technical people. Having a Catalog that everyone shares can help make it easier to
express your needs and desires in a concrete way, with the immediate feedback provided by everyone looking at the same visual
representation."])

