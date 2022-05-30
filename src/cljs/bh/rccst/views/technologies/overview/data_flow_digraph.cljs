(ns bh.rccst.views.technologies.overview.data-flow-digraph
  (:require [woolybear.ad.layout :as layout]))


(defn page []
  [layout/page {:extra-classes :is-fluid}
   [:div.is-fluid
    [:h2.has-text-info "Data-flow Oriented Design"]
    [layout/markdown-block
     "> _a system design approach where data is transformed across a collection of 'processes',
> sometimes called 'automata' (nodes) that are connected (edges) via 'channels' (pub/sub, topics,
> queues, function calls, etc.)_

We have developed a mental model of software as a directed graph of data transformations. We see this
model as applicable at al levels of system design (requirements, architecture, modularity, deployment)
and in all aspects of system implementation (front-end, back-end, etc.).

This model fits perfectly within the idiomatic approach promoted by the Clojure functional programing language
we are developing in: transformations of immutable data, glued together into systems. Rich Hickey, creator of Clojure,
strongly promotes the use of queue as a system implementation mechanism (see [here](https://www.youtube.com/watch?v=LKtk3HCgTa8) and
[here](https://www.youtube.com/watch?v=ROor6_NGIWU)) to decouple the elements of computation/transformation.

As can be seen through a survey of dataflow system approaches, this model is often applied only to the 'services' portion,
often called the 'back-end', but we have determined that this approach is perfectly amenable to developing User Interfaces
(UIs), or 'front-ends'. This is a significant shift from how UI development is typically taught and practiced.



### How we got here.

The breakthrough came when we recognized that 'molecules', as introduced by Brad Frost as part of his [Atomic Design](),
in chemistry classes are physically modeled using balls and sticks, as show in Figure 1.

![Figure 1. Molecule Modeling Kit](/imgs/data-flow/chemical-modeling-kit.jpeg)

_Figure 1. Molecule Modeling Kit_

Combined with Clojure's, and most other functional programming languages', idiomatic approach promoting system composition from
transformations of data (using map, filter, and reduce), and the Re-frame notion of the
[_signal graph_](https://day8.github.io/re-frame/subscriptions/)
we saw that by placing the Signal Graph center of the design, rather than being a by product of a more typical approach, we
could unlock the power of the directed graph, simplifying both design and implementation.

We can also implement any and all UIs in a single, standard way, using a single, standard software mechanism, rather than
continually developing one-off, custom UI elements and composed widgets.

This approach also means we can apply the same System Engineering approach used for the services on the back-end to the
front-end as well! In fact, the front-end is not really a separate system as it is typically considered in the ad hoc approach, but
just one more collection of transformations of data within the boundary of the 'system' as a whole. However, instead of depositing
the results of a transformation into a database or a channel, queue, or topic, in the case of the UI, the results are converted into
HTML and 'deposited' on the User's display!

> ***Note:*** Although we've developed this sophisticated graph-based capability, ad we encourage you to use it,
> all the UI components are still available for use in developing more 'typical' ad-hoc UIs. The Data-flow approach
> is build _on top_ of the basic components. Developers get the best of both worlds!

### Transforming Data

Sticking with our chemistry analogy, chemical processes also perform transformations on the molecules, as
shown in Figure 2.

![Figure 2. Chemical outputs from the combustion of Ethanol_](/imgs/data-flow/combustion-reaction-model.jpeg)

_Figure 2. Chemical outputs from the combustion of Ethanol._

Note the arrow! Even in this simple example we can see the flow.


#### Types of transforms:

- filter : select only certain items or just parts of items, based upon some criterion.
- transform : convert the physical format; e.g., vector of hash-maps to vector of vectors, etc.
- augment : add data
- reduce : materialize a view over a collection of items (summation, group-by, etc.)

The flow can be defined and visualized as a directed graph (digraph)

One of the keys to designing software using data-flow is to focus on that data at the
beginning (the original source) and the end of the flow (typically the UI or the output)
of a microservice)


### Our Implementation

Since we are working in Clojure, and, on the UI side, using Re-frame, we take advantage of the Signal Graph
and take advantage of the [Layer 2 'Extractors' and Layer 3 'Materialized Views'](https://day8.github.io/re-frame/subscriptions/#the-four-layers)
as the primary mechanisms for the various data transformations needed to support the UI.

Layer 2 extractors are used for remote sources (`:source/remote`), while Layer 3 materialized views provide
the computational logic to produce the values for use with `:source/local`.

Layer 4, the View Functions, are provided by the various UI Components we have developed. The Catalog
show them and how they can be used, but within the Data-flow implementation, or more 'manually' as part
of some custom UI.

The 'magic' is that our implementation build the Layer 3 subscriptions programmatically, based upon the structure of the data
you define for the intermediary, what we call a `:source/local`. In those cases where you must perform some custom logic,
you also inject a `:source/fn` or 'source function' to produce the data, and the output can then be fed into other Layer 3 or
even Layer 4 subscriptions, depending upon how you wire everything together in the graph.

#### Development Process

Overall, the approach to developing using this technique is:

1. Identify the original source(s) of data, typically of 'type' `:source/remote`
2. Identify the UI components (i.e., `:ui/component`) to visualize the data
3. Determine the transformations from the source data to the visualization, specifically:
    - filtering
    - additional _enhancing_ data, sometimes called _mix-ins_
    - any re-formatting of the data (rearranging fields, changing hash-map keys, etc.)
4. Connect the various nodes to each other, from sources, through transformations, and into the UI components

One way to think of this is as a _directed graph_, with the sources at the top and the UI at the bottom;
the data flows _down hill_.

![Figure 3. A simplified directed graph of a UI.]()

_Figure 3. A simplified directed graph of a UI._

Let's walk through an example.

#### Example

The very simplest example is to take a single source and connect it directly to a UI component without
any additional processing, so:


![Figure 4. Simplest possible UI example.]()

_Figure 4. Simplest possible UI example._

As you can see, we have a single `:source/remote`, which we will call `:topic/data` and a single UI
components, which we will call `:ui/data-table`. By design, we give each element of the model a
name (`:topic/data` and `:ui/data-table`) so we can refer to them throughout the definition.

Each element also has a _type_ which tells the processing logic how to actually implement the required logic.
Our toolkit provides dozens of pre-built data and UI components, and you can always develop you own.



``` clojure
(def ui-definition
  {:components   {:topic/measurements {:type :source/remote :name :source/measurements}
                  :ui/bar-chart       {:type :ui/component :name :rechart/bar-2}}

   :links        {:topic/measurements {:data {:ui/bar-chart :data}}}

   :grid-layout  [{:i :ui/bar-chart :x 0 :y 0 :w 20 :h 11 :static true}]})
```


### Other Similar Approaches

- [Event Modeling](https://eventmodeling.org)
- [Stream processing](https://en.wikipedia.org/wiki/Stream_processing)
- [Reactive programming](https://en.wikipedia.org/wiki/Reactive_programming)
- [LabView](https://www.ni.com/getting-started/labview-basics/dataflow)
- [Power BI](https://en.wikipedia.org/wiki/Microsoft_Power_BI)
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



### Additional Links

- [Data Flow (Wikipedia)](https://en.wikipedia.org/wiki/Dataflow)
- [Dataflow programming (Wikipedia)](https://en.wikipedia.org/wiki/Dataflow_programming)
- [Kahn process network (Wikipedia)](https://en.wikipedia.org/wiki/Kahn_process_networks)

"]]])