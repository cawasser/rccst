(ns bh.rccst.views.technologies.overview.atom-design
  (:require [bh.rccst.views.technologies.overview.overview :as o]))


(defn overview []
  (o/overview "Atomic Design"
    "#### _Composing Complex UIs from Simpler Elements_

> We’re not designing pages, we’re designing systems of components.
>
> — Stephen Hay

As the craft of Web design continues to evolve, we’re recognizing the need to develop thoughtful
design systems, rather than creating simple collections of web pages. A lot has been said about creating
design systems, and much of it focuses on establishing
foundations for color, typography, grids, texture and the like. This type of thinking is certainly
important, but I’m slightly less interested in these aspects of design because ultimately they are
and will always be subjective. Lately I’ve been more interested in what our interfaces are comprised
of and how we can construct design systems in a more methodical way.

In searching for inspiration and parallels, I kept coming back to chemistry. The thought is that all
matter (whether solid, liquid, gas, simple, complex, etc.) is comprised of atoms. Those atomic units
bond together to form molecules, which in turn combine into more complex organisms to ultimately create
all matter in our universe. Similarly, interfaces are made up of smaller components. This means we can
break entire interfaces
down into fundamental building blocks and work up from there. That’s the basic gist of atomic design.

- Brad Frost

![atomic-design](/imgs/atomic-design.png)

During our development of _modern_ user interfaces using Atomic Design and ClojureScript, we came to realized that
the molecule as an organized collection of atoms, like that shown below, was the key to unlocking a superpowered way
to develop User Interfaces.

![molecule](/imgs/glucose-molecule.jpg)

A molecule is a ***graph!*** The _atoms_ are the nodes, and the _bonds_ between them are the edges!

When combined with the notion of Event Modeling as a means to describe the entire system as a directed graph,
we consider the _directed graph_ as a single, powerful abstraction of Information Systems as a whole. We've developed
an approach to UI development which takes advantage of this powerful abstraction.


> For more information on Atomic Design, please see [Atomic Design](https://bradfrost.com/blog/post/atomic-web-design/)
    "))