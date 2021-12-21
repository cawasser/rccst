<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**Table of Contents**  *generated with [DocToc](https://github.com/thlorenz/doctoc)*

- [Decomplecting the Solution](#decomplecting-the-solution)
  - [Places in the Design that are Complected](#places-in-the-design-that-are-complected)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

# Decomplecting the Solution

First, some definitions:

**complect** _(verb)_

- to interweave, intertwine, or braid. verb form of _complex_

> _complect_ was brought into general use within the Clojure Community by Rich Hickey in his seminal presentation
> [Simple Made Easy](https://www.youtube.com/watch?v=LKtk3HCgTa8)

**decomplect** _(verb)_

- the act of removing complected elements from software design and/or source code


If "simplicity" is good, then "complexity" is bad, and we should always be on the lookout for places in
our designs or code that are intertwined (complected).

## Places in the Design that are Complected

- [X] bh.rccst.data-source.lookup complects handling the route with providing the schema for the response
  - create new namespace for the schema ([bh.rccst.data-source.lookup.schema]())
- [X] bh.rccst.data-source.lookup/lookup-handler complects creating the response with wrapping it for return
  - create new namespace and wrapper function ([bh.rccst.data-source.common]())
- [ ] 
