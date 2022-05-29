(ns bh.rccst.views.technologies.overview.data-flow-digraph
  (:require [woolybear.ad.layout :as layout]
            [woolybear.packs.flex-panel :as flex]))


(defn page []
  [layout/page {:extra-classes :is-fluid}
   ;[flex/flex-panel {:extra-classes :is-fluid
   ;                  :height        "76vh"}
   ; [flex/flex-top {:extra-classes :is-fluid}
     [:div.is-fluid
      [:h2.has-text-info "Data-flow Oriented Design"]
      [layout/markdown-block
       "> _a system design approach where data is transformed across a collection of 'processes' (nodes)
> that are connected (edges) via 'channels' (pub/sub, topics, queues, function calls, etc.)_

We have developed a mental model of software as a directed graph of data transformations. We see this
model as applicable at al levels of system design (requirements, architecture, modularity, deployment)
and in all aspects of system implementation (front-end, back-end, etc.)

### Types of transforms:

- filter : select only certain items or just parts of items, based upon some criterion.
- transform : convert the physical format; e.g., vector of hash-maps to vector of vectors, etc.
- augment : add data
- reduce : materialize a view over a collection of items (summation, group-by, etc.)

The flow can be defined and visualized as a directed graph (digraph)


One of the keys to designing software using data-flow is to focus on that data at the
beginning (the original source) and the end of the flow (typically the UI or the output)
of a microservice)


### Other Similar Approaches

- [Event Modeling](https://eventmodeling.org)
- [Stream processing](https://en.wikipedia.org/wiki/Stream_processing)
- [Reactive programming](https://en.wikipedia.org/wiki/Reactive_programming)
- [LabView](https://www.ni.com/getting-started/labview-basics/dataflow)
- [Lucid programming language](https://en.wikipedia.org/wiki/Lucid_(programming_language))
- [Petri Nets](https://en.wikipedia.org/wiki/Petri_net)
- [React](https://reactjs.org/)
- [Re-frame](https://day8.github.io/re-frame/re-frame/)


### Differences from Dataflow programming

Dataflow programming (see [here]()), is often implemented (defined?) such that the processing steps
execute as soon as their inputs all become available. In contrast, in our implementation, there are
circumstances where the process will trigger when _any_ of the inputs are available (the other inputs are
assumed to have default values). Our approach assures that transformation updates will be computed whenever
any of the inputs change.

> Is this really true? Need to do some investigation/experimentation to determine.



### Links

- [Data Flow (Wikipedia)](https://en.wikipedia.org/wiki/Dataflow)
- [Dataflow programming (Wikipedia)](https://en.wikipedia.org/wiki/Dataflow_programming)
-
- [Kahn process network (Wikipedia)](https://en.wikipedia.org/wiki/Kahn_process_networks)

"]]])